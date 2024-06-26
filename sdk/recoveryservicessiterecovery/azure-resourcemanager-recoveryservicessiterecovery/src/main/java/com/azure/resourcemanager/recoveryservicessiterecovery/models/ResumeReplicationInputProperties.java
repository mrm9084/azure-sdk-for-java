// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.recoveryservicessiterecovery.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resume replication input properties.
 */
@Fluent
public final class ResumeReplicationInputProperties {
    /*
     * The provider specific input for resume replication.
     */
    @JsonProperty(value = "providerSpecificDetails", required = true)
    private ResumeReplicationProviderSpecificInput providerSpecificDetails;

    /**
     * Creates an instance of ResumeReplicationInputProperties class.
     */
    public ResumeReplicationInputProperties() {
    }

    /**
     * Get the providerSpecificDetails property: The provider specific input for resume replication.
     * 
     * @return the providerSpecificDetails value.
     */
    public ResumeReplicationProviderSpecificInput providerSpecificDetails() {
        return this.providerSpecificDetails;
    }

    /**
     * Set the providerSpecificDetails property: The provider specific input for resume replication.
     * 
     * @param providerSpecificDetails the providerSpecificDetails value to set.
     * @return the ResumeReplicationInputProperties object itself.
     */
    public ResumeReplicationInputProperties
        withProviderSpecificDetails(ResumeReplicationProviderSpecificInput providerSpecificDetails) {
        this.providerSpecificDetails = providerSpecificDetails;
        return this;
    }

    /**
     * Validates the instance.
     * 
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (providerSpecificDetails() == null) {
            throw LOGGER.logExceptionAsError(new IllegalArgumentException(
                "Missing required property providerSpecificDetails in model ResumeReplicationInputProperties"));
        } else {
            providerSpecificDetails().validate();
        }
    }

    private static final ClientLogger LOGGER = new ClientLogger(ResumeReplicationInputProperties.class);
}
