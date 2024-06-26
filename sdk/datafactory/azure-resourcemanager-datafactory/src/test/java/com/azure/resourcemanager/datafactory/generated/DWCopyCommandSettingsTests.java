// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.datafactory.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.datafactory.models.DWCopyCommandDefaultValue;
import com.azure.resourcemanager.datafactory.models.DWCopyCommandSettings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;

public final class DWCopyCommandSettingsTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        DWCopyCommandSettings model = BinaryData.fromString(
            "{\"defaultValues\":[{\"columnName\":\"datahozkmi\",\"defaultValue\":\"dataxdnugbisfnbtqd\"},{\"columnName\":\"dataw\",\"defaultValue\":\"datadroi\"},{\"columnName\":\"databulvk\",\"defaultValue\":\"datayhnfqnekpxd\"},{\"columnName\":\"datae\",\"defaultValue\":\"dataf\"}],\"additionalOptions\":{\"xjdolobtzr\":\"ahnsmktkhlq\",\"lpbzo\":\"xnlaurviyntc\",\"fkte\":\"tfbjk\"}}")
            .toObject(DWCopyCommandSettings.class);
        Assertions.assertEquals("ahnsmktkhlq", model.additionalOptions().get("xjdolobtzr"));
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        DWCopyCommandSettings model = new DWCopyCommandSettings()
            .withDefaultValues(Arrays.asList(
                new DWCopyCommandDefaultValue().withColumnName("datahozkmi").withDefaultValue("dataxdnugbisfnbtqd"),
                new DWCopyCommandDefaultValue().withColumnName("dataw").withDefaultValue("datadroi"),
                new DWCopyCommandDefaultValue().withColumnName("databulvk").withDefaultValue("datayhnfqnekpxd"),
                new DWCopyCommandDefaultValue().withColumnName("datae").withDefaultValue("dataf")))
            .withAdditionalOptions(mapOf("xjdolobtzr", "ahnsmktkhlq", "lpbzo", "xnlaurviyntc", "fkte", "tfbjk"));
        model = BinaryData.fromObject(model).toObject(DWCopyCommandSettings.class);
        Assertions.assertEquals("ahnsmktkhlq", model.additionalOptions().get("xjdolobtzr"));
    }

    // Use "Map.of" if available
    @SuppressWarnings("unchecked")
    private static <T> Map<String, T> mapOf(Object... inputs) {
        Map<String, T> map = new HashMap<>();
        for (int i = 0; i < inputs.length; i += 2) {
            String key = (String) inputs[i];
            T value = (T) inputs[i + 1];
            map.put(key, value);
        }
        return map;
    }
}
