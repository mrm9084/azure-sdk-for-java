// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.imagebuilder.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.imagebuilder.models.ImageTemplateManagedImageSource;
import org.junit.jupiter.api.Assertions;

public final class ImageTemplateManagedImageSourceTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        ImageTemplateManagedImageSource model
            = BinaryData.fromString("{\"type\":\"ManagedImage\",\"imageId\":\"dcsi\"}")
                .toObject(ImageTemplateManagedImageSource.class);
        Assertions.assertEquals("dcsi", model.imageId());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        ImageTemplateManagedImageSource model = new ImageTemplateManagedImageSource().withImageId("dcsi");
        model = BinaryData.fromObject(model).toObject(ImageTemplateManagedImageSource.class);
        Assertions.assertEquals("dcsi", model.imageId());
    }
}
