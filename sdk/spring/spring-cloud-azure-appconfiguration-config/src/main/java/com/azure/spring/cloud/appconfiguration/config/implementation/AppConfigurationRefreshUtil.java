// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.appconfiguration.config.implementation;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azure.core.http.MatchConditions;
import com.azure.core.http.rest.PagedResponse;
import com.azure.data.appconfiguration.models.ConfigurationSetting;
import com.azure.data.appconfiguration.models.SettingSelector;
import com.azure.spring.cloud.appconfiguration.config.implementation.http.policy.BaseAppConfigurationPolicy;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationStoreMonitoring;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.FeatureFlagStore;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class AppConfigurationRefreshUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfigurationPullRefresh.class);

    /**
     * Goes through each config store and checks if any of its keys need to be refreshed. If any store has a value that
     * needs to be updated a refresh event is called after every store is checked.
     *
     * @return If a refresh event is called.
     */
    static Mono<RefreshEventData> refreshStoresCheck(AppConfigurationReplicaClientFactory clientFactory,
        Duration refreshInterval, List<String> profiles, Long defaultMinBackoff) {
        BaseAppConfigurationPolicy.setWatchRequests(true);

        Flux<RefreshEventData> configEventData = Flux.just(new RefreshEventData());
        Flux<PagedResponse<ConfigurationSetting>> changes = Flux.empty();

        try {
            if (refreshInterval != null && StateHolder.getNextForcedRefresh() != null
                && Instant.now().isAfter(StateHolder.getNextForcedRefresh())) {
                String eventDataInfo = "Minimum refresh period reached. Refreshing configurations.";

                LOGGER.info(eventDataInfo);
                RefreshEventData eventData = new RefreshEventData();
                eventData.setFullMessage(eventDataInfo);
                return Mono.just(eventData);
            }

            for (Entry<String, ConnectionManager> entry : clientFactory.getConnections().entrySet()) {
                String originEndpoint = entry.getKey();
                ConnectionManager connection = entry.getValue();
                // For safety reset current used replica.
                clientFactory.setCurrentConfigStoreClient(originEndpoint, originEndpoint);

                AppConfigurationStoreMonitoring monitor = connection.getMonitoring();

                List<AppConfigurationReplicaClient> clients = clientFactory.getAvailableClients(originEndpoint);

                if (monitor.isEnabled() && StateHolder.getLoadState(originEndpoint)) {
                    for (AppConfigurationReplicaClient client : clients) {
                        try {
                            configEventData = Flux.concat(configEventData, refreshWithTime(client,
                                StateHolder.getState(originEndpoint), monitor.getRefreshInterval()));
                            clientFactory.setCurrentConfigStoreClient(originEndpoint, client.getEndpoint());
                        } catch (AppConfigurationStatusException e) {
                            LOGGER.warn(
                                "Failed attempting to connect to " + client.getEndpoint() + " during refresh check.");

                            clientFactory.backoffClientClient(originEndpoint, client.getEndpoint());
                        }
                    }
                } else {
                    LOGGER.debug("Skipping configuration refresh check for " + originEndpoint);
                }

                FeatureFlagStore featureStore = connection.getFeatureFlagStore();

                if (featureStore.getEnabled() && StateHolder.getLoadStateFeatureFlag(originEndpoint)) {
                    for (AppConfigurationReplicaClient client : clients) {
                        try {
                            changes = Flux.concat(changes,
                                refreshWithTimeFeatureFlags(client, monitor.getFeatureFlagRefreshInterval()));

                            // If check didn't throw an error other clients don't need to be checked.
                            break;
                        } catch (AppConfigurationStatusException e) {
                            LOGGER.warn(
                                "Failed attempting to connect to " + client.getEndpoint() + " during refresh check.");

                            clientFactory.backoffClientClient(originEndpoint, client.getEndpoint());
                        }
                    }
                } else {
                    LOGGER.debug("Skipping feature flag refresh check for " + originEndpoint);
                }

            }
        } catch (Exception e) {
            // The next refresh will happen sooner if refresh interval is expired.
            StateHolder current = StateHolder.getCurrentState();
            
            current.updateNextRefreshTime(refreshInterval, defaultMinBackoff);
            throw e;
        }

        Flux<RefreshEventData> ffEventData = changes.map(result -> {
            RefreshEventData featureFlagEventData = new RefreshEventData();
            LOGGER.info("Configuration Refresh Event triggered by Feature Flag change.");
            featureFlagEventData.setMessage("Configuration Refresh Event triggered by Feature Flag change.");

            return featureFlagEventData;
        });

        return Flux.concat(configEventData, ffEventData).reduce((a, b) -> {
            if (!a.getDoRefresh() && !b.getDoRefresh()) {
                return a;
            }
            if (a.getDoRefresh() && !b.getDoRefresh()) {
                return a;
            }
            if (b.getDoRefresh() && !a.getDoRefresh()) {
                return b;
            }

            return new RefreshEventData().setFullMessage(a.getMessage() + "\n" + b.getMessage());
        });
    }

    static boolean checkStoreAfterRefreshFailed(AppConfigurationReplicaClient client,
        AppConfigurationReplicaClientFactory clientFactory, FeatureFlagStore featureStore) {
        List<RefreshEventData> configResult = refreshStoreCheck(client,
            clientFactory.findOriginForEndpoint(client.getEndpoint())).collectList().block();
        for (RefreshEventData eventData : configResult) {
            if (eventData.getDoRefresh()) {
                return true;
            }
        }
        List<Boolean> featureFlagResults = refreshStoreFeatureFlagCheck(featureStore, client).collectList().block();
        for (Boolean featureFlagResult : featureFlagResults) {
            if (featureFlagResult) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is for a <b>refresh fail only</b>.
     *
     * @param client Client checking for refresh
     * @param originEndpoint config store origin endpoint
     * @return A refresh should be triggered.
     */
    private static Flux<RefreshEventData> refreshStoreCheck(AppConfigurationReplicaClient client,
        String originEndpoint) {
        Flux<RefreshEventData> eventData = Flux.just(new RefreshEventData());
        if (StateHolder.getLoadState(originEndpoint)) {
            eventData = refreshWithoutTime(client, StateHolder.getState(originEndpoint).getWatchKeys());
        }
        return eventData;
    }

    /**
     * This is for a <b>refresh fail only</b>.
     * 
     * @param featureStore Feature info for the store
     * @param profiles Current configured profiles, can be used as labels.
     * @param client Client checking for refresh
     * @return true if a refresh should be triggered.
     */
    private static Flux<Boolean> refreshStoreFeatureFlagCheck(FeatureFlagStore featureStore,
        AppConfigurationReplicaClient client) {
        String endpoint = client.getEndpoint();

        if (featureStore.getEnabled() && StateHolder.getLoadStateFeatureFlag(endpoint)) {
            return refreshWithoutTimeFeatureFlags(client).map(response -> {
                if (response == null) {
                    return false;
                }
                return true;
            });
        } else {
            LOGGER.debug("Skipping feature flag refresh check for " + endpoint);
        }
        return Flux.just(false);
    }

    /**
     * Checks refresh trigger for etag changes. If they have changed a RefreshEventData is published.
     *
     * @param state The refresh state of the endpoint being checked.
     * @param refreshInterval Amount of time to wait until next check of this endpoint.
     * @param eventData Info for this refresh event.
     * @return
     */
    private static Flux<RefreshEventData> refreshWithTime(AppConfigurationReplicaClient client, State state,
        Duration refreshInterval) throws AppConfigurationStatusException {
        Flux<RefreshEventData> eventData = Flux.just(new RefreshEventData());
        if (Instant.now().isAfter(state.getNextRefreshCheck())) {

            eventData = refreshWithoutTime(client, state.getWatchKeys());

            StateHolder.getCurrentState().updateStateRefresh(state, refreshInterval);
        }
        return eventData;
    }

    /**
     * Checks refresh trigger for etag changes. If they have changed a RefreshEventData is published.
     *
     * @param client Client checking for refresh
     * @param watchKeys Watch keys for the store.
     * @param eventData Refresh event info
     * @return
     */
    private static Flux<RefreshEventData> refreshWithoutTime(AppConfigurationReplicaClient client,
        List<ConfigurationSetting> watchKeys) throws AppConfigurationStatusException {
        List<Mono<RefreshEventData>> watchedKeys = new ArrayList<>();

        for (ConfigurationSetting watchKey : watchKeys) {
            Mono<RefreshEventData> watchedKey = client.getWatchKey(watchKey.getKey(), watchKey.getLabel())
                .map(configurationSetting -> {
                    return checkETag(watchKey, configurationSetting, client.getEndpoint());
                });
            watchedKeys.add(watchedKey);
        }
        return Flux.concat(watchedKeys);
    }

    private static Flux<PagedResponse<ConfigurationSetting>> refreshWithTimeFeatureFlags(
        AppConfigurationReplicaClient client, Duration refreshInterval)
        throws AppConfigurationStatusException {
        Instant date = Instant.now();
        State state = StateHolder.getStateFeatureFlag(client.getEndpoint());
        if (date.isAfter(state.getNextRefreshCheck())) {

            List<Flux<PagedResponse<ConfigurationSetting>>> changes = new ArrayList<>();

            for (Entry<SettingSelector, MatchConditions> entry : state.getWatchKeysff().entrySet()) {
                changes.add(client.checkWatchKeys(entry.getKey().setMatchConditions(List.of(entry.getValue()))));
            }

            StateHolder.getCurrentState().updateStateRefresh(state, refreshInterval);
            return Flux.concat(changes);
        }
        StateHolder.getCurrentState().updateStateRefresh(state, refreshInterval);
        return Flux.empty();
    }

    private static Flux<PagedResponse<ConfigurationSetting>> refreshWithoutTimeFeatureFlags(
        AppConfigurationReplicaClient client) throws AppConfigurationStatusException {

        State state = StateHolder.getStateFeatureFlag(client.getEndpoint());

        List<Flux<PagedResponse<ConfigurationSetting>>> changes = new ArrayList<>();

        for (Entry<SettingSelector, MatchConditions> entry : state.getWatchKeysff().entrySet()) {
            changes.add(client.checkWatchKeys(entry.getKey().setMatchConditions(List.of(entry.getValue()))));
        }

        return Flux.concat(changes);
    }

    private static RefreshEventData checkETag(ConfigurationSetting watchSetting,
        ConfigurationSetting currentTriggerConfiguration,
        String endpoint) {
        RefreshEventData eventData = new RefreshEventData();
        if (currentTriggerConfiguration == null) {
            return eventData;
        }

        LOGGER.debug(watchSetting.getETag(), " - ", currentTriggerConfiguration.getETag());
        if (watchSetting.getETag() != null && !watchSetting.getETag().equals(currentTriggerConfiguration.getETag())) {
            LOGGER.trace("Some keys in store [{}] matching the key [{}] and label [{}] is updated, "
                + "will send refresh event.", endpoint, watchSetting.getKey(), watchSetting.getLabel());

            String eventDataInfo = watchSetting.getKey();

            // Only one refresh Event needs to be call to update all of the
            // stores, not one for each.
            LOGGER.info("Configuration Refresh Event triggered by " + eventDataInfo);
            eventData.setMessage(eventDataInfo);
        }
        return eventData;
    }

    /**
     * For each refresh, multiple etags can change, but even one etag is changed, refresh is required.
     */
    static class RefreshEventData {

        private static final String MSG_TEMPLATE = "Some keys matching %s has been updated since last check.";

        private String message;

        private boolean doRefresh = false;

        RefreshEventData() {
            this.message = "";
        }

        RefreshEventData setMessage(String prefix) {
            setFullMessage(String.format(MSG_TEMPLATE, prefix));
            return this;
        }

        RefreshEventData setFullMessage(String message) {
            this.message = message;
            this.doRefresh = true;
            return this;
        }

        public String getMessage() {
            return this.message;
        }

        public boolean getDoRefresh() {
            return doRefresh;
        }
    }
}
