// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.appconfiguration.config.implementation;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.azure.data.appconfiguration.models.ConfigurationSetting;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationKeyValueSelector;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationProviderProperties;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationStoreMonitoring;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationStoreTrigger;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.ConfigStore;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.FeatureFlagKeyValueSelector;

/**
 * Locates Azure App Configuration Property Sources.
 */
public final class AppConfigurationPropertySourceFactory {

    private final AppConfigurationProviderProperties appProperties;

    private final AppConfigurationReplicaClientFactory clientFactory;

    private final AppConfigurationKeyVaultClientFactory keyVaultClientFactory;

    private Duration refreshInterval;

    public AppConfigurationPropertySourceFactory(AppConfigurationProviderProperties appProperties,
        AppConfigurationReplicaClientFactory clientFactory, AppConfigurationKeyVaultClientFactory keyVaultClientFactory,
        Duration refreshInterval) {
        this.refreshInterval = refreshInterval;
        this.appProperties = appProperties;
        this.clientFactory = clientFactory;
        this.keyVaultClientFactory = keyVaultClientFactory;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfigurationPropertySourceFactory.class);

    List<AppConfigurationPropertySource> build(ConfigStore configStore, List<String> profiles, StateHolder newState,
        Boolean startup) throws InterruptedException {
        Boolean first = true; 
        while (startup || first) {
            first = false;
            List<AppConfigurationReplicaClient> clients = clientFactory.getAvailableClients(configStore.getEndpoint(),
                true);
            List<AppConfigurationPropertySource> sourceList = new ArrayList<>();
            boolean reloadFailed = false;
            for (AppConfigurationReplicaClient client : clients) {
                sourceList = new ArrayList<>();

                if (!startup && reloadFailed && !AppConfigurationRefreshUtil
                    .checkStoreAfterRefreshFailed(client, clientFactory, configStore.getFeatureFlags(), profiles)) {
                    // This store doesn't have any changes where to refresh store did. Skipping Checking next.
                    continue;
                }

                // Reverse in order to add Profile specific properties earlier, and last profile comes first
                try {
                    List<AppConfigurationPropertySource> sources = create(client, configStore,
                        profiles);
                    sourceList.addAll(sources);

                    LOGGER.debug("PropertySource context.");
                    setupMonitoring(configStore, client, sources, newState);

                    return sourceList;
                } catch (AppConfigurationStatusException e) {
                    // TODO NEW CODE HERE
                    reloadFailed = true;
                    clientFactory.backoffClientClient(configStore.getEndpoint(), client.getEndpoint());
                } catch (Exception e) {
                    newState = failedToGeneratePropertySource(configStore, newState, e, startup);

                    // Not a retriable error
                    return null;
                }
            }
        }
        return null;
    }

    private void setupMonitoring(ConfigStore configStore, AppConfigurationReplicaClient client,
        List<AppConfigurationPropertySource> sources, StateHolder newState) {
        AppConfigurationStoreMonitoring monitoring = configStore.getMonitoring();

        if (configStore.getFeatureFlags().getEnabled()) {
            List<ConfigurationSetting> watchKeysFeatures = getFeatureFlagWatchKeys(configStore, sources);
            newState.setStateFeatureFlag(configStore.getEndpoint(), watchKeysFeatures,
                monitoring.getFeatureFlagRefreshInterval());
        }

        if (monitoring.isEnabled()) {
            // Setting new ETag values for Watch
            List<ConfigurationSetting> watchKeysSettings = getWatchKeys(client, monitoring.getTriggers());

            newState.setState(configStore.getEndpoint(), watchKeysSettings, monitoring.getRefreshInterval());
        }
        newState.setLoadState(configStore.getEndpoint(), true, configStore.isFailFast());
        newState.setLoadStateFeatureFlag(configStore.getEndpoint(), configStore.getFeatureFlags().getEnabled(),
            configStore.isFailFast());
    }

    private List<ConfigurationSetting> getWatchKeys(AppConfigurationReplicaClient client,
        List<AppConfigurationStoreTrigger> triggers) {
        List<ConfigurationSetting> watchKeysSettings = new ArrayList<>();
        for (AppConfigurationStoreTrigger trigger : triggers) {
            ConfigurationSetting watchKey = client.getWatchKey(trigger.getKey(), trigger.getLabel());
            if (watchKey != null) {
                watchKeysSettings.add(watchKey);
            } else {
                watchKeysSettings.add(new ConfigurationSetting().setKey(trigger.getKey()).setLabel(trigger.getLabel()));
            }
        }
        return watchKeysSettings;
    }

    private List<ConfigurationSetting> getFeatureFlagWatchKeys(ConfigStore configStore,
        List<AppConfigurationPropertySource> sources) {
        List<ConfigurationSetting> watchKeysFeatures = new ArrayList<>();
        if (configStore.getFeatureFlags().getEnabled()) {
            for (AppConfigurationPropertySource propertySource : sources) {
                if (propertySource instanceof AppConfigurationFeatureManagementPropertySource) {
                    watchKeysFeatures.addAll(
                        ((AppConfigurationFeatureManagementPropertySource) propertySource).getFeatureFlagSettings());
                }
            }
        }
        return watchKeysFeatures;
    }

    StateHolder failedToGeneratePropertySource(ConfigStore configStore, StateHolder newState, Exception e,
        Boolean startup) throws InterruptedException {
        String message = "Failed to generate property sources for " + configStore.getEndpoint();
        if (!startup) {
            // Need to check for refresh first, or reset will never happen if fail fast is true.
            LOGGER.error("Refreshing failed while reading configuration from Azure App Configuration store "
                + configStore.getEndpoint() + ".");

            if (refreshInterval != null) {
                // The next refresh will happen sooner if refresh interval is expired.
                newState.updateNextRefreshTime(refreshInterval, appProperties.getDefaultMinBackoff());
            }
            throw new RuntimeException(message, e);
        } else if (configStore.isFailFast()) {
            LOGGER.error("Fail fast is set and there was an error reading configuration from Azure App "
                + "Configuration store " + configStore.getEndpoint() + ".");
            delayException(appProperties);
            throw new RuntimeException(message, e);
        } else {
            LOGGER.warn(
                "Unable to load configuration from Azure AppConfiguration store " + configStore.getEndpoint() + ".", e);
            newState.setLoadState(configStore.getEndpoint(), false, configStore.isFailFast());
            newState.setLoadStateFeatureFlag(configStore.getEndpoint(), false, configStore.isFailFast());
        }
        return newState;
    }

    /**
     * Creates a new set of AppConfigurationPropertySources, 1 per Label.
     *
     * @param client client for connecting to App Configuration
     * @param store Config Store the PropertySource is being generated from
     * @param profiles active profiles to be used as labels. it needs to be in the last one.
     * @return a list of AppConfigurationPropertySources
     * @throws Exception creating a property source failed
     */
    private List<AppConfigurationPropertySource> create(AppConfigurationReplicaClient client, ConfigStore store,
        List<String> profiles) throws Exception {
        List<AppConfigurationPropertySource> sourceList = new ArrayList<>();
        List<AppConfigurationKeyValueSelector> selects = store.getSelects();

        if (store.getFeatureFlags().getEnabled()) {
            for (FeatureFlagKeyValueSelector selectedKeys : store.getFeatureFlags().getSelects()) {
                AppConfigurationFeatureManagementPropertySource propertySource = new AppConfigurationFeatureManagementPropertySource(
                    store.getEndpoint(), client,
                    selectedKeys.getKeyFilter(), selectedKeys.getLabelFilter(profiles));

                propertySource.initProperties(null);
                sourceList.add(propertySource);
            }
        }

        for (AppConfigurationKeyValueSelector selectedKeys : selects) {
            AppConfigurationPropertySource propertySource = null;

            if (StringUtils.hasText(selectedKeys.getSnapshotName())) {
                propertySource = new AppConfigurationSnapshotPropertySource(
                    selectedKeys.getSnapshotName() + "/" + store.getEndpoint(), client, keyVaultClientFactory,
                    selectedKeys.getSnapshotName());
            } else {
                propertySource = new AppConfigurationApplicationSettingPropertySource(
                    selectedKeys.getKeyFilter() + store.getEndpoint() + "/", client, keyVaultClientFactory,
                    selectedKeys.getKeyFilter(), selectedKeys.getLabelFilter(profiles));
            }
            propertySource.initProperties(store.getTrimKeyPrefix());
            sourceList.add(propertySource);

        }

        return sourceList;
    }

    private void delayException(AppConfigurationProviderProperties appProperties) throws InterruptedException {
        Instant currentDate = Instant.now();
        Instant preKillTIme = appProperties.getStartDate().plusSeconds(appProperties.getPrekillTime());
        if (currentDate.isBefore(preKillTIme)) {
            long diffInMillies = Math.abs(preKillTIme.toEpochMilli() - currentDate.toEpochMilli());
            try {
                Thread.sleep(diffInMillies);
            } catch (InterruptedException e) {
                LOGGER.error("Failed to wait before fast fail.");
                throw e;
            }
        }
    }
}
