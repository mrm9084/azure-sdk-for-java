// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.azurearcdata.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

/** Properties of SqlServerInstance. */
@Fluent
public final class SqlServerInstanceProperties {
    /*
     * SQL Server version.
     */
    @JsonProperty(value = "version")
    private SqlVersion version;

    /*
     * SQL Server edition.
     */
    @JsonProperty(value = "edition")
    private EditionType edition;

    /*
     * ARM Resource id of the container resource (Azure Arc for Servers).
     */
    @JsonProperty(value = "containerResourceId", required = true)
    private String containerResourceId;

    /*
     * The time when the resource was created.
     */
    @JsonProperty(value = "createTime", access = JsonProperty.Access.WRITE_ONLY)
    private String createTime;

    /*
     * The number of logical processors used by the SQL Server instance.
     */
    @JsonProperty(value = "vCore")
    private String vCore;

    /*
     * The cloud connectivity status.
     */
    @JsonProperty(value = "status", required = true)
    private ConnectionStatus status;

    /*
     * SQL Server update level.
     */
    @JsonProperty(value = "patchLevel")
    private String patchLevel;

    /*
     * SQL Server collation.
     */
    @JsonProperty(value = "collation")
    private String collation;

    /*
     * SQL Server current version.
     */
    @JsonProperty(value = "currentVersion")
    private String currentVersion;

    /*
     * SQL Server instance name.
     */
    @JsonProperty(value = "instanceName")
    private String instanceName;

    /*
     * Dynamic TCP ports used by SQL Server.
     */
    @JsonProperty(value = "tcpDynamicPorts")
    private String tcpDynamicPorts;

    /*
     * Static TCP ports used by SQL Server.
     */
    @JsonProperty(value = "tcpStaticPorts")
    private String tcpStaticPorts;

    /*
     * SQL Server product ID.
     */
    @JsonProperty(value = "productId")
    private String productId;

    /*
     * SQL Server license type.
     */
    @JsonProperty(value = "licenseType")
    private ArcSqlServerLicenseType licenseType;

    /*
     * Timestamp of last Azure Defender status update.
     */
    @JsonProperty(value = "azureDefenderStatusLastUpdated")
    private OffsetDateTime azureDefenderStatusLastUpdated;

    /*
     * Status of Azure Defender.
     */
    @JsonProperty(value = "azureDefenderStatus")
    private DefenderStatus azureDefenderStatus;

    /*
     * The provisioningState property.
     */
    @JsonProperty(value = "provisioningState", access = JsonProperty.Access.WRITE_ONLY)
    private String provisioningState;

    /** Creates an instance of SqlServerInstanceProperties class. */
    public SqlServerInstanceProperties() {
    }

    /**
     * Get the version property: SQL Server version.
     *
     * @return the version value.
     */
    public SqlVersion version() {
        return this.version;
    }

    /**
     * Set the version property: SQL Server version.
     *
     * @param version the version value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withVersion(SqlVersion version) {
        this.version = version;
        return this;
    }

    /**
     * Get the edition property: SQL Server edition.
     *
     * @return the edition value.
     */
    public EditionType edition() {
        return this.edition;
    }

    /**
     * Set the edition property: SQL Server edition.
     *
     * @param edition the edition value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withEdition(EditionType edition) {
        this.edition = edition;
        return this;
    }

    /**
     * Get the containerResourceId property: ARM Resource id of the container resource (Azure Arc for Servers).
     *
     * @return the containerResourceId value.
     */
    public String containerResourceId() {
        return this.containerResourceId;
    }

    /**
     * Set the containerResourceId property: ARM Resource id of the container resource (Azure Arc for Servers).
     *
     * @param containerResourceId the containerResourceId value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withContainerResourceId(String containerResourceId) {
        this.containerResourceId = containerResourceId;
        return this;
    }

    /**
     * Get the createTime property: The time when the resource was created.
     *
     * @return the createTime value.
     */
    public String createTime() {
        return this.createTime;
    }

    /**
     * Get the vCore property: The number of logical processors used by the SQL Server instance.
     *
     * @return the vCore value.
     */
    public String vCore() {
        return this.vCore;
    }

    /**
     * Set the vCore property: The number of logical processors used by the SQL Server instance.
     *
     * @param vCore the vCore value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withVCore(String vCore) {
        this.vCore = vCore;
        return this;
    }

    /**
     * Get the status property: The cloud connectivity status.
     *
     * @return the status value.
     */
    public ConnectionStatus status() {
        return this.status;
    }

    /**
     * Set the status property: The cloud connectivity status.
     *
     * @param status the status value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withStatus(ConnectionStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Get the patchLevel property: SQL Server update level.
     *
     * @return the patchLevel value.
     */
    public String patchLevel() {
        return this.patchLevel;
    }

    /**
     * Set the patchLevel property: SQL Server update level.
     *
     * @param patchLevel the patchLevel value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withPatchLevel(String patchLevel) {
        this.patchLevel = patchLevel;
        return this;
    }

    /**
     * Get the collation property: SQL Server collation.
     *
     * @return the collation value.
     */
    public String collation() {
        return this.collation;
    }

    /**
     * Set the collation property: SQL Server collation.
     *
     * @param collation the collation value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withCollation(String collation) {
        this.collation = collation;
        return this;
    }

    /**
     * Get the currentVersion property: SQL Server current version.
     *
     * @return the currentVersion value.
     */
    public String currentVersion() {
        return this.currentVersion;
    }

    /**
     * Set the currentVersion property: SQL Server current version.
     *
     * @param currentVersion the currentVersion value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
        return this;
    }

    /**
     * Get the instanceName property: SQL Server instance name.
     *
     * @return the instanceName value.
     */
    public String instanceName() {
        return this.instanceName;
    }

    /**
     * Set the instanceName property: SQL Server instance name.
     *
     * @param instanceName the instanceName value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withInstanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }

    /**
     * Get the tcpDynamicPorts property: Dynamic TCP ports used by SQL Server.
     *
     * @return the tcpDynamicPorts value.
     */
    public String tcpDynamicPorts() {
        return this.tcpDynamicPorts;
    }

    /**
     * Set the tcpDynamicPorts property: Dynamic TCP ports used by SQL Server.
     *
     * @param tcpDynamicPorts the tcpDynamicPorts value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withTcpDynamicPorts(String tcpDynamicPorts) {
        this.tcpDynamicPorts = tcpDynamicPorts;
        return this;
    }

    /**
     * Get the tcpStaticPorts property: Static TCP ports used by SQL Server.
     *
     * @return the tcpStaticPorts value.
     */
    public String tcpStaticPorts() {
        return this.tcpStaticPorts;
    }

    /**
     * Set the tcpStaticPorts property: Static TCP ports used by SQL Server.
     *
     * @param tcpStaticPorts the tcpStaticPorts value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withTcpStaticPorts(String tcpStaticPorts) {
        this.tcpStaticPorts = tcpStaticPorts;
        return this;
    }

    /**
     * Get the productId property: SQL Server product ID.
     *
     * @return the productId value.
     */
    public String productId() {
        return this.productId;
    }

    /**
     * Set the productId property: SQL Server product ID.
     *
     * @param productId the productId value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withProductId(String productId) {
        this.productId = productId;
        return this;
    }

    /**
     * Get the licenseType property: SQL Server license type.
     *
     * @return the licenseType value.
     */
    public ArcSqlServerLicenseType licenseType() {
        return this.licenseType;
    }

    /**
     * Set the licenseType property: SQL Server license type.
     *
     * @param licenseType the licenseType value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withLicenseType(ArcSqlServerLicenseType licenseType) {
        this.licenseType = licenseType;
        return this;
    }

    /**
     * Get the azureDefenderStatusLastUpdated property: Timestamp of last Azure Defender status update.
     *
     * @return the azureDefenderStatusLastUpdated value.
     */
    public OffsetDateTime azureDefenderStatusLastUpdated() {
        return this.azureDefenderStatusLastUpdated;
    }

    /**
     * Set the azureDefenderStatusLastUpdated property: Timestamp of last Azure Defender status update.
     *
     * @param azureDefenderStatusLastUpdated the azureDefenderStatusLastUpdated value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withAzureDefenderStatusLastUpdated(
        OffsetDateTime azureDefenderStatusLastUpdated) {
        this.azureDefenderStatusLastUpdated = azureDefenderStatusLastUpdated;
        return this;
    }

    /**
     * Get the azureDefenderStatus property: Status of Azure Defender.
     *
     * @return the azureDefenderStatus value.
     */
    public DefenderStatus azureDefenderStatus() {
        return this.azureDefenderStatus;
    }

    /**
     * Set the azureDefenderStatus property: Status of Azure Defender.
     *
     * @param azureDefenderStatus the azureDefenderStatus value to set.
     * @return the SqlServerInstanceProperties object itself.
     */
    public SqlServerInstanceProperties withAzureDefenderStatus(DefenderStatus azureDefenderStatus) {
        this.azureDefenderStatus = azureDefenderStatus;
        return this;
    }

    /**
     * Get the provisioningState property: The provisioningState property.
     *
     * @return the provisioningState value.
     */
    public String provisioningState() {
        return this.provisioningState;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (containerResourceId() == null) {
            throw LOGGER
                .logExceptionAsError(
                    new IllegalArgumentException(
                        "Missing required property containerResourceId in model SqlServerInstanceProperties"));
        }
        if (status() == null) {
            throw LOGGER
                .logExceptionAsError(
                    new IllegalArgumentException(
                        "Missing required property status in model SqlServerInstanceProperties"));
        }
    }

    private static final ClientLogger LOGGER = new ClientLogger(SqlServerInstanceProperties.class);
}
