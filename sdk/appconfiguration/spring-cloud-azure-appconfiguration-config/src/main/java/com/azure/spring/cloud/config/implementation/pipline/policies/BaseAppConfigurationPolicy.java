// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.config.implementation.pipline.policies;

import static com.azure.spring.cloud.config.implementation.AppConfigurationConstants.USER_AGENT_TYPE;

import org.springframework.util.StringUtils;

import com.azure.core.http.HttpPipelineCallContext;
import com.azure.core.http.HttpPipelineNextPolicy;
import com.azure.core.http.HttpResponse;
import com.azure.core.http.policy.HttpPipelinePolicy;
import com.azure.spring.cloud.config.implementation.RequestTracingConstants;

import reactor.core.publisher.Mono;

/**
 * HttpPipelinePolicy for connecting to Azure App Configuration.
 */
public final class BaseAppConfigurationPolicy implements HttpPipelinePolicy {

    /**
     * Library Package name
     */
    private static final String PACKAGE_NAME = BaseAppConfigurationPolicy.class.getPackage().getImplementationTitle();

    /**
     * Format of User Agent
     */
    public static final String USER_AGENT = String.format("%s/%s", StringUtils.replace(PACKAGE_NAME, " ", ""),
        BaseAppConfigurationPolicy.class.getPackage().getImplementationVersion());

    static Boolean watchRequests = false;

    private static TracingInfo tracingInfo;

    /**
     * App Configuration Http Pipeline Policy
     * @param isDev is using dev profile
     * @param isKeyVaultConfigured is key vault configured
     * @param replicaCount number of replicas being used. Should equal the number of endpoints minus one.
     */
    public BaseAppConfigurationPolicy(TracingInfo tracingInfo) {
        BaseAppConfigurationPolicy.tracingInfo = tracingInfo;
    }

    @Override
    public Mono<HttpResponse> process(HttpPipelineCallContext context, HttpPipelineNextPolicy next) {
        String sdkUserAgent = context.getHttpRequest().getHeaders().get(USER_AGENT_TYPE).getValue();
        context.getHttpRequest().getHeaders().set(USER_AGENT_TYPE, USER_AGENT + " " + sdkUserAgent);
        context.getHttpRequest().getHeaders().set(RequestTracingConstants.CORRELATION_CONTEXT_HEADER.toString(),
            tracingInfo.getValue(watchRequests));

        return next.process();
    }

    /**
     * @param watchRequests the watchRequests to set
     */
    public static void setWatchRequests(Boolean watchRequests) {
        BaseAppConfigurationPolicy.watchRequests = watchRequests;
    }

    public static TracingInfo getTracingInfo() {
        return BaseAppConfigurationPolicy.tracingInfo;
    }

}
