// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.appconfiguration.config.implementation;

import static com.azure.spring.cloud.appconfiguration.config.implementation.AppConfigurationConstants.EMPTY_LABEL;
import static com.azure.spring.cloud.appconfiguration.config.implementation.TestConstants.TEST_STORE_NAME;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.springframework.core.env.PropertySource;

import com.azure.core.http.rest.PagedFlux;
import com.azure.core.http.rest.PagedResponse;
import com.azure.data.appconfiguration.models.ConfigurationSetting;
import com.azure.data.appconfiguration.models.FeatureFlagConfigurationSetting;
import com.azure.spring.cloud.appconfiguration.config.implementation.autofailover.ReplicaLookUp;
import com.azure.spring.cloud.appconfiguration.config.implementation.feature.entity.Feature;
import com.azure.spring.cloud.appconfiguration.config.implementation.http.policy.TracingInfo;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationKeyValueSelector;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationProperties;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationProviderProperties;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationStoreMonitoring;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationStoreTrigger;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.ConfigStore;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.FeatureFlagStore;

import reactor.core.publisher.Flux;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class AppConfigurationPropertySourceFactoryTest {

    private static final String PROFILE_NAME_1 = "dev";

    private static final String PROFILE_NAME_2 = "prod";

    private static final String KEY_FILTER = "/foo/";

    @Mock
    private AppConfigurationReplicaClientFactory clientFactoryMock;

    @Mock
    private AppConfigurationKeyVaultClientFactory keyVaultClientFactory;

    @Mock
    private AppConfigurationReplicaClient replicaClientMock;

    @Mock
    private FeatureFlagStore featureFlagStoreMock;

    @Mock
    private PagedFlux<ConfigurationSetting> settingsMock;

    @Mock
    private Iterable<PagedResponse<ConfigurationSetting>> iterableMock;

    @Mock
    private Iterator<PagedResponse<ConfigurationSetting>> iteratorMock;

    @Mock
    private Flux<PagedResponse<ConfigurationSetting>> pageMock;

    @Mock
    private PagedResponse<ConfigurationSetting> pagedMock;

    @Mock
    private ConfigStore configStoreMock;

    @Mock
    private ConfigStore configStoreMockError;

    @Mock
    private AppConfigurationProviderProperties appPropertiesMock;

    private AppConfigurationProperties properties;

    @Mock
    private List<ConfigurationSetting> watchKeyListMock;

    @Mock
    private ReplicaLookUp replicaLookUpMock;

    private AppConfigurationPropertySourceFactory propertySourceFactory;

    private AppConfigurationProviderProperties appProperties;

    private List<ConfigStore> stores;

    private MockitoSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        session = Mockito.mockitoSession()
            .initMocks(this).strictness(org.mockito.quality.Strictness.STRICT_STUBS).startMocking();

        properties = new AppConfigurationProperties();
        properties.setEnabled(true);
        properties.setRefreshInterval(null);

        when(configStoreMock.getEndpoint()).thenReturn(TEST_STORE_NAME);

        stores = new ArrayList<>();
        stores.add(configStoreMock);

        when(watchKeyListMock.iterator()).thenReturn(Collections.emptyIterator());

        when(clientFactoryMock.getAvailableClients(Mockito.anyString(), Mockito.eq(true)))
            .thenReturn(Arrays.asList(replicaClientMock));
        when(replicaClientMock.listSettings(Mockito.any())).thenReturn(watchKeyListMock)
            .thenReturn(watchKeyListMock).thenReturn(watchKeyListMock);

        appProperties = new AppConfigurationProviderProperties();
        appProperties.setVersion("1.0");
        appProperties.setMaxRetries(12);
        appProperties.setMaxRetryTime(0);
        appProperties.setDefaultMaxBackoff((long) 600);
        appProperties.setDefaultMinBackoff((long) 30);

        AppConfigurationKeyValueSelector selectedKeys = new AppConfigurationKeyValueSelector().setKeyFilter(KEY_FILTER);
        List<AppConfigurationKeyValueSelector> selects = new ArrayList<>();
        selects.add(selectedKeys);
        when(configStoreMock.getSelects()).thenReturn(selects);
    }

    @AfterEach
    public void cleanup() throws Exception {
        MockitoAnnotations.openMocks(this).close();
        AppConfigurationPropertySourceLocator.STARTUP.set(true);
        session.finishMocking();
    }
    
    public void setupMonitoring() {
        AppConfigurationStoreMonitoring monitoring = new AppConfigurationStoreMonitoring();
        monitoring.setEnabled(false);
        AppConfigurationStoreTrigger trigger = new AppConfigurationStoreTrigger();
        trigger.setKey("sentinel");
        trigger.setKey("test");
        ArrayList<AppConfigurationStoreTrigger> triggers = new ArrayList<>();
        triggers.add(trigger);
        monitoring.setTriggers(triggers);
        when(configStoreMock.getMonitoring()).thenReturn(monitoring);
    }

    @Test
    public void compositeSourceIsCreated() throws InterruptedException {
        setupMonitoring();
        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStoreMock);
        
        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock, keyVaultClientFactory, null);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            List<AppConfigurationPropertySource> sources = propertySourceFactory.build(configStoreMock, List.of(), new StateHolder(), true);

            String[] expectedSourceNames = new String[] {
                KEY_FILTER + "store1/\0"
            };
            assertEquals(expectedSourceNames.length, sources.size());
            assertArrayEquals((Object[]) expectedSourceNames, sources.stream().map(PropertySource::getName).toArray());
        }
    }

    @Test
    public void compositeSourceIsCreatedWithMonitoring() throws InterruptedException {
        String watchKey = "wk1";
        String watchValue = "0";
        String watchLabel = EMPTY_LABEL;
        AppConfigurationStoreMonitoring monitoring = new AppConfigurationStoreMonitoring();
        monitoring.setEnabled(true);
        List<AppConfigurationStoreTrigger> watchKeys = new ArrayList<>();
        AppConfigurationStoreTrigger trigger = new AppConfigurationStoreTrigger();
        trigger.setKey(watchKey);
        trigger.setLabel(watchLabel);
        watchKeys.add(trigger);
        monitoring.setTriggers(watchKeys);

        when(configStoreMock.getMonitoring()).thenReturn(monitoring);
        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStoreMock);
        when(replicaClientMock.getWatchKey(Mockito.eq(watchKey), Mockito.anyString()))
            .thenReturn(TestUtils.createItem("", watchKey, watchValue, watchLabel, ""));

        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock,
            keyVaultClientFactory, null);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            List<AppConfigurationPropertySource> sources = propertySourceFactory.build(configStoreMock, List.of(),
                new StateHolder(), true);

            String[] expectedSourceNames = new String[] {
                KEY_FILTER + "store1/\0"
            };
            assertEquals(expectedSourceNames.length, sources.size());
            assertArrayEquals((Object[]) expectedSourceNames, sources.stream().map(PropertySource::getName).toArray());
            verify(replicaClientMock, times(1)).getWatchKey(Mockito.eq(watchKey), Mockito.anyString());
        }
    }

    @Test
    public void compositeSourceIsCreatedWithMonitoringNoWatchKey() throws InterruptedException {
        String watchKey = "wk1";
        String watchLabel = EMPTY_LABEL;
        AppConfigurationStoreMonitoring monitoring = new AppConfigurationStoreMonitoring();
        monitoring.setEnabled(true);
        List<AppConfigurationStoreTrigger> watchKeys = new ArrayList<>();
        AppConfigurationStoreTrigger trigger = new AppConfigurationStoreTrigger();
        trigger.setKey(watchKey);
        trigger.setLabel(watchLabel);
        watchKeys.add(trigger);
        monitoring.setTriggers(watchKeys);

        when(configStoreMock.getMonitoring()).thenReturn(monitoring);
        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStoreMock);
        when(replicaClientMock.getWatchKey(Mockito.eq(watchKey), Mockito.anyString()))
            .thenReturn(null);

        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock,
            keyVaultClientFactory, null);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            List<AppConfigurationPropertySource> sources = propertySourceFactory.build(configStoreMock, List.of(),
                new StateHolder(), true);

            String[] expectedSourceNames = new String[] {
                KEY_FILTER + "store1/\0"
            };
            assertEquals(expectedSourceNames.length, sources.size());
            assertArrayEquals((Object[]) expectedSourceNames, sources.stream().map(PropertySource::getName).toArray());
            verify(replicaClientMock, times(1)).getWatchKey(Mockito.eq(watchKey), Mockito.anyString());
        }
    }

    @Test
    public void devSourceIsCreated() throws InterruptedException {
        setupMonitoring();
        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStoreMock);

        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock, keyVaultClientFactory, null);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            List<AppConfigurationPropertySource> sources = propertySourceFactory.build(configStoreMock, List.of(PROFILE_NAME_1), new StateHolder(), true);

            String[] expectedSourceNames = new String[] {
                KEY_FILTER + "store1/dev"
            };
            assertEquals(expectedSourceNames.length, sources.size());
            assertArrayEquals((Object[]) expectedSourceNames, sources.stream().map(PropertySource::getName).toArray());
        }
    }

    @Test
    public void multiSourceIsCreated() throws InterruptedException {
        setupMonitoring();
        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStoreMock);

        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock, keyVaultClientFactory, null);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            List<String> profiles = new ArrayList<>();
            profiles.addAll(List.of(PROFILE_NAME_1, PROFILE_NAME_2));
            List<AppConfigurationPropertySource> sources = propertySourceFactory.build(configStoreMock, profiles, new StateHolder(), true);

            String[] expectedSourceNames = new String[] {
                KEY_FILTER + "store1/prod,dev"
            };
            assertEquals(expectedSourceNames.length, sources.size());
            assertArrayEquals((Object[]) expectedSourceNames, sources.stream().map(PropertySource::getName).toArray());
        }
    }

    @Test
    public void storeCreatedWithFeatureFlags() throws InterruptedException {
        setupMonitoring();
        FeatureFlagStore featureFlagStore = new FeatureFlagStore();
        featureFlagStore.setEnabled(true);
        featureFlagStore.validateAndInit();

        List<ConfigurationSetting> featureList = new ArrayList<>();
        FeatureFlagConfigurationSetting featureFlag = new FeatureFlagConfigurationSetting("Alpha", false);
        featureFlag.setValue("");
        featureList.add(featureFlag);

        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStore);
        when(replicaClientMock.listSettings(Mockito.any())).thenReturn(featureList);

        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock,
            keyVaultClientFactory, null);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            List<AppConfigurationPropertySource> sources = propertySourceFactory.build(configStoreMock, List.of(),
                new StateHolder(), true);

            // Application name: foo and active profile: dev,prod, should construct below
            // composite Property Source:
            // [/foo_prod/, /foo_dev/, /foo/, /application_prod/, /application_dev/,
            // /application/]
            String[] expectedSourceNames = new String[] {
                "FM_store1/ ",
                KEY_FILTER + "store1/\0"
            };
            assertEquals(expectedSourceNames.length, sources.size());
            assertArrayEquals((Object[]) expectedSourceNames, sources.stream().map(PropertySource::getName).toArray());
        }
    }

    @Test
    public void storeCreatedWithFeatureFlagsRequireAll() throws InterruptedException {
        setupMonitoring();
        FeatureFlagStore featureFlagStore = new FeatureFlagStore();
        featureFlagStore.setEnabled(true);
        featureFlagStore.validateAndInit();

        List<ConfigurationSetting> featureList = new ArrayList<>();
        FeatureFlagConfigurationSetting featureFlag = new FeatureFlagConfigurationSetting("Alpha", true);
        featureFlag.setValue(
            "{\"id\":null,\"description\":null,\"display_name\":null,\"enabled\":true,\"conditions\":{\"requirement_type\":\"All\", \"client_filters\":[{\"name\":\"AlwaysOn\",\"parameters\":{}}]}}");
        featureList.add(featureFlag);

        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStore);
        when(replicaClientMock.listSettings(Mockito.any())).thenReturn(featureList);
        when(replicaClientMock.getTracingInfo()).thenReturn(new TracingInfo(false, false, 0, null));

        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock,
            keyVaultClientFactory, null);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            List<AppConfigurationPropertySource> sources = propertySourceFactory.build(configStoreMock, List.of(),
                new StateHolder(), true);
            // Application name: foo and active profile: dev,prod, should construct below
            // composite Property Source:
            // [/foo_prod/, /foo_dev/, /foo/, /application_prod/, /application_dev/,
            // /application/]
            String[] expectedSourceNames = new String[] {
                "FM_store1/ ",
                KEY_FILTER + "store1/\0"
            };
            assertEquals(expectedSourceNames.length, sources.size());
            Object[] propertySources = sources.stream().map(c -> c.getProperty("feature-management.Alpha")).toArray();
            Feature alpha = (Feature) propertySources[0];
            assertEquals("All", alpha.getRequirementType());
            assertArrayEquals((Object[]) expectedSourceNames, sources.stream().map(PropertySource::getName).toArray());

        }
    }

    @Test
    public void storeCreatedWithFeatureFlagsWithMonitoring() throws InterruptedException {
        AppConfigurationStoreMonitoring monitoring = new AppConfigurationStoreMonitoring();
        monitoring.setEnabled(true);
        FeatureFlagStore featureFlagStore = new FeatureFlagStore();
        featureFlagStore.setEnabled(true);
        featureFlagStore.validateAndInit();

        List<ConfigurationSetting> featureList = new ArrayList<>();
        FeatureFlagConfigurationSetting featureFlag = new FeatureFlagConfigurationSetting("Alpha", false);
        featureFlag.setValue("");
        featureList.add(featureFlag);

        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStore);
        when(configStoreMock.getMonitoring()).thenReturn(monitoring);
        when(replicaClientMock.listSettings(Mockito.any())).thenReturn(featureList);

        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock,
            keyVaultClientFactory, null);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            List<AppConfigurationPropertySource> sources = propertySourceFactory.build(configStoreMock, List.of(),
                new StateHolder(), true);
            // Application name: foo and active profile: dev,prod, should construct below
            // composite Property Source:
            // [/foo_prod/, /foo_dev/, /foo/, /application_prod/, /application_dev/,
            // /application/]
            String[] expectedSourceNames = new String[] {
                "FM_store1/ ",
                KEY_FILTER + "store1/\0"
            };
            assertEquals(expectedSourceNames.length, sources.size());
            assertArrayEquals((Object[]) expectedSourceNames, sources.stream().map(PropertySource::getName).toArray());
        }
    }

    @Test
    public void watchedKeyCheck() throws InterruptedException {
        setupMonitoring();
        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStoreMock);

        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock, keyVaultClientFactory, null);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            List<AppConfigurationPropertySource> sources = propertySourceFactory.build(configStoreMock, List.of(), new StateHolder(), true);
            // Application name: foo and active profile: dev,prod, should construct below
            // composite Property Source:
            // [/foo_prod/, /foo_dev/, /foo/, /application_prod/, /application_dev/,
            // /application/]
            String[] expectedSourceNames = new String[] {
                KEY_FILTER + "store1/\0"
            };
            assertEquals(expectedSourceNames.length, sources.size());
            assertArrayEquals((Object[]) expectedSourceNames, sources.stream().map(PropertySource::getName).toArray());
        }
    }

    @Test
    public void defaultFailFastThrowException() {
        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStoreMock);
        when(configStoreMock.isFailFast()).thenReturn(true);

        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock,
            keyVaultClientFactory, null);

        when(clientFactoryMock.getAvailableClients(Mockito.anyString())).thenReturn(Arrays.asList(replicaClientMock));
        when(replicaClientMock.getWatchKey(Mockito.any(), Mockito.anyString())).thenThrow(new RuntimeException());
        RuntimeException e = assertThrows(RuntimeException.class,
            () -> propertySourceFactory.build(configStoreMock, null, null, true));
        assertEquals("Failed to generate property sources for " + TEST_STORE_NAME, e.getMessage());
        verify(configStoreMock, times(1)).isFailFast();
    }

    @Test
    public void refreshThrowException() throws IllegalArgumentException {
        when(configStoreMock.getFeatureFlags()).thenReturn(featureFlagStoreMock);
        AppConfigurationPropertySourceLocator.STARTUP.set(false);

        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock, keyVaultClientFactory, null);

        when(replicaClientMock.listSettings(any())).thenThrow(new RuntimeException());

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.getLoadState(Mockito.anyString())).thenReturn(true);
            RuntimeException e = assertThrows(RuntimeException.class, () -> propertySourceFactory.build(configStoreMock, List.of(), new StateHolder(), false));
            assertEquals("Failed to generate property sources for store1", e.getMessage());
        }
    }

    @Test
    public void notFailFastShouldPass() throws InterruptedException {
        when(configStoreMock.isFailFast()).thenReturn(false);
        propertySourceFactory = new AppConfigurationPropertySourceFactory(appProperties, clientFactoryMock, keyVaultClientFactory, null);

        when(configStoreMock.isFailFast()).thenReturn(false);
        when(configStoreMock.getEndpoint()).thenReturn(TEST_STORE_NAME);

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            List<AppConfigurationPropertySource> sources = propertySourceFactory.build(configStoreMock, List.of(), new StateHolder(), true);
            assertNull(sources);

            // Once a store fails it should stop attempting to load
            verify(configStoreMock, times(3)).isFailFast();
        }
    }
}
