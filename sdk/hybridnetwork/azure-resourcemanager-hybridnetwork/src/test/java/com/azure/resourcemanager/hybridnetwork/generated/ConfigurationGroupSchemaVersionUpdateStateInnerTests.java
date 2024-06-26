// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.hybridnetwork.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.hybridnetwork.fluent.models.ConfigurationGroupSchemaVersionUpdateStateInner;
import com.azure.resourcemanager.hybridnetwork.models.VersionState;
import org.junit.jupiter.api.Assertions;

public final class ConfigurationGroupSchemaVersionUpdateStateInnerTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        ConfigurationGroupSchemaVersionUpdateStateInner model = BinaryData.fromString("{\"versionState\":\"Preview\"}")
            .toObject(ConfigurationGroupSchemaVersionUpdateStateInner.class);
        Assertions.assertEquals(VersionState.PREVIEW, model.versionState());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        ConfigurationGroupSchemaVersionUpdateStateInner model
            = new ConfigurationGroupSchemaVersionUpdateStateInner().withVersionState(VersionState.PREVIEW);
        model = BinaryData.fromObject(model).toObject(ConfigurationGroupSchemaVersionUpdateStateInner.class);
        Assertions.assertEquals(VersionState.PREVIEW, model.versionState());
    }
}
