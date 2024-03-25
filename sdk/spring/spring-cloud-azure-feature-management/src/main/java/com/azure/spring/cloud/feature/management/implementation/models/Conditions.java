// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.cloud.feature.management.implementation.models;

import java.util.ArrayList;
import java.util.List;

import com.azure.spring.cloud.feature.management.models.FeatureFilterEvaluationContext;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conditions {

    private List<FeatureFilterEvaluationContext> clientFilters = new ArrayList<>();

    /**
     * @return the clientFilters
     */
    public List<FeatureFilterEvaluationContext> getClientFilters() {
        return clientFilters;
    }

    /**
     * @param clientFilters the clientFilters to set
     */
    public Conditions setClientFilters(List<FeatureFilterEvaluationContext> clientFilters) {
        this.clientFilters = clientFilters;
        return this;
    }

}