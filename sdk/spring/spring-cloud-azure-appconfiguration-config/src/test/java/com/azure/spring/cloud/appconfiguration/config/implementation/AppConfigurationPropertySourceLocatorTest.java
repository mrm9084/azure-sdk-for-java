// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.appconfiguration.config.implementation;

import static com.azure.spring.cloud.appconfiguration.config.implementation.TestConstants.TEST_CONN_STRING;
import static com.azure.spring.cloud.appconfiguration.config.implementation.TestConstants.TEST_CONN_STRING_2;
import static com.azure.spring.cloud.appconfiguration.config.implementation.TestConstants.TEST_STORE_NAME_1;
import static com.azure.spring.cloud.appconfiguration.config.implementation.TestConstants.TEST_STORE_NAME_2;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import com.azure.spring.cloud.appconfiguration.config.implementation.autofailover.ReplicaLookUp;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationKeyValueSelector;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationProperties;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationProviderProperties;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationStoreMonitoring;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.AppConfigurationStoreTrigger;
import com.azure.spring.cloud.appconfiguration.config.implementation.properties.ConfigStore;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class AppConfigurationPropertySourceLocatorTest {

    private static final String KEY_FILTER = "/foo/";

    @Mock
    private ConfigurableEnvironment emptyEnvironment;

    @Mock
    private AppConfigurationKeyVaultClientFactory keyVaultClientFactory;

    @Mock
    private ConfigStore configStoreMock;

    @Mock
    private ConfigStore configStoreMockError;

    @Mock
    private AppConfigurationProviderProperties appPropertiesMock;

    private AppConfigurationProperties properties;

    @Mock
    private ReplicaLookUp replicaLookUpMock;

    @Mock
    private AppConfigurationPropertySourceFactory propertySourceFactoryMock;

    private AppConfigurationPropertySourceLocator locator;

    private AppConfigurationProviderProperties appProperties;

    private List<ConfigStore> stores;

    private MockitoSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        session = Mockito.mockitoSession()
            .initMocks(this).strictness(org.mockito.quality.Strictness.STRICT_STUBS).startMocking();

        MutablePropertySources sources = new MutablePropertySources();

        sources.addFirst(new PropertySource<String>("refreshArgs") {

            @Override
            public Object getProperty(String name) {
                return null;
            }
        });

        properties = new AppConfigurationProperties();
        properties.setEnabled(true);
        properties.setRefreshInterval(null);

        stores = List.of(configStoreMock);

        AppConfigurationStoreMonitoring monitoring = new AppConfigurationStoreMonitoring();
        monitoring.setEnabled(false);
        AppConfigurationStoreTrigger trigger = new AppConfigurationStoreTrigger();
        trigger.setKey("sentinel");
        trigger.setKey("test");
        ArrayList<AppConfigurationStoreTrigger> triggers = new ArrayList<>();
        triggers.add(trigger);
        monitoring.setTriggers(triggers);

        appProperties = new AppConfigurationProviderProperties();
        appProperties.setVersion("1.0");
        appProperties.setMaxRetries(12);
        appProperties.setMaxRetryTime(0);
        appProperties.setDefaultMaxBackoff((long) 600);
        appProperties.setDefaultMinBackoff((long) 30);

        AppConfigurationKeyValueSelector selectedKeys = new AppConfigurationKeyValueSelector().setKeyFilter(KEY_FILTER);
        List<AppConfigurationKeyValueSelector> selects = new ArrayList<>();
        selects.add(selectedKeys);
    }

    @AfterEach
    public void cleanup() throws Exception {
        MockitoAnnotations.openMocks(this).close();
        AppConfigurationPropertySourceLocator.STARTUP.set(true);
        session.finishMocking();
    }

    @Test
    public void multiplePropertySourcesExistForMultiStores() throws InterruptedException {
        properties = new AppConfigurationProperties();
        when(emptyEnvironment.getActiveProfiles()).thenReturn(new String[] {});
        TestUtils.addStore(properties, TEST_STORE_NAME_1, TEST_CONN_STRING, KEY_FILTER);
        TestUtils.addStore(properties, TEST_STORE_NAME_2, TEST_CONN_STRING_2, KEY_FILTER);

        MutablePropertySources mutableSources = new MutablePropertySources();
        mutableSources.addFirst(new PropertySource<String>("refreshArgs") {
            @Override
            public Object getProperty(String name) {
                return null;
            }
        });

        when(emptyEnvironment.getPropertySources()).thenReturn(mutableSources);
        when(propertySourceFactoryMock.build(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(List.of(new AppConfigurationApplicationSettingPropertySource("a", null, keyVaultClientFactory,
                KEY_FILTER, null))).thenReturn(List.of(new AppConfigurationApplicationSettingPropertySource("b", null, keyVaultClientFactory,
                    KEY_FILTER, null)));

        locator = new AppConfigurationPropertySourceLocator(appProperties,
            keyVaultClientFactory, null, properties.getStores(), replicaLookUpMock, propertySourceFactoryMock, Duration.ofSeconds(100));

        try (MockedStatic<StateHolder> stateHolderMock = Mockito.mockStatic(StateHolder.class)) {
            stateHolderMock.when(() -> StateHolder.updateState(Mockito.any())).thenReturn(null);
            PropertySource<?> source = locator.locate(emptyEnvironment);
            assertTrue(source instanceof CompositePropertySource);

            Collection<PropertySource<?>> sources = ((CompositePropertySource) source).getPropertySources();
            String[] expectedSourceNames = new String[] { "a", "b" };
            assertEquals(2, sources.size());
            assertArrayEquals((Object[]) expectedSourceNames, sources.stream().map(PropertySource::getName).toArray());
        }
    }

    @Test
    public void awaitOnError() throws InterruptedException {
        when(propertySourceFactoryMock.build(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(null);
        when(propertySourceFactoryMock.failedToGeneratePropertySource(Mockito.any(), Mockito.any(), Mockito.any(),
            Mockito.any())).thenThrow(new RuntimeException());
        ConfigStore configStore = new ConfigStore();

        ConfigurableEnvironment env = Mockito.mock(ConfigurableEnvironment.class);
        MutablePropertySources sources = new MutablePropertySources();

        sources.addFirst(new PropertySource<String>("refreshArgs") {

            @Override
            public Object getProperty(String name) {
                return null;
            }
        });

        when(env.getPropertySources()).thenReturn(sources);

        String[] array = {};
        when(env.getActiveProfiles()).thenReturn(array);
        AppConfigurationKeyValueSelector selectedKeys = new AppConfigurationKeyValueSelector()
            .setKeyFilter("/application/");

        configStore.setSelects(List.of(selectedKeys));
        configStore.setEnabled(true);
        configStore.setEndpoint("");

        locator = new AppConfigurationPropertySourceLocator(appPropertiesMock, keyVaultClientFactory,
            null, List.of(configStore), replicaLookUpMock, propertySourceFactoryMock, Duration.ofSeconds(100));

        assertThrows(RuntimeException.class, () -> locator.locate(env));
        verify(propertySourceFactoryMock, times(1)).failedToGeneratePropertySource(Mockito.any(), Mockito.any(),
            Mockito.any(), Mockito.any());
    }

    @Test
    public void storeDisabled() {
        when(emptyEnvironment.getPropertySources()).thenReturn(new MutablePropertySources());
        String[] profiles = {};
        when(emptyEnvironment.getActiveProfiles()).thenReturn(profiles);
        locator = new AppConfigurationPropertySourceLocator(appProperties, keyVaultClientFactory,
            null, stores, replicaLookUpMock, propertySourceFactoryMock, Duration.ofSeconds(100));

        PropertySource<?> source = locator.locate(emptyEnvironment);
        assertTrue(source instanceof CompositePropertySource);

        Collection<PropertySource<?>> sources = ((CompositePropertySource) source).getPropertySources();

        assertEquals(0, sources.size());
    }
}
