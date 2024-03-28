// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.appconfiguration.config.implementation;

import java.util.ArrayList;
import java.util.List;

import com.azure.core.http.MatchConditions;
import com.azure.data.appconfiguration.models.SettingSelector;

import reactor.core.publisher.Flux;

public class FeatureFlagWatch {

    private SettingSelector settingSelector;

    private List<MatchConditions> conditions;

    public FeatureFlagWatch(SettingSelector settingSelector, Flux<MatchConditions> conditions) {
        this.settingSelector = settingSelector;
        this.conditions = new ArrayList<>();
        conditions.subscribe(condition -> this.conditions.add(condition));
    }

    /**
     * @return the settingSelector
     */
    public SettingSelector getSettingSelector() {
        return settingSelector;
    }

    /**
     * @param settingSelector the settingSelector to set
     */
    public void setSettingSelector(SettingSelector settingSelector) {
        this.settingSelector = settingSelector;
    }

    /**
     * @return the conditions
     */
    public List<MatchConditions> getConditions() {
        return conditions;
    }

    /**
     * @param conditions the conditions to set
     */
    public void setConditions(List<MatchConditions> conditions) {
        this.conditions = conditions;
    }

}
