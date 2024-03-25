// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.feature.management.implementation;

import java.util.HashMap;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.azure.spring.cloud.feature.management.implementation.models.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration Properties for Feature Management. Processes the configurations to be usable by Feature Management.
 */
@ConfigurationProperties(prefix = "feature-management")
public class FeatureManagementProperties {

    @JsonProperty("feature-flags")
    private List<Feature> featureFlags;

    private HashMap<String, Feature> featureMap;

    /**
     * @return the featureFlags
     */
    public List<Feature> getFeatureFlags() {
        return featureFlags;
    }

    /**
     * @param featureFlags the featureFlags to set
     */
    public void setFeatureFlags(List<Feature> featureFlags) {
        this.featureFlags = featureFlags;
        featureMap = new HashMap<>();
        featureFlags.stream().forEach(feature -> featureMap.put(feature.getKey(), feature));
    }

    public Feature getFeature(String name) {
        return featureMap.getOrDefault(name, null);
    }

    /**
     * @return the featureMap
     */
    public HashMap<String, Feature> getFeatureMap() {
        return featureMap;
    }

    /**
     * @param featureMap the featureMap to set
     */
    public void setFeatureMap(HashMap<String, Feature> featureMap) {
        this.featureMap = featureMap;
    }

}
