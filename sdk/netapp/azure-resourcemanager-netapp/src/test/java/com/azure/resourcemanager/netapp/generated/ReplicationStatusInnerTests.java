// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.netapp.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.netapp.fluent.models.ReplicationStatusInner;
import com.azure.resourcemanager.netapp.models.MirrorState;
import com.azure.resourcemanager.netapp.models.RelationshipStatus;
import org.junit.jupiter.api.Assertions;

public final class ReplicationStatusInnerTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        ReplicationStatusInner model = BinaryData.fromString(
            "{\"healthy\":true,\"relationshipStatus\":\"Unknown\",\"mirrorState\":\"Broken\",\"totalProgress\":\"epxgyqagvr\",\"errorMessage\":\"npkukghimdblx\"}")
            .toObject(ReplicationStatusInner.class);
        Assertions.assertEquals(true, model.healthy());
        Assertions.assertEquals(RelationshipStatus.UNKNOWN, model.relationshipStatus());
        Assertions.assertEquals(MirrorState.BROKEN, model.mirrorState());
        Assertions.assertEquals("epxgyqagvr", model.totalProgress());
        Assertions.assertEquals("npkukghimdblx", model.errorMessage());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        ReplicationStatusInner model = new ReplicationStatusInner().withHealthy(true)
            .withRelationshipStatus(RelationshipStatus.UNKNOWN)
            .withMirrorState(MirrorState.BROKEN)
            .withTotalProgress("epxgyqagvr")
            .withErrorMessage("npkukghimdblx");
        model = BinaryData.fromObject(model).toObject(ReplicationStatusInner.class);
        Assertions.assertEquals(true, model.healthy());
        Assertions.assertEquals(RelationshipStatus.UNKNOWN, model.relationshipStatus());
        Assertions.assertEquals(MirrorState.BROKEN, model.mirrorState());
        Assertions.assertEquals("epxgyqagvr", model.totalProgress());
        Assertions.assertEquals("npkukghimdblx", model.errorMessage());
    }
}
