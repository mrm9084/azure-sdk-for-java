// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.feature.management.implementation.models;

import static com.azure.spring.cloud.feature.management.implementation.FeatureManagementConstants.DEFAULT_REQUIREMENT_TYPE;

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

    @JsonProperty("conditions")
    private Conditions conditions = new Conditions();

    @JsonProperty("requirement-type")
    private String requirementType = DEFAULT_REQUIREMENT_TYPE;

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public Feature setKey(String key) {
        this.key = key;
        return this;
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
    public Feature setEvaluate(Boolean evaluate) {
        this.evaluate = evaluate;
        return this;
    }

    /**
     * @return the conditions
     */
    public Conditions getConditions() {
        return conditions;
    }

    /**
     * @param conditions the conditions to set
     */
    public Feature setConditions(Conditions conditions) {
        this.conditions = conditions;
        return this;
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
    public Feature setRequirementType(String requirementType) {
        this.requirementType = requirementType;
        return this;
    }

}
