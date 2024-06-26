// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.azurestackhci.implementation;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.Response;
import com.azure.core.http.rest.SimpleResponse;
import com.azure.core.util.Context;
import com.azure.core.util.logging.ClientLogger;
import com.azure.resourcemanager.azurestackhci.fluent.HybridIdentityMetadatasClient;
import com.azure.resourcemanager.azurestackhci.fluent.models.HybridIdentityMetadataInner;
import com.azure.resourcemanager.azurestackhci.models.HybridIdentityMetadata;
import com.azure.resourcemanager.azurestackhci.models.HybridIdentityMetadatas;

public final class HybridIdentityMetadatasImpl implements HybridIdentityMetadatas {
    private static final ClientLogger LOGGER = new ClientLogger(HybridIdentityMetadatasImpl.class);

    private final HybridIdentityMetadatasClient innerClient;

    private final com.azure.resourcemanager.azurestackhci.AzureStackHciManager serviceManager;

    public HybridIdentityMetadatasImpl(
        HybridIdentityMetadatasClient innerClient,
        com.azure.resourcemanager.azurestackhci.AzureStackHciManager serviceManager) {
        this.innerClient = innerClient;
        this.serviceManager = serviceManager;
    }

    public Response<HybridIdentityMetadata> getWithResponse(String resourceUri, Context context) {
        Response<HybridIdentityMetadataInner> inner = this.serviceClient().getWithResponse(resourceUri, context);
        if (inner != null) {
            return new SimpleResponse<>(
                inner.getRequest(),
                inner.getStatusCode(),
                inner.getHeaders(),
                new HybridIdentityMetadataImpl(inner.getValue(), this.manager()));
        } else {
            return null;
        }
    }

    public HybridIdentityMetadata get(String resourceUri) {
        HybridIdentityMetadataInner inner = this.serviceClient().get(resourceUri);
        if (inner != null) {
            return new HybridIdentityMetadataImpl(inner, this.manager());
        } else {
            return null;
        }
    }

    public PagedIterable<HybridIdentityMetadata> list(String resourceUri) {
        PagedIterable<HybridIdentityMetadataInner> inner = this.serviceClient().list(resourceUri);
        return Utils.mapPage(inner, inner1 -> new HybridIdentityMetadataImpl(inner1, this.manager()));
    }

    public PagedIterable<HybridIdentityMetadata> list(String resourceUri, Context context) {
        PagedIterable<HybridIdentityMetadataInner> inner = this.serviceClient().list(resourceUri, context);
        return Utils.mapPage(inner, inner1 -> new HybridIdentityMetadataImpl(inner1, this.manager()));
    }

    private HybridIdentityMetadatasClient serviceClient() {
        return this.innerClient;
    }

    private com.azure.resourcemanager.azurestackhci.AzureStackHciManager manager() {
        return this.serviceManager;
    }
}
