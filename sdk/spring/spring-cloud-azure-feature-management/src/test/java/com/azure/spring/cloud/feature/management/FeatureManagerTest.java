// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.feature.management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.azure.spring.cloud.feature.management.filters.FeatureFilter;
import com.azure.spring.cloud.feature.management.implementation.FeatureManagementConfigProperties;
import com.azure.spring.cloud.feature.management.implementation.FeatureManagementProperties;
import com.azure.spring.cloud.feature.management.implementation.TestConfiguration;
import com.azure.spring.cloud.feature.management.implementation.models.Conditions;
import com.azure.spring.cloud.feature.management.implementation.models.Feature;
import com.azure.spring.cloud.feature.management.models.FeatureFilterEvaluationContext;
import com.azure.spring.cloud.feature.management.models.FilterNotFoundException;

/**
 * Unit tests for FeatureManager.
 */
@SpringBootTest(classes = { TestConfiguration.class, SpringBootTest.class })
public class FeatureManagerTest {

    private FeatureManager featureManager;

    @Mock
    private ApplicationContext context;

    @Mock
    private FeatureManagementConfigProperties properties;

    @Mock
    private FeatureManagementProperties featureManagementPropertiesMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(properties.isFailFast()).thenReturn(true);

        featureManager = new FeatureManager(context, featureManagementPropertiesMock, properties);
    }

    @AfterEach
    public void cleanup() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Test
    public void isEnabledFeatureNotFound() {
        assertFalse(featureManager.isEnabledAsync("Non Existed Feature").block());
        verify(featureManagementPropertiesMock, times(1)).getFeature(Mockito.anyString());
    }

    @Test
    public void isEnabledFeatureOff() {
        when(featureManagementPropertiesMock.getFeature(Mockito.any())).thenReturn(new Feature().setEvaluate(false));

        assertFalse(featureManager.isEnabledAsync("Off").block());
        verify(featureManagementPropertiesMock, times(1)).getFeature(Mockito.anyString());
    }

    @Test
    public void isEnabledOnBoolean() {
        Feature feature = new Feature();
        feature.setEvaluate(true);
        when(featureManagementPropertiesMock.getFeature(Mockito.anyString())).thenReturn(feature);

        assertTrue(featureManager.isEnabledAsync("On").block());
        verify(featureManagementPropertiesMock, times(1)).getFeature(Mockito.anyString());
    }

    @Test
    public void isEnabledFeatureHasNoFilters() {
        Feature noFilters = new Feature();
        noFilters.setKey("NoFilters");
        noFilters.setConditions(new Conditions().setClientFilters(List.of()));
        when(featureManagementPropertiesMock.getFeature(Mockito.anyString())).thenReturn(noFilters);

        assertTrue(featureManager.isEnabledAsync("NoFilters").block());
    }

    @Test
    public void isEnabledON() {
        Feature onFeature = new Feature();
        onFeature.setKey("On");
        FeatureFilterEvaluationContext alwaysOn = new FeatureFilterEvaluationContext();
        alwaysOn.setName("AlwaysOn");
        onFeature.setConditions(new Conditions().setClientFilters(List.of(alwaysOn)));
        when(featureManagementPropertiesMock.getFeature(Mockito.anyString())).thenReturn(onFeature);

        when(context.getBean(Mockito.matches("AlwaysOn"))).thenReturn(new AlwaysOnFilter());

        assertTrue(featureManager.isEnabledAsync("On").block());
    }

    @Test
    public void noFilter() {
        Feature onFeature = new Feature();
        onFeature.setKey("Off");
        FeatureFilterEvaluationContext alwaysOn = new FeatureFilterEvaluationContext();
        alwaysOn.setName("AlwaysOff");
        onFeature.setConditions(new Conditions().setClientFilters(List.of(alwaysOn)));
        when(featureManagementPropertiesMock.getFeature(Mockito.anyString())).thenReturn(onFeature);

        when(context.getBean(Mockito.matches("AlwaysOff"))).thenThrow(new NoSuchBeanDefinitionException(""));

        FilterNotFoundException e = assertThrows(FilterNotFoundException.class,
            () -> featureManager.isEnabledAsync("Off").block());
        assertThat(e).hasMessage("Fail fast is set and a Filter was unable to be found: AlwaysOff");
    }

    @Test
    public void allOn() {
        Feature onFeature = new Feature();
        onFeature.setKey("On");
        FeatureFilterEvaluationContext alwaysOn = new FeatureFilterEvaluationContext();
        alwaysOn.setName("AlwaysOn");
        onFeature.setConditions(new Conditions().setClientFilters(List.of(alwaysOn, alwaysOn)));
        onFeature.setRequirementType("All");
        when(featureManagementPropertiesMock.getFeature(Mockito.anyString())).thenReturn(onFeature);

        when(context.getBean(Mockito.matches("AlwaysOn"))).thenReturn(new AlwaysOnFilter())
            .thenReturn(new AlwaysOnFilter());

        assertTrue(featureManager.isEnabledAsync("On").block());
    }

    @Test
    public void oneOffAny() {
        Feature onFeature = new Feature();
        onFeature.setKey("On");
        FeatureFilterEvaluationContext alwaysOn = new FeatureFilterEvaluationContext();
        alwaysOn.setName("AlwaysOn");
        onFeature.setConditions(new Conditions().setClientFilters(List.of(alwaysOn, alwaysOn)));
        onFeature.setRequirementType("Any");
        when(featureManagementPropertiesMock.getFeature(Mockito.anyString())).thenReturn(onFeature);

        when(context.getBean(Mockito.matches("AlwaysOn"))).thenReturn(new AlwaysOnFilter())
            .thenReturn(new AlwaysOffFilter());

        assertTrue(featureManager.isEnabledAsync("On").block());
    }

    @Test
    public void oneOffAll() {
        Feature onFeature = new Feature();
        onFeature.setKey("On");
        FeatureFilterEvaluationContext alwaysOn = new FeatureFilterEvaluationContext();
        alwaysOn.setName("AlwaysOn");
        onFeature.setConditions(new Conditions().setClientFilters(List.of(alwaysOn, alwaysOn)));
        onFeature.setRequirementType("All");
        when(featureManagementPropertiesMock.getFeature(Mockito.anyString())).thenReturn(onFeature);

        when(context.getBean(Mockito.matches("AlwaysOn"))).thenReturn(new AlwaysOnFilter())
            .thenReturn(new AlwaysOffFilter());

        assertFalse(featureManager.isEnabledAsync("On").block());
    }

    class AlwaysOnFilter implements FeatureFilter {

        @Override
        public boolean evaluate(FeatureFilterEvaluationContext context) {
            return true;
        }

    }

    class AlwaysOffFilter implements FeatureFilter {

        @Override
        public boolean evaluate(FeatureFilterEvaluationContext context) {
            return false;
        }

    }

}
