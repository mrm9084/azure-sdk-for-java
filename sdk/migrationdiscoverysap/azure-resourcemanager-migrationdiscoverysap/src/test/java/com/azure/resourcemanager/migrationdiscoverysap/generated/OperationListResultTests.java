// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.migrationdiscoverysap.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.migrationdiscoverysap.models.OperationListResult;

public final class OperationListResultTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        OperationListResult model = BinaryData.fromString(
            "{\"value\":[{\"name\":\"iqfouflmmnkz\",\"isDataAction\":false,\"display\":{\"provider\":\"glougpbk\",\"resource\":\"mutduqktaps\",\"operation\":\"gcue\",\"description\":\"umkdosvqwhbmd\"},\"origin\":\"user\",\"actionType\":\"Internal\"},{\"name\":\"dgmb\",\"isDataAction\":false,\"display\":{\"provider\":\"pbhtqqrolfpfpsa\",\"resource\":\"bquxigjy\",\"operation\":\"zjaoyfhrtxil\",\"description\":\"rkujy\"},\"origin\":\"system\",\"actionType\":\"Internal\"},{\"name\":\"vfqawrlyxwjkcpr\",\"isDataAction\":false,\"display\":{\"provider\":\"gjvtbv\",\"resource\":\"sszdnru\",\"operation\":\"guhmuouqfpr\",\"description\":\"wbnguitnwui\"},\"origin\":\"user\",\"actionType\":\"Internal\"}],\"nextLink\":\"fizuckyf\"}")
            .toObject(OperationListResult.class);
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        OperationListResult model = new OperationListResult();
        model = BinaryData.fromObject(model).toObject(OperationListResult.class);
    }
}
