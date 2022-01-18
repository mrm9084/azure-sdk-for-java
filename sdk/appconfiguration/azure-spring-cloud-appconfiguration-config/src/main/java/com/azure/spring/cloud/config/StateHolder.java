// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.config;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.azure.data.appconfiguration.models.ConfigurationSetting;

final class StateHolder {

    private static final int MAX_JITTER = 15;
    private static final String FEATURE_ENDPOINT = "_feature";
    private final Map<String, State> storeState = new ConcurrentHashMap<>();
    private final Map<String, Boolean> loadState = new ConcurrentHashMap<>();

    /**
     * @return the state
     */
    State getState(String endpoint) {
        return storeState.get(endpoint);
    }
    
    /**
     * @return the state
     */
    State getStateFeatureFlag(String endpoint) {
        return storeState.get(endpoint + FEATURE_ENDPOINT);
    }

    /**
     * @param endpoint the stores endpoint
     * @param watchKeys list of configuration watch keys that can trigger a refresh event
     * @param monitoring refresh configurations
     */
    void setState(String endpoint, List<ConfigurationSetting> watchKeys,
        Duration duration) {
        storeState.put(endpoint, new State(watchKeys, Math.toIntExact(duration.getSeconds()), endpoint));
    }
    
    /**
     * @param endpoint the stores endpoint
     * @param watchKeys list of configuration watch keys that can trigger a refresh event
     * @param monitoring refresh configurations
     */
    void setStateFeatureFlag(String endpoint, List<ConfigurationSetting> watchKeys,
        Duration duration) {
        setState(endpoint + FEATURE_ENDPOINT, watchKeys, duration);
    }

    /**
     * @param state previous state to base off
     * @param duration nextRefreshPeriod
     */
    void setState(State state, Duration duration) {
        storeState.put(state.getKey(), new State(state.getWatchKeys(), Math.toIntExact(duration.getSeconds()), state.getKey()));
    }

    void expireState(String endpoint) {
        String key = endpoint;
        State oldState = storeState.get(key);
        SecureRandom random = new SecureRandom();
        long wait = (long) (random.nextDouble() * MAX_JITTER);
        long timeLeft = (int) ((oldState.getNextRefreshCheck().getTime() - (new Date().getTime())) / 1000);
        if (wait < timeLeft) {
            storeState.put(key, new State(oldState.getWatchKeys(), (int) wait, oldState.getKey()));
        }
    }

    /**
     * @param name endpoint name
     * @return the loadState
     */
    boolean getLoadState(String name) {
        return loadState.getOrDefault(name,  false);
    }
    
    /**
     * @param name endpoint name
     * @return the loadState
     */
    boolean getLoadStateFeatureFlag(String name) {
        return getLoadState(name + FEATURE_ENDPOINT);
    }

    /**
     * @param name endpoint name
     * @param loaded the loadState to set
     */
    void setLoadState(String name, Boolean loaded) {
        loadState.put(name, loaded);
    }
    
    /**
     * @param name endpoint name
     * @param loaded the loadState to set
     */
    void setLoadStateFeatureFlag(String name, Boolean loaded) {
        setLoadState(name + FEATURE_ENDPOINT, loaded);
    }
}
