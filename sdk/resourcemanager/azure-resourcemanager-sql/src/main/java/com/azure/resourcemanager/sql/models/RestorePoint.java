// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.resourcemanager.sql.models;

import com.azure.core.annotation.Fluent;
import com.azure.resourcemanager.resources.fluentcore.arm.models.HasId;
import com.azure.resourcemanager.resources.fluentcore.arm.models.HasName;
import com.azure.resourcemanager.resources.fluentcore.arm.models.HasResourceGroup;
import com.azure.resourcemanager.resources.fluentcore.model.HasInnerModel;
import com.azure.resourcemanager.sql.fluent.models.RestorePointInner;
import java.time.OffsetDateTime;

/** An immutable client-side representation of an Azure SQL database's Restore Point. */
@Fluent
public interface RestorePoint extends HasInnerModel<RestorePointInner>, HasResourceGroup, HasName, HasId {
    /**
     * Gets name of the SQL Server to which this replication belongs.
     *
     * @return name of the SQL Server to which this replication belongs
     */
    String sqlServerName();

    /**
     * Gets name of the SQL Database to which this replication belongs.
     *
     * @return name of the SQL Database to which this replication belongs
     */
    String databaseName();

    /**
     * Gets the ID of the SQL Database to which this replication belongs.
     *
     * @return the ID of the SQL Database to which this replication belongs
     */
    String databaseId();

    /**
     * Gets the restore point type of the Azure SQL Database restore point.
     *
     * @return the restore point type of the Azure SQL Database restore point.
     */
    RestorePointType restorePointType();

    /**
     * Gets restore point creation time.
     *
     * @return restore point creation time (ISO8601 format). Populated when restorePointType = CONTINUOUS. Null
     *     otherwise.
     */
    OffsetDateTime restorePointCreationDate();

    /**
     * Gets earliest restore time.
     *
     * @return earliest restore time (ISO8601 format). Populated when restorePointType = DISCRETE. Null otherwise.
     */
    OffsetDateTime earliestRestoreDate();
}
