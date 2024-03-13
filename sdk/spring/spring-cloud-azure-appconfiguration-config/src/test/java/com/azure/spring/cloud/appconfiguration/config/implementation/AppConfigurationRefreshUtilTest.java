// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.appconfiguration.config.implementation;

import static com.azure.spring.cloud.appconfiguration.config.implementation.AppConfigurationConstants.EMPTY_LABEL;
import static com.azure.spring.cloud.appconfiguration.config.implementation.AppConfigurationConstants.FEATURE_FLAG_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.azure.core.http.MatchConditions;
import com.azure.core.http.rest.PagedResponse;
import com.azure.data.appconfiguration.models.ConfigurationSetting;
import com.azure.data.appconfiguration.models.FeatureFlagConfigurationSetting;
import com.azure.data.appconfiguration.models.SettingSelector;
import com.azure.spring.cloud.appconfiguration.config.implementation.AppConfigurationRefreshUtil.RefreshEventData;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationStoreMonitoring;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.ConfigStore;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.FeatureFlagKeyValueSelector;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.FeatureFlagStore;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AppConfigurationRefreshUtilTest {

    private static final String KEY_FILTER = "/application/*";

    @Mock
    private AppConfigurationReplicaClient clientMock;

    @Mock
    private AppConfigurationReplicaClientFactory clientFactoryMock;

    @Mock
    private ConfigurationSetting configurationMock;

    @Mock
    private AppConfigurationReplicaClient clientOriginMock;

    @Mock
    private StateHolder currentStateMock;

    @Mock
    private ConnectionManager connectionManagerMock;

    @Mock
    private PagedResponse<ConfigurationSetting> pagedResponseMock;

    @Mock
    private StateHolder stateHolderMockReturn;

    private ConfigStore configStore;

    private String endpoint;

    private Mono<RefreshEventData> eventData = Mono.just(new RefreshEventData());

    private final List<AppConfigurationReplicaClient> clients = new ArrayList<>();

    private final List<ConfigurationSetting> watchKeys = generateWatchKeys();

    private final List<ConfigurationSetting> watchKeysFeatureFlags = generateFeatureFlagWatchKeys();

    private final AppConfigurationStoreMonitoring monitoring = new AppConfigurationStoreMonitoring();

    private final FeatureFlagStore featureStore = new FeatureFlagStore();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        configStore = new ConfigStore();

        featureStore.setEnabled(true);

        List<FeatureFlagKeyValueSelector> ffSelects = new ArrayList<>();
        FeatureFlagKeyValueSelector ffSelect = new FeatureFlagKeyValueSelector().setKeyFilter(FEATURE_FLAG_PREFIX)
            .setLabelFilter(EMPTY_LABEL);
        ffSelects.add(ffSelect);
        featureStore.setSelects(ffSelects);
        configStore.setFeatureFlags(featureStore);

        monitoring.setEnabled(true);
        featureStore.setEnabled(true);
    }

    @Test
    public void refreshWithoutTimeWatchKeyConfigStoreNotLoaded(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        when(clientMock.getWatchKey(Mockito.eq(KEY_FILTER), Mockito.eq(EMPTY_LABEL)))
            .thenReturn(Mono.just(watchKeys.get(0)));
        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadState(endpoint)).thenReturn(false);

            assertFalse(
                AppConfigurationRefreshUtil.checkStoreAfterRefreshFailed(clientMock, clientFactoryMock, featureStore));
        }
    }

    @Test
    public void refreshWithoutTimeWatchKeyConfigStoreWatchKeyNotReturned(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        State newState = new State(watchKeys, Math.toIntExact(Duration.ofMinutes(10).getSeconds()), endpoint);

        // Config Store doesn't return a watch key change.
        when(clientMock.getWatchKey(Mockito.eq(KEY_FILTER), Mockito.eq(EMPTY_LABEL)))
            .thenReturn(Mono.just(watchKeys.get(0)));
        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadState(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getState(endpoint)).thenReturn(newState);

            assertFalse(
                AppConfigurationRefreshUtil.checkStoreAfterRefreshFailed(clientMock, clientFactoryMock, featureStore));

        }
    }

    @Test
    public void refreshWithoutTimeWatchKeyConfigStoreWatchKeyNoChange(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);
        when(clientMock.checkWatchKeys(Mockito.any())).thenReturn(Flux.empty());

        ConfigurationSetting updatedWatchKey = new ConfigurationSetting().setKey(KEY_FILTER).setLabel(EMPTY_LABEL)
            .setETag("updated");

        Map<SettingSelector, MatchConditions> originalFFETags = new HashMap<>();
        originalFFETags.put(new SettingSelector().setKeyFilter(FEATURE_FLAG_PREFIX + "*").setLabelFilter(EMPTY_LABEL),
            new MatchConditions());

        State newState = new State(originalFFETags, Math.toIntExact(Duration.ofMinutes(10).getSeconds()), endpoint);

        List<ConfigurationSetting> listedKeys = new ArrayList<>();
        listedKeys.add(watchKeysFeatureFlags.get(0));

        // Config Store does return a watch key change.
        when(clientMock.getWatchKey(Mockito.eq(KEY_FILTER), Mockito.eq(EMPTY_LABEL)))
            .thenReturn(Mono.just(updatedWatchKey));
        when(clientMock.listSettings(Mockito.any(SettingSelector.class))).thenReturn(Flux.just(configurationMock));
        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getStateFeatureFlag(endpoint)).thenReturn(newState);

            assertFalse(
                AppConfigurationRefreshUtil.checkStoreAfterRefreshFailed(clientMock, clientFactoryMock, featureStore));

        }
    }

    @Test
    public void refreshWithoutTimeFeatureFlagDisabled(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        configStore.getFeatureFlags().setEnabled(false);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(false);

            assertFalse(
                AppConfigurationRefreshUtil.checkStoreAfterRefreshFailed(clientMock, clientFactoryMock, featureStore));

        }
    }

    @Test
    public void refreshWithoutTimeFeatureFlagNotLoaded(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        configStore.getFeatureFlags().setEnabled(true);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(false);

            assertFalse(
                AppConfigurationRefreshUtil.checkStoreAfterRefreshFailed(clientMock, clientFactoryMock, featureStore));

        }
    }

    @Test
    public void refreshWithoutTimeFeatureFlagNoChange(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);
        when(clientOriginMock.getEndpoint()).thenReturn(endpoint);
        when(clientMock.checkWatchKeys(Mockito.any())).thenReturn(Flux.just());

        Map<SettingSelector, MatchConditions> originalFFETags = new HashMap<>();
        originalFFETags.put(new SettingSelector().setKeyFilter(FEATURE_FLAG_PREFIX + "*").setLabelFilter(EMPTY_LABEL),
            new MatchConditions());

        State newState = new State(originalFFETags, Math.toIntExact(Duration.ofMinutes(10).getSeconds()),
            endpoint);

        // Config Store doesn't return a watch key change.
        when(clientMock.listSettings(Mockito.any(SettingSelector.class))).thenReturn(Flux.just(configurationMock));

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getStateFeatureFlag(endpoint)).thenReturn(newState);

            assertFalse(
                AppConfigurationRefreshUtil.checkStoreAfterRefreshFailed(clientMock, clientFactoryMock, featureStore));

        }

    }

    @Test
    public void refreshWithoutTimeFeatureFlagNoWatchKeyReturned(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        Map<SettingSelector, MatchConditions> originalFFETags = new HashMap<>();
        originalFFETags.put(new SettingSelector().setKeyFilter(FEATURE_FLAG_PREFIX + "*").setLabelFilter(EMPTY_LABEL),
            new MatchConditions());

        State newState = new State(originalFFETags, Math.toIntExact(Duration.ofMinutes(10).getSeconds()),
            endpoint);

        // Config Store does return a watch key change.
        when(clientMock.listSettings(Mockito.any(SettingSelector.class))).thenReturn(Flux.just(configurationMock));
        when(clientOriginMock.getEndpoint()).thenReturn(endpoint);
        when(clientMock.checkWatchKeys(Mockito.any())).thenReturn(Flux.just(pagedResponseMock));

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getStateFeatureFlag(endpoint)).thenReturn(newState);

            assertTrue(
                AppConfigurationRefreshUtil.checkStoreAfterRefreshFailed(clientMock, clientFactoryMock, featureStore));

        }
    }

    @Test
    public void refreshWithoutTimeFeatureFlagWasDeleted(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);
        when(clientMock.checkWatchKeys(Mockito.any())).thenReturn(Flux.just(pagedResponseMock));

        // Config Store doesn't return a value, Feature Flag was deleted
        when(clientMock.listSettings(Mockito.any(SettingSelector.class))).thenReturn(Flux.just(configurationMock));

        Map<SettingSelector, MatchConditions> originalFFETags = new HashMap<>();
        originalFFETags.put(new SettingSelector().setKeyFilter(FEATURE_FLAG_PREFIX + "*").setLabelFilter(EMPTY_LABEL),
            new MatchConditions());

        State newState = new State(originalFFETags, Math.toIntExact(Duration.ofMinutes(10).getSeconds()),
            endpoint);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getStateFeatureFlag(endpoint)).thenReturn(newState);

            assertTrue(
                AppConfigurationRefreshUtil.checkStoreAfterRefreshFailed(clientMock, clientFactoryMock, featureStore));

        }
    }

    @Test
    public void refreshWithoutTimeFeatureFlagWasAdded(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);
        when(clientMock.checkWatchKeys(Mockito.any())).thenReturn(Flux.just(pagedResponseMock));

        Map<SettingSelector, MatchConditions> originalFFETags = new HashMap<>();
        originalFFETags.put(new SettingSelector().setKeyFilter(FEATURE_FLAG_PREFIX + "*").setLabelFilter(EMPTY_LABEL),
            new MatchConditions());

        State newState = new State(originalFFETags, Math.toIntExact(Duration.ofMinutes(10).getSeconds()),
            endpoint);

        // Config Store returns a new feature flag
        when(clientMock.listSettings(Mockito.any(SettingSelector.class))).thenReturn(Flux.just(configurationMock));
        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getStateFeatureFlag(endpoint)).thenReturn(newState);

            assertTrue(
                AppConfigurationRefreshUtil.checkStoreAfterRefreshFailed(clientMock, clientFactoryMock, featureStore));

        }
    }

    @Test
    public void refreshStoresCheckSettingsTestNotEnabled(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        clients.add(clientOriginMock);

        Map<String, ConnectionManager> connections = new HashMap<>();
        connections.put(endpoint, connectionManagerMock);

        when(connectionManagerMock.getMonitoring()).thenReturn(monitoring);
        when(connectionManagerMock.getFeatureFlagStore()).thenReturn(featureStore);
        when(clientFactoryMock.getConnections()).thenReturn(connections);

        when(clientFactoryMock.getAvailableClients(Mockito.eq(endpoint))).thenReturn(clients);

        State newState = new State(generateWatchKeys(), Math.toIntExact(Duration.ofMinutes(10).getSeconds()), endpoint);

        // Config Store doesn't return a watch key change.
        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadState(endpoint)).thenReturn(false);
            stateHolderMock.when(() -> StateHolder.getState(endpoint)).thenReturn(newState);

            // Monitor is disabled
            eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock, Duration.ofMinutes(10),
                new ArrayList<>(), (long) 60);
            assertFalse(eventData.block().getDoRefresh());
            verify(clientFactoryMock, times(1)).setCurrentConfigStoreClient(Mockito.eq(endpoint), Mockito.eq(endpoint));
            verify(clientOriginMock, times(0)).getWatchKey(Mockito.anyString(), Mockito.anyString());
        }
    }

    @Test
    public void refreshStoresCheckSettingsTestNotLoaded(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        clients.add(clientOriginMock);

        Map<String, ConnectionManager> connections = new HashMap<>();
        connections.put(endpoint, connectionManagerMock);

        when(connectionManagerMock.getMonitoring()).thenReturn(monitoring);
        when(connectionManagerMock.getFeatureFlagStore()).thenReturn(featureStore);
        when(clientFactoryMock.getConnections()).thenReturn(connections);

        when(clientFactoryMock.getAvailableClients(Mockito.eq(endpoint))).thenReturn(clients);

        State newState = new State(generateWatchKeys(), Math.toIntExact(Duration.ofMinutes(10).getSeconds()), endpoint);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadState(endpoint)).thenReturn(false);
            stateHolderMock.when(() -> StateHolder.getState(endpoint)).thenReturn(newState);

            eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock, Duration.ofMinutes(10),
                new ArrayList<>(), (long) 60);
            assertFalse(eventData.block().getDoRefresh());
            verify(clientFactoryMock, times(1)).setCurrentConfigStoreClient(Mockito.eq(endpoint), Mockito.eq(endpoint));
            verify(clientOriginMock, times(0)).getWatchKey(Mockito.anyString(), Mockito.anyString());
        }
    }

    @Test
    public void refreshStoresCheckSettingsTestNotRefreshTime(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        clients.add(clientOriginMock);

        Map<String, ConnectionManager> connections = new HashMap<>();
        connections.put(endpoint, connectionManagerMock);

        when(connectionManagerMock.getMonitoring()).thenReturn(monitoring);
        when(connectionManagerMock.getFeatureFlagStore()).thenReturn(featureStore);
        when(clientFactoryMock.getConnections()).thenReturn(connections);

        // Not Refresh Time
        when(clientFactoryMock.getAvailableClients(Mockito.eq(endpoint))).thenReturn(clients);

        State newState = new State(generateWatchKeys(), Math.toIntExact(Duration.ofMinutes(10).getSeconds()), endpoint);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadState(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getState(endpoint)).thenReturn(newState);

            eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock, Duration.ofMinutes(10),
                new ArrayList<>(), (long) 60);
            assertFalse(eventData.block().getDoRefresh());
            verify(clientFactoryMock, times(1)).setCurrentConfigStoreClient(Mockito.eq(endpoint), Mockito.eq(endpoint));
            verify(clientOriginMock, times(0)).getWatchKey(Mockito.anyString(), Mockito.anyString());
        }
    }

    @Test
    public void refreshStoresCheckSettingsTestFailedRequest(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);
        when(clientOriginMock.getWatchKey(Mockito.any(), Mockito.any()))
            .thenThrow(new AppConfigurationStatusException(null, null, null));

        clients.add(clientOriginMock);

        Map<String, ConnectionManager> connections = new HashMap<>();
        connections.put(endpoint, connectionManagerMock);

        when(connectionManagerMock.getMonitoring()).thenReturn(monitoring);
        when(connectionManagerMock.getFeatureFlagStore()).thenReturn(featureStore);
        when(clientFactoryMock.getConnections()).thenReturn(connections);

        // Refresh Time, but failed watch request
        when(clientFactoryMock.getAvailableClients(Mockito.eq(endpoint))).thenReturn(clients);

        State newState = new State(generateWatchKeys(), Math.toIntExact(Duration.ofMinutes(-1).getSeconds()), endpoint);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadState(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getState(endpoint)).thenReturn(newState);
            stateHolderMock.when(() -> StateHolder.getCurrentState()).thenReturn(stateHolderMockReturn);

            eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock, Duration.ofMinutes(10),
                new ArrayList<>(), (long) 60);

            RefreshEventData myEvent = eventData.block();

            assertFalse(myEvent.getDoRefresh());
            verify(clientFactoryMock, times(1)).setCurrentConfigStoreClient(Mockito.eq(endpoint), Mockito.eq(endpoint));
            verify(clientOriginMock, times(1)).getWatchKey(Mockito.anyString(), Mockito.anyString());
            assertEquals(newState, StateHolder.getState(endpoint));
        }
    }

    @Test
    public void refreshStoresCheckSettingsTestRefreshTimeNoChange(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        clients.add(clientOriginMock);

        Map<String, ConnectionManager> connections = new HashMap<>();
        connections.put(endpoint, connectionManagerMock);

        when(connectionManagerMock.getMonitoring()).thenReturn(monitoring);
        when(connectionManagerMock.getFeatureFlagStore()).thenReturn(featureStore);
        when(clientFactoryMock.getConnections()).thenReturn(connections);

        // Refresh Time, but no change
        when(clientFactoryMock.getAvailableClients(Mockito.eq(endpoint))).thenReturn(clients);
        when(clientOriginMock.getWatchKey(Mockito.anyString(), Mockito.anyString())).thenReturn(Mono.empty());

        State newState = new State(generateWatchKeys(), Math.toIntExact(Duration.ofMinutes(-1).getSeconds()), endpoint);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadState(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getState(Mockito.any())).thenReturn(newState);
            StateHolder updatedStateHolder = new StateHolder();
            stateHolderMock.when(() -> StateHolder.getCurrentState()).thenReturn(updatedStateHolder);

            eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock, Duration.ofMinutes(10),
                new ArrayList<>(), (long) 60);
            assertEquals(newState, StateHolder.getState(endpoint));
            assertFalse(eventData.block().getDoRefresh());
            verify(clientFactoryMock, times(1)).setCurrentConfigStoreClient(Mockito.eq(endpoint), Mockito.eq(endpoint));
            verify(clientOriginMock, times(1)).getWatchKey(Mockito.anyString(), Mockito.anyString());
        }
    }

    @Test
    public void refreshStoresCheckSettingsTestTriggerRefresh(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        clients.add(clientOriginMock);

        Map<String, ConnectionManager> connections = new HashMap<>();
        connections.put(endpoint, connectionManagerMock);

        when(connectionManagerMock.getMonitoring()).thenReturn(monitoring);
        when(connectionManagerMock.getFeatureFlagStore()).thenReturn(featureStore);
        when(clientFactoryMock.getConnections()).thenReturn(connections);

        // Refresh Time, trigger refresh
        when(clientFactoryMock.getAvailableClients(Mockito.eq(endpoint))).thenReturn(clients);

        ConfigurationSetting refreshKey = new ConfigurationSetting().setKey(KEY_FILTER).setLabel(EMPTY_LABEL)
            .setETag("new");

        when(clientOriginMock.getWatchKey(Mockito.anyString(), Mockito.anyString())).thenReturn(Mono.just(refreshKey));

        State newState = new State(generateWatchKeys(), Math.toIntExact(Duration.ofMinutes(-1).getSeconds()), endpoint);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadState(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getState(endpoint)).thenReturn(newState);
            stateHolderMock.when(StateHolder::getCurrentState).thenReturn(currentStateMock);

            eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock, Duration.ofMinutes(10),
                new ArrayList<>(), (long) 60);
            assertTrue(eventData.block().getDoRefresh());
            verify(clientFactoryMock, times(1)).setCurrentConfigStoreClient(Mockito.eq(endpoint), Mockito.eq(endpoint));
            verify(clientOriginMock, times(1)).getWatchKey(Mockito.anyString(), Mockito.anyString());
            verify(currentStateMock, times(1)).updateStateRefresh(Mockito.any(), Mockito.any());
        }
    }

    @Test
    public void refreshStoresCheckFeatureFlagTestNotLoaded(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        clients.add(clientOriginMock);

        Map<String, ConnectionManager> connections = new HashMap<>();
        connections.put(endpoint, connectionManagerMock);

        when(connectionManagerMock.getMonitoring()).thenReturn(monitoring);
        when(connectionManagerMock.getFeatureFlagStore()).thenReturn(featureStore);
        when(clientFactoryMock.getConnections()).thenReturn(connections);

        when(clientFactoryMock.getAvailableClients(Mockito.eq(endpoint))).thenReturn(clients);

        State newState = new State(generateFeatureFlagWatchKeys(), Math.toIntExact(Duration.ofMinutes(10).getSeconds()),
            endpoint);

        // Config Store doesn't return a watch key change.
        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(false);
            stateHolderMock.when(() -> StateHolder.getStateFeatureFlag(endpoint)).thenReturn(newState);

            // Monitor is disabled
            eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock, Duration.ofMinutes(10),
                new ArrayList<>(), (long) 60);
            assertFalse(eventData.block().getDoRefresh());
            verify(clientFactoryMock, times(1)).setCurrentConfigStoreClient(Mockito.eq(endpoint), Mockito.eq(endpoint));
            verify(clientOriginMock, times(0)).getWatchKey(Mockito.anyString(), Mockito.anyString());
        }
    }

    @Test
    public void refreshStoresCheckFeatureFlagTestNotRefreshTime(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);
        when(clientOriginMock.getEndpoint()).thenReturn(endpoint);
        when(clientMock.checkWatchKeys(Mockito.any())).thenReturn(Flux.just(pagedResponseMock));

        clients.add(clientOriginMock);

        Map<String, ConnectionManager> connections = new HashMap<>();
        connections.put(endpoint, connectionManagerMock);

        when(connectionManagerMock.getMonitoring()).thenReturn(monitoring);
        when(connectionManagerMock.getFeatureFlagStore()).thenReturn(featureStore);
        when(clientFactoryMock.getConnections()).thenReturn(connections);

        when(clientFactoryMock.getAvailableClients(Mockito.eq(endpoint))).thenReturn(clients);

        Map<SettingSelector, MatchConditions> originalFFETags = new HashMap<>();
        originalFFETags.put(new SettingSelector().setKeyFilter(FEATURE_FLAG_PREFIX + "*").setLabelFilter(EMPTY_LABEL),
            new MatchConditions());

        State newState = new State(originalFFETags, Math.toIntExact(Duration.ofMinutes(10).getSeconds()),
            endpoint);

        // Config Store doesn't return a watch key change.
        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getStateFeatureFlag(endpoint)).thenReturn(newState);
            stateHolderMock.when(StateHolder::getCurrentState).thenReturn(currentStateMock);

            // Monitor is disabled
            eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock, Duration.ofMinutes(10),
                new ArrayList<>(), (long) 60);
            assertFalse(eventData.block().getDoRefresh());
            verify(clientFactoryMock, times(1)).setCurrentConfigStoreClient(Mockito.eq(endpoint), Mockito.eq(endpoint));
            verify(clientOriginMock, times(0)).getWatchKey(Mockito.anyString(), Mockito.anyString());
        }
    }

    @Test
    public void refreshStoresCheckFeatureFlagTestNoChange(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);
        when(clientOriginMock.getEndpoint()).thenReturn(endpoint);
        when(clientMock.checkWatchKeys(Mockito.any())).thenReturn(Flux.just(pagedResponseMock));

        clients.add(clientOriginMock);
        configStore.setEndpoint(endpoint);
        configStore.setFeatureFlags(featureStore);
        configStore.setMonitoring(monitoring);

        List<ConfigurationSetting> listedKeys = new ArrayList<>();
        listedKeys.add(watchKeysFeatureFlags.get(0));

        Map<String, ConnectionManager> connections = new HashMap<>();
        connections.put(endpoint, connectionManagerMock);

        when(connectionManagerMock.getMonitoring()).thenReturn(monitoring);
        when(connectionManagerMock.getFeatureFlagStore()).thenReturn(featureStore);
        when(clientFactoryMock.getConnections()).thenReturn(connections);

        when(clientFactoryMock.getAvailableClients(Mockito.eq(endpoint))).thenReturn(clients);
        when(clientOriginMock.listSettings(Mockito.any())).thenReturn(Flux.just(configurationMock));
        when(clientOriginMock.checkWatchKeys(Mockito.any())).thenReturn(Flux.empty());

        Map<SettingSelector, MatchConditions> originalFFETags = new HashMap<>();
        originalFFETags.put(new SettingSelector().setKeyFilter(FEATURE_FLAG_PREFIX + "*").setLabelFilter(EMPTY_LABEL),
            new MatchConditions());

        State newState = new State(originalFFETags, Math.toIntExact(Duration.ofMinutes(-1).getSeconds()),
            endpoint);

        // Config Store doesn't return a watch key change.
        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getStateFeatureFlag(endpoint)).thenReturn(newState);
            stateHolderMock.when(StateHolder::getCurrentState).thenReturn(currentStateMock);
            stateHolderMock.when(StateHolder::getCurrentState).thenReturn(currentStateMock);
            // Monitor is disabled
            eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock, Duration.ofMinutes(10),
                new ArrayList<>(), (long) 60);
            assertFalse(eventData.block().getDoRefresh());
            verify(clientFactoryMock, times(1)).setCurrentConfigStoreClient(Mockito.eq(endpoint), Mockito.eq(endpoint));
            verify(clientOriginMock, times(0)).getWatchKey(Mockito.anyString(), Mockito.anyString());
            verify(currentStateMock, times(1)).updateStateRefresh(Mockito.any(), Mockito.any());

        }
    }

    @Test
    public void refreshStoresCheckFeatureFlagTestTriggerRefresh(TestInfo testInfo) {
        endpoint = testInfo.getDisplayName() + ".azconfig.io";
        when(clientMock.getEndpoint()).thenReturn(endpoint);

        clients.add(clientOriginMock);

        Map<String, ConnectionManager> connections = new HashMap<>();
        connections.put(endpoint, connectionManagerMock);

        when(connectionManagerMock.getMonitoring()).thenReturn(monitoring);
        when(connectionManagerMock.getFeatureFlagStore()).thenReturn(featureStore);
        when(clientFactoryMock.getConnections()).thenReturn(connections);

        List<ConfigurationSetting> listedKeys = new ArrayList<>();

        FeatureFlagConfigurationSetting updated = (FeatureFlagConfigurationSetting) watchKeysFeatureFlags.get(0);
        updated.setETag("new");

        listedKeys.add(updated);

        when(clientFactoryMock.getAvailableClients(Mockito.eq(endpoint))).thenReturn(clients);
        when(clientOriginMock.listSettings(Mockito.any())).thenReturn(Flux.just(configurationMock));
        when(clientOriginMock.getEndpoint()).thenReturn(endpoint);
        when(clientOriginMock.checkWatchKeys(Mockito.any())).thenReturn(Flux.just(pagedResponseMock));

        Map<SettingSelector, MatchConditions> originalFFETags = new HashMap<>();
        originalFFETags.put(new SettingSelector().setKeyFilter(FEATURE_FLAG_PREFIX + "*").setLabelFilter(EMPTY_LABEL),
            new MatchConditions());

        State newState = new State(originalFFETags, Math.toIntExact(Duration.ofMinutes(-1).getSeconds()),
            endpoint);

        // Config Store doesn't return a watch key change.
        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadStateFeatureFlag(endpoint)).thenReturn(true);
            stateHolderMock.when(() -> StateHolder.getStateFeatureFlag(endpoint)).thenReturn(newState);
            stateHolderMock.when(StateHolder::getCurrentState).thenReturn(currentStateMock);

            // Monitor is disabled
            eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock, Duration.ofMinutes(10),
                new ArrayList<>(), (long) 60);
            assertTrue(eventData.block().getDoRefresh());
            verify(clientFactoryMock, times(1)).setCurrentConfigStoreClient(Mockito.eq(endpoint), Mockito.eq(endpoint));
            verify(clientOriginMock, times(0)).getWatchKey(Mockito.anyString(), Mockito.anyString());
            verify(currentStateMock, times(1)).updateStateRefresh(Mockito.any(), Mockito.any());
        }
    }

    @Test
    public void minRefreshPeriodTest() {
        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getNextForcedRefresh()).thenReturn(Instant.now().minusSeconds(600));
            Mono<RefreshEventData> eventData = AppConfigurationRefreshUtil.refreshStoresCheck(clientFactoryMock,
                Duration.ofMinutes(1), new ArrayList<String>(), (long) 0);
            RefreshEventData event = eventData.block();
            assertTrue(event.getDoRefresh());
            assertEquals("Minimum refresh period reached. Refreshing configurations.", event.getMessage());
        }
    }

    private List<ConfigurationSetting> generateWatchKeys() {
        List<ConfigurationSetting> watchKeys = new ArrayList<>();

        ConfigurationSetting currentWatchKey = new ConfigurationSetting().setKey(KEY_FILTER).setLabel(EMPTY_LABEL)
            .setETag("current");

        watchKeys.add(currentWatchKey);
        return watchKeys;
    }

    private List<ConfigurationSetting> generateFeatureFlagWatchKeys() {
        List<ConfigurationSetting> watchKeys = new ArrayList<>();

        FeatureFlagConfigurationSetting currentWatchKey = new FeatureFlagConfigurationSetting("Alpha", false)
            .setETag("current").setLabel(EMPTY_LABEL);
        watchKeys.add(currentWatchKey);
        return watchKeys;
    }
}
