// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) TypeSpec Code Generator.

package com.azure.resourcemanager.neonpostgres.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.neonpostgres.fluent.models.OrganizationResourceInner;
import com.azure.resourcemanager.neonpostgres.models.CompanyDetails;
import com.azure.resourcemanager.neonpostgres.models.MarketplaceDetails;
import com.azure.resourcemanager.neonpostgres.models.MarketplaceSubscriptionStatus;
import com.azure.resourcemanager.neonpostgres.models.OfferDetails;
import com.azure.resourcemanager.neonpostgres.models.OrganizationProperties;
import com.azure.resourcemanager.neonpostgres.models.PartnerOrganizationProperties;
import com.azure.resourcemanager.neonpostgres.models.SingleSignOnProperties;
import com.azure.resourcemanager.neonpostgres.models.SingleSignOnStates;
import com.azure.resourcemanager.neonpostgres.models.UserDetails;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;

public final class OrganizationResourceInnerTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        OrganizationResourceInner model = BinaryData.fromString(
            "{\"properties\":{\"marketplaceDetails\":{\"subscriptionId\":\"jbpzvgnwzsymg\",\"subscriptionStatus\":\"PendingFulfillmentStart\",\"offerDetails\":{\"publisherId\":\"fcyzkohdbihanufh\",\"offerId\":\"cbjy\",\"planId\":\"a\",\"planName\":\"th\",\"termUnit\":\"hab\",\"termId\":\"pikxwczbyscnpqxu\"}},\"userDetails\":{\"firstName\":\"vyq\",\"lastName\":\"wby\",\"emailAddress\":\"k\",\"upn\":\"dumjgrtfwvuk\",\"phoneNumber\":\"audccsnhs\"},\"companyDetails\":{\"companyName\":\"nyejhkryhtnap\",\"country\":\"wlokjyem\",\"officeAddress\":\"vnipjox\",\"businessPhone\":\"nchgej\",\"domain\":\"odmailzyd\",\"numberOfEmployees\":223931992450768188},\"provisioningState\":\"Failed\",\"partnerOrganizationProperties\":{\"organizationId\":\"uxinpmqnjaq\",\"organizationName\":\"ixjsprozvcputeg\",\"singleSignOnProperties\":{\"singleSignOnState\":\"Enable\",\"enterpriseAppId\":\"datscmd\",\"singleSignOnUrl\":\"jhulsuuvmkjo\",\"aadDomains\":[\"wfndiodjpsl\",\"ej\"]}}},\"location\":\"vwryoqpso\",\"tags\":{\"j\":\"tazak\",\"yffdfdos\":\"ahbc\",\"hcrzevd\":\"gexpaojakhmsbz\",\"qjbpfzfsin\":\"hlxaolthqtr\"},\"id\":\"gvfcj\",\"name\":\"wzo\",\"type\":\"xjtfelluwfzit\"}")
            .toObject(OrganizationResourceInner.class);
        Assertions.assertEquals("vwryoqpso", model.location());
        Assertions.assertEquals("tazak", model.tags().get("j"));
        Assertions.assertEquals("jbpzvgnwzsymg", model.properties().marketplaceDetails().subscriptionId());
        Assertions.assertEquals(MarketplaceSubscriptionStatus.PENDING_FULFILLMENT_START,
            model.properties().marketplaceDetails().subscriptionStatus());
        Assertions.assertEquals("fcyzkohdbihanufh",
            model.properties().marketplaceDetails().offerDetails().publisherId());
        Assertions.assertEquals("cbjy", model.properties().marketplaceDetails().offerDetails().offerId());
        Assertions.assertEquals("a", model.properties().marketplaceDetails().offerDetails().planId());
        Assertions.assertEquals("th", model.properties().marketplaceDetails().offerDetails().planName());
        Assertions.assertEquals("hab", model.properties().marketplaceDetails().offerDetails().termUnit());
        Assertions.assertEquals("pikxwczbyscnpqxu", model.properties().marketplaceDetails().offerDetails().termId());
        Assertions.assertEquals("vyq", model.properties().userDetails().firstName());
        Assertions.assertEquals("wby", model.properties().userDetails().lastName());
        Assertions.assertEquals("k", model.properties().userDetails().emailAddress());
        Assertions.assertEquals("dumjgrtfwvuk", model.properties().userDetails().upn());
        Assertions.assertEquals("audccsnhs", model.properties().userDetails().phoneNumber());
        Assertions.assertEquals("nyejhkryhtnap", model.properties().companyDetails().companyName());
        Assertions.assertEquals("wlokjyem", model.properties().companyDetails().country());
        Assertions.assertEquals("vnipjox", model.properties().companyDetails().officeAddress());
        Assertions.assertEquals("nchgej", model.properties().companyDetails().businessPhone());
        Assertions.assertEquals("odmailzyd", model.properties().companyDetails().domain());
        Assertions.assertEquals(223931992450768188L, model.properties().companyDetails().numberOfEmployees());
        Assertions.assertEquals("uxinpmqnjaq", model.properties().partnerOrganizationProperties().organizationId());
        Assertions.assertEquals("ixjsprozvcputeg",
            model.properties().partnerOrganizationProperties().organizationName());
        Assertions.assertEquals(SingleSignOnStates.ENABLE,
            model.properties().partnerOrganizationProperties().singleSignOnProperties().singleSignOnState());
        Assertions.assertEquals("datscmd",
            model.properties().partnerOrganizationProperties().singleSignOnProperties().enterpriseAppId());
        Assertions.assertEquals("jhulsuuvmkjo",
            model.properties().partnerOrganizationProperties().singleSignOnProperties().singleSignOnUrl());
        Assertions.assertEquals("wfndiodjpsl",
            model.properties().partnerOrganizationProperties().singleSignOnProperties().aadDomains().get(0));
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        OrganizationResourceInner model = new OrganizationResourceInner().withLocation("vwryoqpso")
            .withTags(mapOf("j", "tazak", "yffdfdos", "ahbc", "hcrzevd", "gexpaojakhmsbz", "qjbpfzfsin", "hlxaolthqtr"))
            .withProperties(new OrganizationProperties()
                .withMarketplaceDetails(new MarketplaceDetails().withSubscriptionId("jbpzvgnwzsymg")
                    .withSubscriptionStatus(MarketplaceSubscriptionStatus.PENDING_FULFILLMENT_START)
                    .withOfferDetails(new OfferDetails().withPublisherId("fcyzkohdbihanufh")
                        .withOfferId("cbjy")
                        .withPlanId("a")
                        .withPlanName("th")
                        .withTermUnit("hab")
                        .withTermId("pikxwczbyscnpqxu")))
                .withUserDetails(new UserDetails().withFirstName("vyq")
                    .withLastName("wby")
                    .withEmailAddress("k")
                    .withUpn("dumjgrtfwvuk")
                    .withPhoneNumber("audccsnhs"))
                .withCompanyDetails(new CompanyDetails().withCompanyName("nyejhkryhtnap")
                    .withCountry("wlokjyem")
                    .withOfficeAddress("vnipjox")
                    .withBusinessPhone("nchgej")
                    .withDomain("odmailzyd")
                    .withNumberOfEmployees(223931992450768188L))
                .withPartnerOrganizationProperties(new PartnerOrganizationProperties().withOrganizationId("uxinpmqnjaq")
                    .withOrganizationName("ixjsprozvcputeg")
                    .withSingleSignOnProperties(
                        new SingleSignOnProperties().withSingleSignOnState(SingleSignOnStates.ENABLE)
                            .withEnterpriseAppId("datscmd")
                            .withSingleSignOnUrl("jhulsuuvmkjo")
                            .withAadDomains(Arrays.asList("wfndiodjpsl", "ej")))));
        model = BinaryData.fromObject(model).toObject(OrganizationResourceInner.class);
        Assertions.assertEquals("vwryoqpso", model.location());
        Assertions.assertEquals("tazak", model.tags().get("j"));
        Assertions.assertEquals("jbpzvgnwzsymg", model.properties().marketplaceDetails().subscriptionId());
        Assertions.assertEquals(MarketplaceSubscriptionStatus.PENDING_FULFILLMENT_START,
            model.properties().marketplaceDetails().subscriptionStatus());
        Assertions.assertEquals("fcyzkohdbihanufh",
            model.properties().marketplaceDetails().offerDetails().publisherId());
        Assertions.assertEquals("cbjy", model.properties().marketplaceDetails().offerDetails().offerId());
        Assertions.assertEquals("a", model.properties().marketplaceDetails().offerDetails().planId());
        Assertions.assertEquals("th", model.properties().marketplaceDetails().offerDetails().planName());
        Assertions.assertEquals("hab", model.properties().marketplaceDetails().offerDetails().termUnit());
        Assertions.assertEquals("pikxwczbyscnpqxu", model.properties().marketplaceDetails().offerDetails().termId());
        Assertions.assertEquals("vyq", model.properties().userDetails().firstName());
        Assertions.assertEquals("wby", model.properties().userDetails().lastName());
        Assertions.assertEquals("k", model.properties().userDetails().emailAddress());
        Assertions.assertEquals("dumjgrtfwvuk", model.properties().userDetails().upn());
        Assertions.assertEquals("audccsnhs", model.properties().userDetails().phoneNumber());
        Assertions.assertEquals("nyejhkryhtnap", model.properties().companyDetails().companyName());
        Assertions.assertEquals("wlokjyem", model.properties().companyDetails().country());
        Assertions.assertEquals("vnipjox", model.properties().companyDetails().officeAddress());
        Assertions.assertEquals("nchgej", model.properties().companyDetails().businessPhone());
        Assertions.assertEquals("odmailzyd", model.properties().companyDetails().domain());
        Assertions.assertEquals(223931992450768188L, model.properties().companyDetails().numberOfEmployees());
        Assertions.assertEquals("uxinpmqnjaq", model.properties().partnerOrganizationProperties().organizationId());
        Assertions.assertEquals("ixjsprozvcputeg",
            model.properties().partnerOrganizationProperties().organizationName());
        Assertions.assertEquals(SingleSignOnStates.ENABLE,
            model.properties().partnerOrganizationProperties().singleSignOnProperties().singleSignOnState());
        Assertions.assertEquals("datscmd",
            model.properties().partnerOrganizationProperties().singleSignOnProperties().enterpriseAppId());
        Assertions.assertEquals("jhulsuuvmkjo",
            model.properties().partnerOrganizationProperties().singleSignOnProperties().singleSignOnUrl());
        Assertions.assertEquals("wfndiodjpsl",
            model.properties().partnerOrganizationProperties().singleSignOnProperties().aadDomains().get(0));
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
