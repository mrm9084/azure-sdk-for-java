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
@ActiveProfiles("server")
public class ServerSideFeatureManagementPropertiesTest {
    @Autowired
    private FeatureManagementProperties serverSideProperties;

    @Test
    void onOffMapTest() {
        //assertTrue(serverSideProperties.getOnOff().get("Gamma"));
    }

    @Test
    void featureManagementTest() {
        final Feature alphaFeatureItem = serverSideProperties.getFeature("Alpha");
        assertEquals(alphaFeatureItem.getKey(), "Alpha");
        assertEquals(alphaFeatureItem.getConditions().getClientFilters().size(), 1);
        assertEquals(alphaFeatureItem.getConditions().getClientFilters().get(0).getName(), "Microsoft.Random");

        final Feature betaFeatureItem = serverSideProperties.getFeature("Beta");
        assertEquals(betaFeatureItem.getKey(), "Beta");
        assertEquals(betaFeatureItem.getConditions().getClientFilters().size(), 1);
        assertEquals(betaFeatureItem.getConditions().getClientFilters().get(0).getName(), "Microsoft.TimeWindowFilter");

        final Feature deltaFeatureItem = serverSideProperties.getFeature("Delta");
        assertEquals(deltaFeatureItem.getKey(), "Delta");
        assertEquals(deltaFeatureItem.getConditions().getClientFilters().size(), 0);
    }

}
