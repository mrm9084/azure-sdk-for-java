// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.azurestackhci.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The InterfaceDnsSettings model. */
@Fluent
public final class InterfaceDnsSettings {
    /*
     * List of DNS server IP Addresses for the interface
     */
    @JsonProperty(value = "dnsServers")
    private List<String> dnsServers;

    /** Creates an instance of InterfaceDnsSettings class. */
    public InterfaceDnsSettings() {
    }

    /**
     * Get the dnsServers property: List of DNS server IP Addresses for the interface.
     *
     * @return the dnsServers value.
     */
    public List<String> dnsServers() {
        return this.dnsServers;
    }

    /**
     * Set the dnsServers property: List of DNS server IP Addresses for the interface.
     *
     * @param dnsServers the dnsServers value to set.
     * @return the InterfaceDnsSettings object itself.
     */
    public InterfaceDnsSettings withDnsServers(List<String> dnsServers) {
        this.dnsServers = dnsServers;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
    }
}
