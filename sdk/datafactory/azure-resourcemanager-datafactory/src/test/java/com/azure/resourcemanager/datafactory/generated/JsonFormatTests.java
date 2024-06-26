// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.datafactory.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.datafactory.models.JsonFormat;

public final class JsonFormatTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        JsonFormat model = BinaryData.fromString(
            "{\"type\":\"jrnm\",\"filePattern\":\"datadxmdses\",\"nestingSeparator\":\"dataujbjppp\",\"encodingName\":\"datalpdib\",\"jsonNodeReference\":\"datath\",\"jsonPathDefinition\":\"dataat\",\"serializer\":\"datazqpl\",\"deserializer\":\"datakihonik\",\"\":{\"gkensckhbmcarmo\":\"datazfffjilzfbpnt\"}}")
            .toObject(JsonFormat.class);
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        JsonFormat model = new JsonFormat().withSerializer("datazqpl")
            .withDeserializer("datakihonik")
            .withFilePattern("datadxmdses")
            .withNestingSeparator("dataujbjppp")
            .withEncodingName("datalpdib")
            .withJsonNodeReference("datath")
            .withJsonPathDefinition("dataat");
        model = BinaryData.fromObject(model).toObject(JsonFormat.class);
    }
}
