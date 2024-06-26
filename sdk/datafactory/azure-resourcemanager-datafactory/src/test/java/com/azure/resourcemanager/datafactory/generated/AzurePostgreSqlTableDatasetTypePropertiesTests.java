// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.datafactory.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.datafactory.fluent.models.AzurePostgreSqlTableDatasetTypeProperties;

public final class AzurePostgreSqlTableDatasetTypePropertiesTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        AzurePostgreSqlTableDatasetTypeProperties model
            = BinaryData.fromString("{\"tableName\":\"dataez\",\"table\":\"datagdpftfcb\",\"schema\":\"datasrdp\"}")
                .toObject(AzurePostgreSqlTableDatasetTypeProperties.class);
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        AzurePostgreSqlTableDatasetTypeProperties model
            = new AzurePostgreSqlTableDatasetTypeProperties().withTableName("dataez")
                .withTable("datagdpftfcb")
                .withSchema("datasrdp");
        model = BinaryData.fromObject(model).toObject(AzurePostgreSqlTableDatasetTypeProperties.class);
    }
}
