// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.storagepool.models;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.util.Context;

/**
 * Resource collection API of ResourceSkus.
 */
public interface ResourceSkus {
    /**
     * Lists available StoragePool resources and skus in an Azure location.
     * 
     * @param location The location of the resource.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return list Disk Pool skus operation response as paginated response with {@link PagedIterable}.
     */
    PagedIterable<ResourceSkuInfo> list(String location);

    /**
     * Lists available StoragePool resources and skus in an Azure location.
     * 
     * @param location The location of the resource.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return list Disk Pool skus operation response as paginated response with {@link PagedIterable}.
     */
    PagedIterable<ResourceSkuInfo> list(String location, Context context);
}
