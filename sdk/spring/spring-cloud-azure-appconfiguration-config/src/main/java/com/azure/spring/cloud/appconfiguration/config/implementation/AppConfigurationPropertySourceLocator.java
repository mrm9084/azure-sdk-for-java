// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.appconfiguration.config.implementation;

import static org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration.BOOTSTRAP_PROPERTY_SOURCE_NAME;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import com.azure.spring.cloud.appconfiguration.config.implementation.autofailover.ReplicaLookUp;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationKeyValueSelector;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationProviderProperties;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.ConfigStore;

/**
 * Locates Azure App Configuration Property Sources.
 */
public final class AppConfigurationPropertySourceLocator implements PropertySourceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfigurationPropertySourceLocator.class);

    private static final String PROPERTY_SOURCE_NAME = "azure-config-store";

    private static final String REFRESH_ARGS_PROPERTY_SOURCE = "refreshArgs";

    private final List<ConfigStore> configStores;

    private final ReplicaLookUp replicaLookUp;

    private final AppConfigurationPropertySourceFactory propertySourceFactory;

    private Duration refreshInterval;

    private final Duration startupTimeout;

    static final AtomicBoolean STARTUP = new AtomicBoolean(true);

    /**
     * Loads all Azure App Configuration Property Sources configured.
     * 
     * @param properties Configurations for stores to be loaded.
     * @param appProperties Configurations for the library.
     * @param clientFactory factory for creating clients for connecting to Azure App Configuration.
     * @param keyVaultClientFactory factory for creating clients for connecting to Azure Key Vault
     */
    public AppConfigurationPropertySourceLocator(AppConfigurationProviderProperties appProperties,
        AppConfigurationKeyVaultClientFactory keyVaultClientFactory,
        Duration refreshInterval, List<ConfigStore> configStores, ReplicaLookUp replicaLookUp,
        AppConfigurationPropertySourceFactory propertySourceFactory, Duration startupTimeout) {
        this.refreshInterval = refreshInterval;
        this.configStores = configStores;
        this.replicaLookUp = replicaLookUp;
        this.propertySourceFactory = propertySourceFactory;
        this.startupTimeout = startupTimeout;

        BackoffTimeCalculator.setDefaults(appProperties.getDefaultMaxBackoff(), appProperties.getDefaultMinBackoff());
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        if (!(environment instanceof ConfigurableEnvironment)) {
            return null;
        }
        replicaLookUp.updateAutoFailoverEndpoints();

        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        boolean currentlyLoaded = env.getPropertySources().stream().anyMatch(source -> {
            String storeName = configStores.get(0).getEndpoint();
            AppConfigurationKeyValueSelector selectedKey = configStores.get(0).getSelects().get(0);
            return source.getName()
                .startsWith(BOOTSTRAP_PROPERTY_SOURCE_NAME + "-" + selectedKey.getKeyFilter() + storeName + "/");
        });
        if (currentlyLoaded && !env.getPropertySources().contains(REFRESH_ARGS_PROPERTY_SOURCE)) {
            return null;
        }

        List<String> profiles = Arrays.asList(env.getActiveProfiles());

        CompositePropertySource composite = new CompositePropertySource(PROPERTY_SOURCE_NAME);
        Collections.reverse(configStores); // Last store has the highest precedence

        StateHolder newState = new StateHolder();
        newState.setNextForcedRefresh(refreshInterval);

        // Feature Management needs to be set in the last config store.
        try {
            for (ConfigStore configStore : configStores) {
                boolean loadNewPropertySources = STARTUP.get() || StateHolder.getLoadState(configStore.getEndpoint());

                if (configStore.isEnabled() && loadNewPropertySources) {
                    // There is only one Feature Set for all AppConfigurationPropertySources
                    List<AppConfigurationPropertySource> sourceList = propertySourceFactory.build(configStore, profiles,
                        newState, STARTUP.get(), startupTimeout);

                    if (sourceList != null) {
                        // Updating list of propertySources
                        sourceList.forEach(composite::addPropertySource);
                    } else if (!STARTUP.get() || (configStore.isFailFast() && STARTUP.get())) {
                        String message = "Failed to generate property sources for " + configStore.getEndpoint();

                        // Refresh failed for a config store ending attempt
                        propertySourceFactory.failedToGeneratePropertySource(configStore, newState,
                            new RuntimeException(message), STARTUP.get());
                    }

                } else if (!configStore.isEnabled() && loadNewPropertySources) {
                    LOGGER.info("Not loading configurations from {} as it is not enabled.", configStore.getEndpoint());
                } else {
                    LOGGER.warn("Not loading configurations from {} as it failed on startup.",
                        configStore.getEndpoint());
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        StateHolder.updateState(newState);
        STARTUP.set(false);

        return composite;
    }
}
