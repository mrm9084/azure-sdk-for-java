// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.cloud.feature.management;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.azure.spring.cloud.feature.management.implementation.FeatureManagementProperties;
import com.azure.spring.cloud.feature.management.implementation.models.Feature;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = FeatureManagementProperties.class)
@SpringBootTest(classes = { SpringBootTest.class })
@ActiveProfiles("client")
public class ClientSideFeatureManagementPropertiesTest {
    @Autowired
    private FeatureManagementProperties clientSideProperties;

    @Test
    void featureManagementTest() {
        
        final Feature alphaFeatureItem = clientSideProperties.getFeature("alpha");
        assertEquals(alphaFeatureItem.getKey(), "alpha");
        assertEquals(alphaFeatureItem.getConditions().getClientFilters().size(), 1);
        assertEquals(alphaFeatureItem.getConditions().getClientFilters().get(0).getName(), "randomFilter");

        final Feature betaFeatureItem = clientSideProperties.getFeature("beta");
        assertEquals(betaFeatureItem.getKey(), "beta");
        assertEquals(betaFeatureItem.getConditions().getClientFilters().size(), 1);
        assertEquals(betaFeatureItem.getConditions().getClientFilters().get(0).getName(), "timeWindowFilter");
    }
}
