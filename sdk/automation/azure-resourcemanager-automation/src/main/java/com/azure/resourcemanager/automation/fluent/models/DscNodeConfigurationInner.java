// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.automation.fluent.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.management.ProxyResource;
import com.azure.resourcemanager.automation.models.DscConfigurationAssociationProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

/** Definition of the dsc node configuration. */
@Fluent
public final class DscNodeConfigurationInner extends ProxyResource {
    /*
     * Gets or sets the configuration properties.
     */
    @JsonProperty(value = "properties")
    private DscNodeConfigurationProperties innerProperties;

    /**
     * Get the innerProperties property: Gets or sets the configuration properties.
     *
     * @return the innerProperties value.
     */
    private DscNodeConfigurationProperties innerProperties() {
        return this.innerProperties;
    }

    /**
     * Get the lastModifiedTime property: Gets or sets the last modified time.
     *
     * @return the lastModifiedTime value.
     */
    public OffsetDateTime lastModifiedTime() {
        return this.innerProperties() == null ? null : this.innerProperties().lastModifiedTime();
    }

    /**
     * Set the lastModifiedTime property: Gets or sets the last modified time.
     *
     * @param lastModifiedTime the lastModifiedTime value to set.
     * @return the DscNodeConfigurationInner object itself.
     */
    public DscNodeConfigurationInner withLastModifiedTime(OffsetDateTime lastModifiedTime) {
        if (this.innerProperties() == null) {
            this.innerProperties = new DscNodeConfigurationProperties();
        }
        this.innerProperties().withLastModifiedTime(lastModifiedTime);
        return this;
    }

    /**
     * Get the creationTime property: Gets or sets creation time.
     *
     * @return the creationTime value.
     */
    public OffsetDateTime creationTime() {
        return this.innerProperties() == null ? null : this.innerProperties().creationTime();
    }

    /**
     * Set the creationTime property: Gets or sets creation time.
     *
     * @param creationTime the creationTime value to set.
     * @return the DscNodeConfigurationInner object itself.
     */
    public DscNodeConfigurationInner withCreationTime(OffsetDateTime creationTime) {
        if (this.innerProperties() == null) {
            this.innerProperties = new DscNodeConfigurationProperties();
        }
        this.innerProperties().withCreationTime(creationTime);
        return this;
    }

    /**
     * Get the configuration property: Gets or sets the configuration of the node.
     *
     * @return the configuration value.
     */
    public DscConfigurationAssociationProperty configuration() {
        return this.innerProperties() == null ? null : this.innerProperties().configuration();
    }

    /**
     * Set the configuration property: Gets or sets the configuration of the node.
     *
     * @param configuration the configuration value to set.
     * @return the DscNodeConfigurationInner object itself.
     */
    public DscNodeConfigurationInner withConfiguration(DscConfigurationAssociationProperty configuration) {
        if (this.innerProperties() == null) {
            this.innerProperties = new DscNodeConfigurationProperties();
        }
        this.innerProperties().withConfiguration(configuration);
        return this;
    }

    /**
     * Get the source property: Source of node configuration.
     *
     * @return the source value.
     */
    public String source() {
        return this.innerProperties() == null ? null : this.innerProperties().source();
    }

    /**
     * Set the source property: Source of node configuration.
     *
     * @param source the source value to set.
     * @return the DscNodeConfigurationInner object itself.
     */
    public DscNodeConfigurationInner withSource(String source) {
        if (this.innerProperties() == null) {
            this.innerProperties = new DscNodeConfigurationProperties();
        }
        this.innerProperties().withSource(source);
        return this;
    }

    /**
     * Get the nodeCount property: Number of nodes with this node configuration assigned.
     *
     * @return the nodeCount value.
     */
    public Long nodeCount() {
        return this.innerProperties() == null ? null : this.innerProperties().nodeCount();
    }

    /**
     * Set the nodeCount property: Number of nodes with this node configuration assigned.
     *
     * @param nodeCount the nodeCount value to set.
     * @return the DscNodeConfigurationInner object itself.
     */
    public DscNodeConfigurationInner withNodeCount(Long nodeCount) {
        if (this.innerProperties() == null) {
            this.innerProperties = new DscNodeConfigurationProperties();
        }
        this.innerProperties().withNodeCount(nodeCount);
        return this;
    }

    /**
     * Get the incrementNodeConfigurationBuild property: If a new build version of NodeConfiguration is required.
     *
     * @return the incrementNodeConfigurationBuild value.
     */
    public Boolean incrementNodeConfigurationBuild() {
        return this.innerProperties() == null ? null : this.innerProperties().incrementNodeConfigurationBuild();
    }

    /**
     * Set the incrementNodeConfigurationBuild property: If a new build version of NodeConfiguration is required.
     *
     * @param incrementNodeConfigurationBuild the incrementNodeConfigurationBuild value to set.
     * @return the DscNodeConfigurationInner object itself.
     */
    public DscNodeConfigurationInner withIncrementNodeConfigurationBuild(Boolean incrementNodeConfigurationBuild) {
        if (this.innerProperties() == null) {
            this.innerProperties = new DscNodeConfigurationProperties();
        }
        this.innerProperties().withIncrementNodeConfigurationBuild(incrementNodeConfigurationBuild);
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (innerProperties() != null) {
            innerProperties().validate();
        }
    }
}
