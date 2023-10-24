// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.feature.management;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.spring.cloud.feature.management.implementation.FeatureManagementConfigProperties;
import com.azure.spring.cloud.feature.management.implementation.FeatureManagementProperties;
import com.azure.spring.cloud.feature.management.implementation.TelemetryPublisher;

/**
 * Configuration for setting up FeatureManager
 */
@Configuration
@EnableConfigurationProperties({ FeatureManagementConfigProperties.class, FeatureManagementProperties.class })
class FeatureManagementConfiguration {
    
    @Autowired
    private transient ApplicationContext appContext;

    /**
     * Creates Feature Manager
     * 
     * @param context ApplicationContext
     * @param featureManagementConfigurations Configuration Properties for Feature Flags
     * @param properties Feature Management configuration properties
     * @return FeatureManager
     */
    @Bean
    FeatureManager featureManager(ApplicationContext context,
        FeatureManagementProperties featureManagementConfigurations, FeatureManagementConfigProperties properties) {
        @SuppressWarnings("unchecked")
        List<TelemetryPublisher> telemetryPublishers = (List<TelemetryPublisher>) appContext.getBeanProvider(new ArrayList<TelemetryPublisher>().getClass());
        return new FeatureManager(context, featureManagementConfigurations, properties, telemetryPublishers);
    }
}
