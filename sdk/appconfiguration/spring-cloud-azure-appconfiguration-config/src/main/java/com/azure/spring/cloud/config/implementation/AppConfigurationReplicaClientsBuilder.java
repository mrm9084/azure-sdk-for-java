// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.config.implementation;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.ExponentialBackoff;
import com.azure.core.http.policy.RetryPolicy;
import com.azure.core.http.policy.RetryStrategy;
import com.azure.data.appconfiguration.ConfigurationClientBuilder;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.spring.cloud.config.AppConfigurationCredentialProvider;
import com.azure.spring.cloud.config.ConfigurationClientCustomizer;
import com.azure.spring.cloud.config.implementation.pipline.policies.BaseAppConfigurationPolicy;
import com.azure.spring.cloud.config.implementation.properties.ConfigStore;
import com.azure.spring.cloud.service.implementation.appconfiguration.ConfigurationClientBuilderFactory;

public class AppConfigurationReplicaClientsBuilder implements EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfigurationReplicaClientsBuilder.class);

    /**
     * Invalid Connection String error message
     */
    public static final String NON_EMPTY_MSG = "%s property should not be null or empty in the connection string of Azure Config Service.";

    private static final Duration DEFAULT_MIN_RETRY_POLICY = Duration.ofMillis(800);

    private static final Duration DEFAULT_MAX_RETRY_POLICY = Duration.ofSeconds(8);

    /**
     * Connection String Regex format
     */
    private static final String CONN_STRING_REGEXP = "Endpoint=([^;]+);Id=([^;]+);Secret=([^;]+)";

    /**
     * Invalid Formatted Connection String Error message
     */
    public static final String ENDPOINT_ERR_MSG = String.format("Connection string does not follow format %s.",
        CONN_STRING_REGEXP);

    private static final Pattern CONN_STRING_PATTERN = Pattern.compile(CONN_STRING_REGEXP);

    private AppConfigurationCredentialProvider tokenCredentialProvider;

    private ConfigurationClientCustomizer clientProvider;

    private final ConfigurationClientBuilderFactory clientFactory;

    private final Environment env;

    private boolean isDev = false;

    private boolean isKeyVaultConfigured = false;

    private final int defaultMaxRetries;

    public AppConfigurationReplicaClientsBuilder(int defaultMaxRetries, ConfigurationClientBuilderFactory clientFactory, Environment env) {
        this.defaultMaxRetries = defaultMaxRetries;
        this.clientFactory = clientFactory;
        this.env = env;
    }

    /**
     * Given a connection string, returns the endpoint inside of it.
     * 
     * @param connectionString connection string to app configuration
     * @return endpoint
     * @throws IllegalStateException when connection string isn't valid.
     */
    public static String getEndpointFromConnectionString(String connectionString) {
        Assert.hasText(connectionString, "Connection string cannot be empty.");

        Matcher matcher = CONN_STRING_PATTERN.matcher(connectionString);
        if (!matcher.find()) {
            throw new IllegalStateException(ENDPOINT_ERR_MSG);
        }

        String endpoint = matcher.group(1);

        Assert.hasText(endpoint, String.format(NON_EMPTY_MSG, "Endpoint"));

        return endpoint;
    }

    /**
     * @param tokenCredentialProvider the tokenCredentialProvider to set
     */
    public void setTokenCredentialProvider(AppConfigurationCredentialProvider tokenCredentialProvider) {
        this.tokenCredentialProvider = tokenCredentialProvider;
    }

    /**
     * @param clientProvider the clientProvider to set
     */
    public void setClientProvider(ConfigurationClientCustomizer clientProvider) {
        this.clientProvider = clientProvider;
    }

    public void setIsKeyVaultConfigured(boolean isKeyVaultConfigured) {
        this.isKeyVaultConfigured = isKeyVaultConfigured;
    }

    /**
     * Builds all the clients for a connection.
     * 
     * @throws IllegalArgumentException when more than 1 connection method is given.
     */
    List<AppConfigurationReplicaClient> buildClients(ConfigStore configStore) {
        List<AppConfigurationReplicaClient> clients = new ArrayList<>();
        // Single client or Multiple?
        // If single call buildClient
        int hasSingleConnectionString = StringUtils.hasText(configStore.getConnectionString()) ? 1 : 0;
        int hasMultiEndpoints = configStore.getEndpoints().size() > 0 ? 1 : 0;
        int hasMultiConnectionString = configStore.getConnectionStrings().size() > 0 ? 1 : 0;

        if (hasSingleConnectionString + hasMultiEndpoints + hasMultiConnectionString > 1) {
            throw new IllegalArgumentException(
                "More than 1 Connection method was set for connecting to App Configuration.");
        }

        TokenCredential tokenCredential = null;

        if (tokenCredentialProvider != null) {
            tokenCredential = tokenCredentialProvider.getAppConfigCredential(configStore.getEndpoint());
        }

        boolean tokenCredentialIsPresent = tokenCredential != null;
        boolean connectionStringIsPresent = configStore.getConnectionString() != null;

        if (tokenCredentialIsPresent && connectionStringIsPresent) {
            throw new IllegalArgumentException(
                "More than 1 Connection method was set for connecting to App Configuration.");
        }

        List<String> connectionStrings = configStore.getConnectionStrings();
        List<String> endpoints = configStore.getEndpoints();

        if (connectionStrings.size() == 0 && StringUtils.hasText(configStore.getConnectionString())) {
            connectionStrings.add(configStore.getConnectionString());
        }

        if (endpoints.size() == 0 && StringUtils.hasText(configStore.getEndpoint())) {
            endpoints.add(configStore.getEndpoint());
        }

        if (connectionStrings.size() > 0) {
            for (String connectionString : connectionStrings) {
                String endpoint = getEndpointFromConnectionString(connectionString);
                LOGGER.debug("Connecting to " + endpoint + " using Connecting String.");
                ConfigurationClientBuilder builder = createBuilderInstance().connectionString(connectionString);
                clients.add(modifyAndBuildClient(builder, endpoint, connectionStrings.size() - 1));
            }
        } else {
            for (String endpoint : endpoints) {
                ConfigurationClientBuilder builder = this.createBuilderInstance();
                if (tokenCredential != null) {
                    // User Provided Token Credential
                    LOGGER.debug("Connecting to " + endpoint + " using AppConfigurationCredentialProvider.");
                    builder.credential(tokenCredential);
                } else {
                    // System Assigned Identity. Needs to be checked last as all of the above should
                    // have an Endpoint.
                    LOGGER.debug("Connecting to " + endpoint
                        + " using Azure System Assigned Identity or Azure User Assigned Identity.");
                    ManagedIdentityCredentialBuilder micBuilder = new ManagedIdentityCredentialBuilder();
                    builder.credential(micBuilder.build());
                }

                builder.endpoint(endpoint);

                clients.add(modifyAndBuildClient(builder, endpoint, endpoints.size() - 1));
            }
        }
        return clients;
    }

    private AppConfigurationReplicaClient modifyAndBuildClient(ConfigurationClientBuilder builder, String endpoint,
        Integer replicaCount) {
        builder.addPolicy(new BaseAppConfigurationPolicy(isDev, isKeyVaultConfigured, replicaCount));

        if (clientProvider != null) {
            clientProvider.setup(builder, endpoint);
        }
        return new AppConfigurationReplicaClient(endpoint, builder.buildClient());
    }

    @Override
    public void setEnvironment(Environment environment) {
        for (String profile : environment.getActiveProfiles()) {
            if ("dev".equalsIgnoreCase(profile)) {
                this.isDev = true;
                break;
            }
        }
    }

    protected ConfigurationClientBuilder createBuilderInstance() {
        RetryStrategy retryStatagy = null;

        String mode = env.getProperty("spring.cloud.azure.retry.mode", "exponential");

        if ("exponential".equals(mode)) {
            String maxRetries = env.getProperty("spring.cloud.azure.retry.exponential.max-retries");
            int retries = defaultMaxRetries;

            if (maxRetries != null) {
                try {
                    retries = Integer.valueOf(maxRetries);
                } catch (NumberFormatException e) {
                    LOGGER.warn(
                        "spring.cloud.azure.retry.exponential.max-retries isn't a valid integer, using default value.");
                }
            }

            String baseDelayEnv = env.getProperty("spring.cloud.azure.retry.exponential.base-delay");
            Duration baseDelay = DEFAULT_MIN_RETRY_POLICY;

            if (baseDelayEnv != null) {
                try {
                    baseDelay = Duration.parse(baseDelayEnv);
                } catch (DateTimeParseException e) {
                    LOGGER.warn(
                        "spring.cloud.azure.retry.exponential.base-delay isn't a valid Duration, using default value.");
                }
            }

            String maxDelayEnv = env.getProperty("spring.cloud.azure.retry.exponential.max-delay");
            Duration maxDelay = DEFAULT_MAX_RETRY_POLICY;

            if (maxDelayEnv != null) {
                try {
                    maxDelay = Duration.parse(maxDelayEnv);
                } catch (DateTimeParseException e) {
                    LOGGER.warn(
                        "spring.cloud.azure.retry.exponential.base-delay isn't a valid Duration, using default value.");
                }
            }

            retryStatagy = new ExponentialBackoff(retries, baseDelay, maxDelay);
        }
        
        ConfigurationClientBuilder builder = clientFactory.build();
        
        if (retryStatagy != null) {
            builder.retryPolicy(new RetryPolicy(retryStatagy));
        }
        
        return builder;
    }
}
