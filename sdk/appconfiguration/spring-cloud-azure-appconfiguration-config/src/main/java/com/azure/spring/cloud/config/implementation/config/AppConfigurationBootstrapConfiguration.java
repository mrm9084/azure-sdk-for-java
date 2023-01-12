// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.config.implementation.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.azure.data.appconfiguration.ConfigurationClientBuilder;
import com.azure.spring.cloud.autoconfigure.context.AzureGlobalProperties;
import com.azure.spring.cloud.autoconfigure.implementation.appconfiguration.AzureAppConfigurationProperties;
import com.azure.spring.cloud.autoconfigure.implementation.properties.utils.AzureGlobalPropertiesUtils;
import com.azure.spring.cloud.config.AppConfigurationCredentialProvider;
import com.azure.spring.cloud.config.ConfigurationClientCustomizer;
import com.azure.spring.cloud.config.KeyVaultCredentialProvider;
import com.azure.spring.cloud.config.KeyVaultSecretProvider;
import com.azure.spring.cloud.config.SecretClientBuilderSetup;
import com.azure.spring.cloud.config.implementation.AppConfigurationKeyVaultClientFactory;
import com.azure.spring.cloud.config.implementation.AppConfigurationPropertySourceLocator;
import com.azure.spring.cloud.config.implementation.AppConfigurationReplicaClientFactory;
import com.azure.spring.cloud.config.implementation.AppConfigurationReplicaClientsBuilder;
import com.azure.spring.cloud.config.implementation.properties.AppConfigurationProperties;
import com.azure.spring.cloud.config.implementation.properties.AppConfigurationProviderProperties;
import com.azure.spring.cloud.core.customizer.AzureServiceClientBuilderCustomizer;
import com.azure.spring.cloud.core.implementation.util.AzureSpringIdentifier;
import com.azure.spring.cloud.core.properties.AzureProperties;
import com.azure.spring.cloud.core.provider.connectionstring.ServiceConnectionStringProvider;
import com.azure.spring.cloud.core.service.AzureServiceType;
import com.azure.spring.cloud.service.implementation.appconfiguration.ConfigurationClientBuilderFactory;

/**
 * Setup ConnectionPool, AppConfigurationPropertySourceLocator, and ClientStore when
 * <i>spring.cloud.azure.appconfiguration.enabled</i> is enabled.
 */
@Configuration
@EnableConfigurationProperties({ AppConfigurationProperties.class, AppConfigurationProviderProperties.class })
@ConditionalOnClass(AppConfigurationPropertySourceLocator.class)
@ConditionalOnProperty(prefix = AppConfigurationProperties.CONFIG_PREFIX, name = "enabled", matchIfMissing = true)
public class AppConfigurationBootstrapConfiguration {

    @Autowired
    private transient ApplicationContext context;

    /**
     *
     * @param properties Client properties
     * @param appProperties Library properties
     * @param clientFactory Store Connections
     * @param keyVaultClientFactory keyVaultClientFactory
     * @return AppConfigurationPropertySourceLocator
     * @throws IllegalArgumentException if both KeyVaultClientProvider and KeyVaultSecretProvider exist.
     */
    @Bean
    AppConfigurationPropertySourceLocator sourceLocator(AppConfigurationProperties properties,
        AppConfigurationProviderProperties appProperties, AppConfigurationReplicaClientFactory clientFactory,
        AppConfigurationKeyVaultClientFactory keyVaultClientFactory)
        throws IllegalArgumentException {

        return new AppConfigurationPropertySourceLocator(appProperties, clientFactory, keyVaultClientFactory,
            properties.getRefreshInterval(), properties.getStores());
    }

    /**
     * @throws IllegalArgumentException if both KeyVaultClientProvider and KeyVaultSecretProvider exist.
     */
    @Bean
    AppConfigurationKeyVaultClientFactory keyVaultClientFactory() throws IllegalArgumentException {
        KeyVaultCredentialProvider keyVaultCredentialProvider = context
            .getBeanProvider(KeyVaultCredentialProvider.class).getIfAvailable();
        SecretClientBuilderSetup keyVaultClientProvider = context.getBeanProvider(SecretClientBuilderSetup.class)
            .getIfAvailable();
        KeyVaultSecretProvider keyVaultSecretProvider = context.getBeanProvider(KeyVaultSecretProvider.class)
            .getIfAvailable();

        return new AppConfigurationKeyVaultClientFactory(keyVaultCredentialProvider, keyVaultClientProvider,
            keyVaultSecretProvider);
    }

    /**
     * Factory for working with App Configuration Clients
     *
     * @param clientBuilder Builder for configuration clients
     * @param properties Client configurations for setting up connections to each config store.
     * @return AppConfigurationReplicaClientFactory
     */
    @Bean
    @ConditionalOnMissingBean
    AppConfigurationReplicaClientFactory buildClientFactory(AppConfigurationReplicaClientsBuilder clientBuilder,
        AppConfigurationProperties properties) {
        return new AppConfigurationReplicaClientFactory(clientBuilder, properties.getStores());
    }

    /**
     * Builder for clients connecting to App Configuration.
     *
     * @param properties Client configurations for setting up connections to each config store.
     * @param appProperties Library configurations for setting up connections to each config store.
     * @return ClientStore
     */
    @Bean
    @ConditionalOnMissingBean
    AppConfigurationReplicaClientsBuilder replicaClientBuilder(AppConfigurationProviderProperties appProperties,
        AppConfigurationKeyVaultClientFactory keyVaultClientFactory, ConfigurationClientBuilderFactory clientFactory,
        Environment env) {

        AppConfigurationReplicaClientsBuilder clientBuilder = new AppConfigurationReplicaClientsBuilder(
            appProperties.getMaxRetries(), clientFactory, env);

        clientBuilder.setTokenCredentialProvider(
            context.getBeanProvider(AppConfigurationCredentialProvider.class).getIfAvailable());
        clientBuilder
            .setClientProvider(context.getBeanProvider(ConfigurationClientCustomizer.class)
                .getIfAvailable());

        clientBuilder.setIsKeyVaultConfigured(keyVaultClientFactory.isConfigured());

        return clientBuilder;
    }
    
    @ConfigurationProperties(prefix = AzureAppConfigurationProperties.PREFIX)
    @Bean
    AzureAppConfigurationProperties azureAppConfigurationProperties(AzureGlobalProperties azureProperties) {
        return loadProperties(azureProperties, new AzureAppConfigurationProperties());
    }
    
    protected <T extends AzureProperties> T loadProperties(AzureGlobalProperties source, T target) {
        return AzureGlobalPropertiesUtils.loadProperties(source, target);
    }

    
    @Bean
    @ConditionalOnMissingBean
    ConfigurationClientBuilderFactory configurationClientBuilderFactory(
        AzureAppConfigurationProperties properties,
        ObjectProvider<ServiceConnectionStringProvider<AzureServiceType.AppConfiguration>> connectionStringProviders,
        ObjectProvider<AzureServiceClientBuilderCustomizer<ConfigurationClientBuilder>> customizers) {
        ConfigurationClientBuilderFactory factory = new ConfigurationClientBuilderFactory(properties);

        factory.setSpringIdentifier(AzureSpringIdentifier.AZURE_SPRING_APP_CONFIG);
        connectionStringProviders.orderedStream().findFirst().ifPresent(factory::setConnectionStringProvider);
        customizers.orderedStream().forEach(factory::addBuilderCustomizer);
        return factory;
    }
}
