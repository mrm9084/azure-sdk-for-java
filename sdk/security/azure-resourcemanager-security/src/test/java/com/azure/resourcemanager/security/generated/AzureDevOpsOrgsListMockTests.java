// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.security.generated;

import com.azure.core.credential.AccessToken;
import com.azure.core.http.HttpClient;
import com.azure.core.http.rest.PagedIterable;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.test.http.MockHttpResponse;
import com.azure.resourcemanager.security.SecurityManager;
import com.azure.resourcemanager.security.models.ActionableRemediationState;
import com.azure.resourcemanager.security.models.AnnotateDefaultBranchState;
import com.azure.resourcemanager.security.models.AzureDevOpsOrg;
import com.azure.resourcemanager.security.models.DevOpsProvisioningState;
import com.azure.resourcemanager.security.models.InheritFromParentState;
import com.azure.resourcemanager.security.models.OnboardingState;
import com.azure.resourcemanager.security.models.RuleCategory;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public final class AzureDevOpsOrgsListMockTests {
    @Test
    public void testList() throws Exception {
        String responseStr
            = "{\"value\":[{\"properties\":{\"provisioningStatusMessage\":\"xmuldhfrerkqpyf\",\"provisioningStatusUpdateTimeUtc\":\"2021-02-20T07:58:55Z\",\"provisioningState\":\"Pending\",\"onboardingState\":\"OnboardedByOtherConnector\",\"actionableRemediation\":{\"state\":\"None\",\"categoryConfigurations\":[{\"minimumSeverityLevel\":\"bdjkmnxsggnow\",\"category\":\"Artifacts\"},{\"minimumSeverityLevel\":\"dbrd\",\"category\":\"Dependencies\"}],\"branchConfiguration\":{\"branchNames\":[\"tycvlkusgiikhrc\",\"hyp\"],\"annotateDefaultBranch\":\"Enabled\"},\"inheritFromParentState\":\"Enabled\"}},\"id\":\"mrdiscsdvkymkt\",\"name\":\"wmivoxgzegngl\",\"type\":\"fnfgazaghddcozwx\"}]}";

        HttpClient httpClient
            = response -> Mono.just(new MockHttpResponse(response, 200, responseStr.getBytes(StandardCharsets.UTF_8)));
        SecurityManager manager = SecurityManager.configure()
            .withHttpClient(httpClient)
            .authenticate(tokenRequestContext -> Mono.just(new AccessToken("this_is_a_token", OffsetDateTime.MAX)),
                new AzureProfile("", "", AzureEnvironment.AZURE));

        PagedIterable<AzureDevOpsOrg> response
            = manager.azureDevOpsOrgs().list("pajfhxsmu", "bzadzglmuuzpsu", com.azure.core.util.Context.NONE);

        Assertions.assertEquals(DevOpsProvisioningState.PENDING,
            response.iterator().next().properties().provisioningState());
        Assertions.assertEquals(OnboardingState.ONBOARDED_BY_OTHER_CONNECTOR,
            response.iterator().next().properties().onboardingState());
        Assertions.assertEquals(ActionableRemediationState.NONE,
            response.iterator().next().properties().actionableRemediation().state());
        Assertions.assertEquals("bdjkmnxsggnow",
            response.iterator()
                .next()
                .properties()
                .actionableRemediation()
                .categoryConfigurations()
                .get(0)
                .minimumSeverityLevel());
        Assertions.assertEquals(RuleCategory.ARTIFACTS,
            response.iterator().next().properties().actionableRemediation().categoryConfigurations().get(0).category());
        Assertions.assertEquals("tycvlkusgiikhrc",
            response.iterator().next().properties().actionableRemediation().branchConfiguration().branchNames().get(0));
        Assertions.assertEquals(AnnotateDefaultBranchState.ENABLED,
            response.iterator()
                .next()
                .properties()
                .actionableRemediation()
                .branchConfiguration()
                .annotateDefaultBranch());
        Assertions.assertEquals(InheritFromParentState.ENABLED,
            response.iterator().next().properties().actionableRemediation().inheritFromParentState());
    }
}
