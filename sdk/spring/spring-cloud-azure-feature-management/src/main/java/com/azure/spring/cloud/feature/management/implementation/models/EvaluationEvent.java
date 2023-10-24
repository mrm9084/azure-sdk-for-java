// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.feature.management.implementation.models;

public class EvaluationEvent {

    private Feature featureDefinition;

    private Boolean isEnabled;

    public EvaluationEvent(Feature featureDefinition, Boolean isEnabled) {
        this.featureDefinition = featureDefinition;
        this.isEnabled = isEnabled;
    }

    /**
     * @return the featureDefinition
     */
    public Feature getFeatureDefinition() {
        return featureDefinition;
    }

    /**
     * @param featureDefinition the featureDefinition to set
     */
    public void setFeatureDefinition(Feature featureDefinition) {
        this.featureDefinition = featureDefinition;
    }

    /**
     * @return the isEnabled
     */
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    /**
     * @param isEnabled the isEnabled to set
     */
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

}
