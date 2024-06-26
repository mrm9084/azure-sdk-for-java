// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.azurestackhci.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.azurestackhci.models.GalleryImageStatusProvisioningStatus;
import com.azure.resourcemanager.azurestackhci.models.Status;
import org.junit.jupiter.api.Assertions;

public final class GalleryImageStatusProvisioningStatusTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        GalleryImageStatusProvisioningStatus model =
            BinaryData
                .fromString("{\"operationId\":\"smy\",\"status\":\"Succeeded\"}")
                .toObject(GalleryImageStatusProvisioningStatus.class);
        Assertions.assertEquals("smy", model.operationId());
        Assertions.assertEquals(Status.SUCCEEDED, model.status());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        GalleryImageStatusProvisioningStatus model =
            new GalleryImageStatusProvisioningStatus().withOperationId("smy").withStatus(Status.SUCCEEDED);
        model = BinaryData.fromObject(model).toObject(GalleryImageStatusProvisioningStatus.class);
        Assertions.assertEquals("smy", model.operationId());
        Assertions.assertEquals(Status.SUCCEEDED, model.status());
    }
}
