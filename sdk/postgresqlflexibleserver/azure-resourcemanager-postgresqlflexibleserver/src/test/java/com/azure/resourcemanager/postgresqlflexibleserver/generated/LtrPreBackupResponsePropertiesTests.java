// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.postgresqlflexibleserver.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.postgresqlflexibleserver.fluent.models.LtrPreBackupResponseProperties;
import org.junit.jupiter.api.Assertions;

public final class LtrPreBackupResponsePropertiesTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        LtrPreBackupResponseProperties model = BinaryData.fromString("{\"numberOfContainers\":1761160515}")
            .toObject(LtrPreBackupResponseProperties.class);
        Assertions.assertEquals(1761160515, model.numberOfContainers());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        LtrPreBackupResponseProperties model = new LtrPreBackupResponseProperties().withNumberOfContainers(1761160515);
        model = BinaryData.fromObject(model).toObject(LtrPreBackupResponseProperties.class);
        Assertions.assertEquals(1761160515, model.numberOfContainers());
    }
}
