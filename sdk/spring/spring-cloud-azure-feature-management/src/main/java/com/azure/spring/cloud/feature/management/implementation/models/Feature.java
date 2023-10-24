// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.feature.management.implementation.models;

import static com.azure.spring.cloud.feature.management.implementation.FeatureManagementConstants.DEFAULT_REQUIREMENT_TYPE;

import java.util.HashMap;
import java.util.Map;

import com.azure.spring.cloud.feature.management.models.FeatureFilterEvaluationContext;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * App Configuration Feature defines the feature name and a Map of FeatureFilterEvaluationContexts.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Feature {

    @JsonProperty("key")
    private String key;

    @JsonProperty("evaluate")
    private Boolean evaluate = true;

    @JsonProperty("requirement-type")
    private String requirementType = DEFAULT_REQUIREMENT_TYPE;;

    @JsonProperty("enabled-for")
    private Map<Integer, FeatureFilterEvaluationContext> enabledFor;

    private Boolean telemetryEnabled = false;

    private Map<String, String> telemetryMetadata = new HashMap<>();

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the evaluate
     */
    public Boolean getEvaluate() {
        return evaluate;
    }

    /**
     * @param evaluate the evaluate to set
     */
    public void setEvaluate(Boolean evaluate) {
        this.evaluate = evaluate;
    }

    /**
     * @return the enabledFor
     */
    public Map<Integer, FeatureFilterEvaluationContext> getEnabledFor() {
        return enabledFor;
    }

    /**
     * @param enabledFor the enabledFor to set
     */
    public void setEnabledFor(Map<Integer, FeatureFilterEvaluationContext> enabledFor) {
        this.enabledFor = enabledFor;
    }

    /**
     * @return the requirementType
     */
    public String getRequirementType() {
        return requirementType;
    }

    /**
     * @param requirementType the requirementType to set
     */
    public void setRequirementType(String requirementType) {
        this.requirementType = requirementType;
    }

    /**
     * @return the telemetryEnabled
     */
    public Boolean getTelemetryEnabled() {
        return telemetryEnabled;
    }

    /**
     * @param telemetryEnabled the telemetryEnabled to set
     */
    public void setTelemetryEnabled(Boolean telemetryEnabled) {
        this.telemetryEnabled = telemetryEnabled;
    }

    /**
     * @return the telemetryMetadata
     */
    public Map<String, String> getTelemetryMetadata() {
        return telemetryMetadata;
    }

    /**
     * @param telemetryMetadata the telemetryMetadata to set
     */
    public void setTelemetryMetadata(Map<String, String> telemetryMetadata) {
        this.telemetryMetadata = telemetryMetadata;
    }

}
