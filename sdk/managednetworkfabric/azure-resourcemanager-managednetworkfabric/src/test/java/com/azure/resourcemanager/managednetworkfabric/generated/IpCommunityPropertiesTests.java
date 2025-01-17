// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.managednetworkfabric.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.managednetworkfabric.fluent.models.IpCommunityProperties;
import com.azure.resourcemanager.managednetworkfabric.models.CommunityActionTypes;
import com.azure.resourcemanager.managednetworkfabric.models.IpCommunityRule;
import com.azure.resourcemanager.managednetworkfabric.models.WellKnownCommunities;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;

public final class IpCommunityPropertiesTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        IpCommunityProperties model = BinaryData.fromString(
            "{\"configurationState\":\"Accepted\",\"provisioningState\":\"Accepted\",\"administrativeState\":\"Disabled\",\"ipCommunityRules\":[{\"action\":\"Deny\",\"sequenceNumber\":8431047214936206826,\"wellKnownCommunities\":[\"Internet\",\"Internet\",\"GShut\",\"NoExport\"],\"communityMembers\":[\"jjkhvyomaclu\",\"vxnqmhrpqpd\"]},{\"action\":\"Permit\",\"sequenceNumber\":1779225770048104813,\"wellKnownCommunities\":[\"NoAdvertise\",\"LocalAS\",\"NoAdvertise\",\"Internet\"],\"communityMembers\":[\"f\",\"xuifmcsypobkdqz\"]},{\"action\":\"Deny\",\"sequenceNumber\":1055349990243516782,\"wellKnownCommunities\":[\"LocalAS\"],\"communityMembers\":[\"lgtrczzy\"]}],\"annotation\":\"xzji\"}")
            .toObject(IpCommunityProperties.class);
        Assertions.assertEquals("xzji", model.annotation());
        Assertions.assertEquals(CommunityActionTypes.DENY, model.ipCommunityRules().get(0).action());
        Assertions.assertEquals(8431047214936206826L, model.ipCommunityRules().get(0).sequenceNumber());
        Assertions.assertEquals(WellKnownCommunities.INTERNET,
            model.ipCommunityRules().get(0).wellKnownCommunities().get(0));
        Assertions.assertEquals("jjkhvyomaclu", model.ipCommunityRules().get(0).communityMembers().get(0));
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        IpCommunityProperties model = new IpCommunityProperties().withAnnotation("xzji")
            .withIpCommunityRules(Arrays.asList(
                new IpCommunityRule().withAction(CommunityActionTypes.DENY)
                    .withSequenceNumber(8431047214936206826L)
                    .withWellKnownCommunities(Arrays.asList(WellKnownCommunities.INTERNET,
                        WellKnownCommunities.INTERNET, WellKnownCommunities.GSHUT, WellKnownCommunities.NO_EXPORT))
                    .withCommunityMembers(Arrays.asList("jjkhvyomaclu", "vxnqmhrpqpd")),
                new IpCommunityRule().withAction(CommunityActionTypes.PERMIT)
                    .withSequenceNumber(1779225770048104813L)
                    .withWellKnownCommunities(
                        Arrays.asList(WellKnownCommunities.NO_ADVERTISE, WellKnownCommunities.LOCAL_AS,
                            WellKnownCommunities.NO_ADVERTISE, WellKnownCommunities.INTERNET))
                    .withCommunityMembers(Arrays.asList("f", "xuifmcsypobkdqz")),
                new IpCommunityRule().withAction(CommunityActionTypes.DENY)
                    .withSequenceNumber(1055349990243516782L)
                    .withWellKnownCommunities(Arrays.asList(WellKnownCommunities.LOCAL_AS))
                    .withCommunityMembers(Arrays.asList("lgtrczzy"))));
        model = BinaryData.fromObject(model).toObject(IpCommunityProperties.class);
        Assertions.assertEquals("xzji", model.annotation());
        Assertions.assertEquals(CommunityActionTypes.DENY, model.ipCommunityRules().get(0).action());
        Assertions.assertEquals(8431047214936206826L, model.ipCommunityRules().get(0).sequenceNumber());
        Assertions.assertEquals(WellKnownCommunities.INTERNET,
            model.ipCommunityRules().get(0).wellKnownCommunities().get(0));
        Assertions.assertEquals("jjkhvyomaclu", model.ipCommunityRules().get(0).communityMembers().get(0));
    }
}
