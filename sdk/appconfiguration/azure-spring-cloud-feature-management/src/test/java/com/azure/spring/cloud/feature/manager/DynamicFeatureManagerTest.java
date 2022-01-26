// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.feature.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.azure.spring.cloud.feature.manager.entities.DynamicFeature;
import com.azure.spring.cloud.feature.manager.entities.Feature;
import com.azure.spring.cloud.feature.manager.entities.FeatureDefinition;
import com.azure.spring.cloud.feature.manager.entities.FeatureFilterEvaluationContext;
import com.azure.spring.cloud.feature.manager.entities.FeatureVariant;
import com.azure.spring.cloud.feature.manager.entities.IFeatureVariantAssigner;
import com.azure.spring.cloud.feature.manager.testobjects.BasicObject;

import reactor.core.publisher.Mono;

/**
 * Unit tests for FeatureManager.
 */
@SpringBootTest(classes = { TestConfiguration.class, SpringBootTest.class })
public class DynamicFeatureManagerTest {

    private static final String FEATURE_KEY = "TestFeature";

    private static final String FILTER_NAME = "Filter1";

    private static final String PARAM_1_NAME = "param1";

    private static final String PARAM_1_VALUE = "testParam";

    private static final String USERS = "users";

    private static final String GROUPS = "groups";

    private static final String DEFAULT_ROLLOUT_PERCENTAGE = "defaultRolloutPercentage";

    private static final LinkedHashMap<String, Object> EMPTY_MAP = new LinkedHashMap<>();

    @InjectMocks
    private FeatureManager featureManager;

    @Mock
    private ApplicationContext context;

    @Mock
    private FeatureManagementConfigProperties properties;

    @Mock
    private FeatureVariantProperties variantProperties;

    @Mock
    private MockFilter filterMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(properties.isFailFast()).thenReturn(true);
    }

    @AfterEach
    public void cleanup() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    /**
     * Tests the conversion that takes place when data comes from EnumerablePropertySource.
     */
    @Test
    public void loadFeatureManagerWithLinkedHashSet() {
        Feature f = new Feature();
        f.setKey(FEATURE_KEY);

        LinkedHashMap<String, Object> testMap = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> testFeature = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> enabledFor = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> ffec = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
        ffec.put("name", FILTER_NAME);
        parameters.put(PARAM_1_NAME, PARAM_1_VALUE);
        ffec.put("parameters", parameters);
        enabledFor.put("0", ffec);
        testFeature.put("enabled-for", enabledFor);
        testMap.put(f.getKey(), testFeature);

        // featureManager.putAll(testMap);
        assertNotNull(featureManager);
        assertNotNull(featureManager.getFeatureManagement());
        assertEquals(1, featureManager.getFeatureManagement().size());
        assertNotNull(featureManager.getFeatureManagement().get(FEATURE_KEY));
        Feature feature = featureManager.getFeatureManagement().get(FEATURE_KEY);
        assertEquals(FEATURE_KEY, feature.getKey());
        assertEquals(1, feature.getEnabledFor().size());
        FeatureFilterEvaluationContext zeroth = feature.getEnabledFor().get(0);
        assertEquals(FILTER_NAME, zeroth.getName());
        assertEquals(1, zeroth.getParameters().size());
        assertEquals(PARAM_1_VALUE, zeroth.getParameters().get(PARAM_1_NAME));
    }

    @Test
    public void getVariantAsyncDefaultBasic() {
        String testString = "Basic Object";
        when(variantProperties.get("testVariantReference")).thenReturn(testString);
        DynamicFeature dynamicFeature = new DynamicFeature();
        dynamicFeature.setAssigner("Test.Assigner");

        Map<String, FeatureVariant> variants = new LinkedHashMap<>();

        variants.put("0", createFeatureVariant("testVariant", EMPTY_MAP, EMPTY_MAP, 100));
        variants.get("0").setDefault(true);
        dynamicFeature.setVariants(variants);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("testVariant", dynamicFeature);

        // featureManager.putAll(params);

        assertEquals(testString, featureManager.getVariantAsync("testVariant", String.class).block());
    }

    @Test
    public void getVariantAsyncDefaultMultiPart() {
        String testString = "Basic Object";
        String variantName = "testVariant";

        HashMap<String, Object> config = new HashMap<>();
        config.put("LevelTwo", testString);

        when(variantProperties.get(variantName + "Reference")).thenReturn(config);

        DynamicFeature dynamicFeature = new DynamicFeature();
        dynamicFeature.setAssigner("Test.Assigner");

        Map<String, FeatureVariant> variants = new LinkedHashMap<>();

        variants.put("0", createFeatureVariant(variantName, ":LevelTwo", EMPTY_MAP, EMPTY_MAP, 100));
        variants.get("0").setDefault(true);
        dynamicFeature.setVariants(variants);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(variantName, dynamicFeature);

        // featureManager.putAll(params);

        assertEquals(testString, featureManager.getVariantAsync(variantName, String.class).block());
    }

    @Test
    public void getVariantNoDefault() {
        DynamicFeature dynamicFeature = new DynamicFeature();
        dynamicFeature.setAssigner("Test.Assigner");

        Map<String, FeatureVariant> variants = new LinkedHashMap<>();

        variants.put("0", createFeatureVariant("testVariant", EMPTY_MAP, EMPTY_MAP, 100));

        dynamicFeature.setVariants(variants);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("testVariant", dynamicFeature);

        // featureManager.putAll(params);

        FeatureManagementException e = assertThrows(FeatureManagementException.class,
            () -> featureManager.getVariantAsync("testVariant", Object.class).block());
        assertEquals("A default variant cannot be found for the feature testVariant", e.getMessage());
    }

    @Test
    public void getVariantAsyncNonDefault() {
        String testString = "Basic Object";

        FeatureVariant variant = createFeatureVariant("testVariant2", EMPTY_MAP, EMPTY_MAP, 100);

        when(variantProperties.get("testVariant2Reference")).thenReturn(testString);

        when(context.getBean(Mockito.matches("Test.Assigner"))).thenReturn(filterMock);
        when(filterMock.assignVariantAsync(Mockito.any())).thenReturn(Mono.just(variant));

        DynamicFeature dynamicFeature = new DynamicFeature();
        dynamicFeature.setAssigner("Test.Assigner");

        Map<String, FeatureVariant> variants = new LinkedHashMap<>();

        variants.put("0", createFeatureVariant("testVariant", EMPTY_MAP, EMPTY_MAP, 0));
        variants.get("0").setDefault(true);
        variants.put("1", variant);
        dynamicFeature.setVariants(variants);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("testVariant", dynamicFeature);

        // featureManager.putAll(params);

        assertEquals(testString, featureManager.getVariantAsync("testVariant", String.class).block());
    }

    @Test
    public void getVariantAsyncComplexObject() {
        String testString = "Basic Object";
        BasicObject testObject = new BasicObject();
        testObject.setTestValue(testString);

        FeatureVariant variant = createFeatureVariant("testVariant2", EMPTY_MAP, EMPTY_MAP, 100);

        when(variantProperties.get("testVariant2Reference")).thenReturn(testObject);

        when(context.getBean(Mockito.matches("Test.Assigner"))).thenReturn(filterMock);
        when(filterMock.assignVariantAsync(Mockito.any())).thenReturn(Mono.just(variant));

        DynamicFeature dynamicFeature = new DynamicFeature();
        dynamicFeature.setAssigner("Test.Assigner");

        Map<String, FeatureVariant> variants = new LinkedHashMap<>();

        variants.put("0", createFeatureVariant("testVariant", EMPTY_MAP, EMPTY_MAP, 0));
        variants.get("0").setDefault(true);
        variants.put("1", variant);
        dynamicFeature.setVariants(variants);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("testVariant", dynamicFeature);

        // featureManager.putAll(params);

        assertEquals(testString,
            featureManager.getVariantAsync("testVariant", BasicObject.class).block().getTestValue());
    }

    class MockFilter implements FeatureFilter, IFeatureVariantAssigner {

        @Override
        public Mono<FeatureVariant> assignVariantAsync(FeatureDefinition featureDefinition) {
            return null;
        }

        @Override
        public boolean evaluate(FeatureFilterEvaluationContext context) {
            return false;
        }

    }

    private FeatureVariant createFeatureVariant(String variantName, LinkedHashMap<String, Object> users,
        LinkedHashMap<String, Object> groups, int defautPercentage) {
        return createFeatureVariant(variantName, "", users, groups, defautPercentage);
    }

    private FeatureVariant createFeatureVariant(String variantName, String additionalRerence,
        LinkedHashMap<String, Object> users,
        LinkedHashMap<String, Object> groups, int defautPercentage) {
        FeatureVariant variant = new FeatureVariant();
        variant.setName(variantName);
        variant.setDefault(false);
        variant.setConfigurationReference(variantName + "Reference" + additionalRerence);

        LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();

        parameters.put(USERS, users);
        parameters.put(GROUPS, groups);
        parameters.put(DEFAULT_ROLLOUT_PERCENTAGE, defautPercentage);

        variant.setAssignmentParameters(parameters);

        return variant;
    }

}
