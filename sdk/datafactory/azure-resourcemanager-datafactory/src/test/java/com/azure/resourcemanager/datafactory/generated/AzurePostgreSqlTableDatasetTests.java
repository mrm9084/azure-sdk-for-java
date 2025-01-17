// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.datafactory.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.datafactory.models.AzurePostgreSqlTableDataset;
import com.azure.resourcemanager.datafactory.models.DatasetFolder;
import com.azure.resourcemanager.datafactory.models.LinkedServiceReference;
import com.azure.resourcemanager.datafactory.models.ParameterSpecification;
import com.azure.resourcemanager.datafactory.models.ParameterType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;

public final class AzurePostgreSqlTableDatasetTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        AzurePostgreSqlTableDataset model = BinaryData.fromString(
            "{\"type\":\"AzurePostgreSqlTable\",\"typeProperties\":{\"tableName\":\"datascec\",\"table\":\"dataaajdfwrdkql\",\"schema\":\"datakfekdesbpjq\"},\"description\":\"lbh\",\"structure\":\"datapduibsr\",\"schema\":\"dataqnneqrypyurvs\",\"linkedServiceName\":{\"referenceName\":\"hovtuercp\",\"parameters\":{\"yb\":\"datawc\",\"nwczsraz\":\"datadzycxhaoegjzgplj\"}},\"parameters\":{\"uapasizzfmugykw\":{\"type\":\"SecureString\",\"defaultValue\":\"datacqhxhj\"},\"enndzgthdzit\":{\"type\":\"Int\",\"defaultValue\":\"datauo\"},\"wonadezmzxvfybxm\":{\"type\":\"Object\",\"defaultValue\":\"datafpherwjqvsw\"},\"c\":{\"type\":\"Object\",\"defaultValue\":\"datanuvqkrrsguog\"}},\"annotations\":[\"datatpyabensjflwp\",\"datatvvqtmvifgcvsim\",\"datalbmti\",\"dataxgosnxa\"],\"folder\":{\"name\":\"cdfmzxaoxlhmvjc\"},\"\":{\"xh\":\"datasbnuc\",\"nkleldk\":\"dataaqoqbvejoysoxovl\",\"qrykkxakruupti\":\"datadlqqhn\"}}")
            .toObject(AzurePostgreSqlTableDataset.class);
        Assertions.assertEquals("lbh", model.description());
        Assertions.assertEquals("hovtuercp", model.linkedServiceName().referenceName());
        Assertions.assertEquals(ParameterType.SECURE_STRING, model.parameters().get("uapasizzfmugykw").type());
        Assertions.assertEquals("cdfmzxaoxlhmvjc", model.folder().name());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        AzurePostgreSqlTableDataset model = new AzurePostgreSqlTableDataset().withDescription("lbh")
            .withStructure("datapduibsr")
            .withSchema("dataqnneqrypyurvs")
            .withLinkedServiceName(new LinkedServiceReference().withReferenceName("hovtuercp")
                .withParameters(mapOf("yb", "datawc", "nwczsraz", "datadzycxhaoegjzgplj")))
            .withParameters(mapOf("uapasizzfmugykw",
                new ParameterSpecification().withType(ParameterType.SECURE_STRING).withDefaultValue("datacqhxhj"),
                "enndzgthdzit", new ParameterSpecification().withType(ParameterType.INT).withDefaultValue("datauo"),
                "wonadezmzxvfybxm",
                new ParameterSpecification().withType(ParameterType.OBJECT).withDefaultValue("datafpherwjqvsw"), "c",
                new ParameterSpecification().withType(ParameterType.OBJECT).withDefaultValue("datanuvqkrrsguog")))
            .withAnnotations(Arrays.asList("datatpyabensjflwp", "datatvvqtmvifgcvsim", "datalbmti", "dataxgosnxa"))
            .withFolder(new DatasetFolder().withName("cdfmzxaoxlhmvjc"))
            .withTableName("datascec")
            .withTable("dataaajdfwrdkql")
            .withSchemaTypePropertiesSchema("datakfekdesbpjq");
        model = BinaryData.fromObject(model).toObject(AzurePostgreSqlTableDataset.class);
        Assertions.assertEquals("lbh", model.description());
        Assertions.assertEquals("hovtuercp", model.linkedServiceName().referenceName());
        Assertions.assertEquals(ParameterType.SECURE_STRING, model.parameters().get("uapasizzfmugykw").type());
        Assertions.assertEquals("cdfmzxaoxlhmvjc", model.folder().name());
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
