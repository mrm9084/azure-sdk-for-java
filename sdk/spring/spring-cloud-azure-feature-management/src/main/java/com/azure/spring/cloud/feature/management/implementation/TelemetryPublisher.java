// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.feature.management.implementation;

import com.azure.spring.cloud.feature.management.implementation.models.EvaluationEvent;

public interface TelemetryPublisher {
    
    /**
     * Handles as EvaluationEvent and publishes it to the configured telemetry channel
     * 
     * @param evaluationEvent The event to publish.
     */
    public void publishEvent(EvaluationEvent evaluationEvent);

}
