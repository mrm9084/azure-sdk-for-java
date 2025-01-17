// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.appcontainers.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.appcontainers.fluent.models.JavaComponentInner;
import com.azure.resourcemanager.appcontainers.models.JavaComponentConfigurationProperty;
import com.azure.resourcemanager.appcontainers.models.JavaComponentProperties;
import com.azure.resourcemanager.appcontainers.models.JavaComponentPropertiesScale;
import com.azure.resourcemanager.appcontainers.models.JavaComponentServiceBind;
import com.azure.resourcemanager.appcontainers.models.JavaComponentsCollection;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;

public final class JavaComponentsCollectionTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        JavaComponentsCollection model = BinaryData.fromString(
            "{\"value\":[{\"properties\":{\"componentType\":\"JavaComponentProperties\",\"provisioningState\":\"Succeeded\",\"configurations\":[{\"propertyName\":\"qroohtu\",\"value\":\"maonurj\"},{\"propertyName\":\"mghihp\",\"value\":\"cmslclblyjxltbs\"},{\"propertyName\":\"scvsfxigctm\",\"value\":\"uupb\"}],\"scale\":{\"minReplicas\":922272916,\"maxReplicas\":1106041304},\"serviceBinds\":[{\"name\":\"ceukdqkkyihztg\",\"serviceId\":\"mgqzgwldoyc\"},{\"name\":\"llcecfehuwaoa\",\"serviceId\":\"h\"},{\"name\":\"qllizstac\",\"serviceId\":\"vhrweftkwqejpmv\"},{\"name\":\"ehaepwamcxtc\",\"serviceId\":\"upeuknijduyye\"}]},\"id\":\"ydjfb\",\"name\":\"c\",\"type\":\"v\"},{\"properties\":{\"componentType\":\"JavaComponentProperties\",\"provisioningState\":\"Succeeded\",\"configurations\":[{\"propertyName\":\"wikdmh\",\"value\":\"kuflgbh\"},{\"propertyName\":\"uacdixmxuf\",\"value\":\"ryjqgdkf\"}],\"scale\":{\"minReplicas\":1021158786,\"maxReplicas\":1850515821},\"serviceBinds\":[{\"name\":\"jhvefgwbmqjchnt\",\"serviceId\":\"faymxbulpz\"},{\"name\":\"lbm\",\"serviceId\":\"yojwyvfkmbtsu\"},{\"name\":\"xsgxjcmmzrrs\",\"serviceId\":\"biwsd\"},{\"name\":\"pxqwo\",\"serviceId\":\"ffjxcjrmmuabwib\"}]},\"id\":\"ogjo\",\"name\":\"mcyefoyzbam\",\"type\":\"in\"},{\"properties\":{\"componentType\":\"JavaComponentProperties\",\"provisioningState\":\"Failed\",\"configurations\":[{\"propertyName\":\"kpoldtvevboc\",\"value\":\"hzjkn\"}],\"scale\":{\"minReplicas\":1180422043,\"maxReplicas\":483249279},\"serviceBinds\":[{\"name\":\"nrup\",\"serviceId\":\"amrdixtrekidswys\"}]},\"id\":\"ruffgllukk\",\"name\":\"tvlxhrpqh\",\"type\":\"mblcouqehbhbcds\"}],\"nextLink\":\"ryrando\"}")
            .toObject(JavaComponentsCollection.class);
        Assertions.assertEquals("qroohtu", model.value().get(0).properties().configurations().get(0).propertyName());
        Assertions.assertEquals("maonurj", model.value().get(0).properties().configurations().get(0).value());
        Assertions.assertEquals(922272916, model.value().get(0).properties().scale().minReplicas());
        Assertions.assertEquals(1106041304, model.value().get(0).properties().scale().maxReplicas());
        Assertions.assertEquals("ceukdqkkyihztg", model.value().get(0).properties().serviceBinds().get(0).name());
        Assertions.assertEquals("mgqzgwldoyc", model.value().get(0).properties().serviceBinds().get(0).serviceId());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        JavaComponentsCollection model
            = new JavaComponentsCollection().withValue(Arrays.asList(
                new JavaComponentInner()
                    .withProperties(new JavaComponentProperties()
                        .withConfigurations(Arrays.asList(
                            new JavaComponentConfigurationProperty().withPropertyName("qroohtu").withValue("maonurj"),
                            new JavaComponentConfigurationProperty().withPropertyName("mghihp")
                                .withValue("cmslclblyjxltbs"),
                            new JavaComponentConfigurationProperty().withPropertyName("scvsfxigctm").withValue("uupb")))
                        .withScale(
                            new JavaComponentPropertiesScale().withMinReplicas(922272916).withMaxReplicas(1106041304))
                        .withServiceBinds(Arrays.asList(
                            new JavaComponentServiceBind().withName("ceukdqkkyihztg").withServiceId("mgqzgwldoyc"),
                            new JavaComponentServiceBind().withName("llcecfehuwaoa").withServiceId("h"),
                            new JavaComponentServiceBind().withName("qllizstac").withServiceId("vhrweftkwqejpmv"),
                            new JavaComponentServiceBind().withName("ehaepwamcxtc").withServiceId("upeuknijduyye")))),
                new JavaComponentInner()
                    .withProperties(new JavaComponentProperties()
                        .withConfigurations(Arrays.asList(
                            new JavaComponentConfigurationProperty().withPropertyName("wikdmh").withValue("kuflgbh"),
                            new JavaComponentConfigurationProperty().withPropertyName("uacdixmxuf")
                                .withValue("ryjqgdkf")))
                        .withScale(
                            new JavaComponentPropertiesScale().withMinReplicas(1021158786).withMaxReplicas(1850515821))
                        .withServiceBinds(Arrays.asList(
                            new JavaComponentServiceBind().withName("jhvefgwbmqjchnt").withServiceId("faymxbulpz"),
                            new JavaComponentServiceBind().withName("lbm").withServiceId("yojwyvfkmbtsu"),
                            new JavaComponentServiceBind().withName("xsgxjcmmzrrs").withServiceId("biwsd"),
                            new JavaComponentServiceBind().withName("pxqwo").withServiceId("ffjxcjrmmuabwib")))),
                new JavaComponentInner().withProperties(new JavaComponentProperties()
                    .withConfigurations(Arrays.asList(
                        new JavaComponentConfigurationProperty().withPropertyName("kpoldtvevboc").withValue("hzjkn")))
                    .withScale(
                        new JavaComponentPropertiesScale().withMinReplicas(1180422043).withMaxReplicas(483249279))
                    .withServiceBinds(Arrays
                        .asList(new JavaComponentServiceBind().withName("nrup").withServiceId("amrdixtrekidswys"))))));
        model = BinaryData.fromObject(model).toObject(JavaComponentsCollection.class);
        Assertions.assertEquals("qroohtu", model.value().get(0).properties().configurations().get(0).propertyName());
        Assertions.assertEquals("maonurj", model.value().get(0).properties().configurations().get(0).value());
        Assertions.assertEquals(922272916, model.value().get(0).properties().scale().minReplicas());
        Assertions.assertEquals(1106041304, model.value().get(0).properties().scale().maxReplicas());
        Assertions.assertEquals("ceukdqkkyihztg", model.value().get(0).properties().serviceBinds().get(0).name());
        Assertions.assertEquals("mgqzgwldoyc", model.value().get(0).properties().serviceBinds().get(0).serviceId());
    }
}
