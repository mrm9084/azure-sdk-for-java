// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.feature.management.implementation.targeting;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Audience of a TargetingFilter rollout
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Audience {


    /**
     * @return the users
     */
    public List<String> getUsers();

    /**
     * @return the groups
     */
    public List<GroupRollout> getGroups();

    /**
     * @return the defaultRolloutPercentage
     */
    public double getDefaultRolloutPercentage();

    /**
     * @return the exclusion
     */
    public Exclusion getExclusion();
    
    
    public void setExclusion(Exclusion exclusion);

}
