// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.agrifood.models;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;

/**
 * Resource collection API of FarmBeatsModels.
 */
public interface FarmBeatsModels {
    /**
     * Get FarmBeats resource.
     * 
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param farmBeatsResourceName FarmBeats resource name.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return farmBeats resource along with {@link Response}.
     */
    Response<FarmBeats> getByResourceGroupWithResponse(String resourceGroupName, String farmBeatsResourceName,
        Context context);

    /**
     * Get FarmBeats resource.
     * 
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param farmBeatsResourceName FarmBeats resource name.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return farmBeats resource.
     */
    FarmBeats getByResourceGroup(String resourceGroupName, String farmBeatsResourceName);

    /**
     * Delete a FarmBeats resource.
     * 
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param farmBeatsResourceName FarmBeats resource name.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the {@link Response}.
     */
    Response<Void> deleteByResourceGroupWithResponse(String resourceGroupName, String farmBeatsResourceName,
        Context context);

    /**
     * Delete a FarmBeats resource.
     * 
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param farmBeatsResourceName FarmBeats resource name.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    void deleteByResourceGroup(String resourceGroupName, String farmBeatsResourceName);

    /**
     * Lists the FarmBeats instances for a subscription.
     * 
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return paged response contains list of requested objects and a URL link to get the next set of results as
     * paginated response with {@link PagedIterable}.
     */
    PagedIterable<FarmBeats> list();

    /**
     * Lists the FarmBeats instances for a subscription.
     * 
     * @param maxPageSize Maximum number of items needed (inclusive).
     * Minimum = 10, Maximum = 1000, Default value = 50.
     * @param skipToken Skip token for getting next set of results.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return paged response contains list of requested objects and a URL link to get the next set of results as
     * paginated response with {@link PagedIterable}.
     */
    PagedIterable<FarmBeats> list(Integer maxPageSize, String skipToken, Context context);

    /**
     * Lists the FarmBeats instances for a resource group.
     * 
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return paged response contains list of requested objects and a URL link to get the next set of results as
     * paginated response with {@link PagedIterable}.
     */
    PagedIterable<FarmBeats> listByResourceGroup(String resourceGroupName);

    /**
     * Lists the FarmBeats instances for a resource group.
     * 
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param maxPageSize Maximum number of items needed (inclusive).
     * Minimum = 10, Maximum = 1000, Default value = 50.
     * @param skipToken Continuation token for getting next set of results.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return paged response contains list of requested objects and a URL link to get the next set of results as
     * paginated response with {@link PagedIterable}.
     */
    PagedIterable<FarmBeats> listByResourceGroup(String resourceGroupName, Integer maxPageSize, String skipToken,
        Context context);

    /**
     * Get FarmBeats resource.
     * 
     * @param id the resource ID.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return farmBeats resource along with {@link Response}.
     */
    FarmBeats getById(String id);

    /**
     * Get FarmBeats resource.
     * 
     * @param id the resource ID.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return farmBeats resource along with {@link Response}.
     */
    Response<FarmBeats> getByIdWithResponse(String id, Context context);

    /**
     * Delete a FarmBeats resource.
     * 
     * @param id the resource ID.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    void deleteById(String id);

    /**
     * Delete a FarmBeats resource.
     * 
     * @param id the resource ID.
     * @param context The context to associate with this operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.management.exception.ManagementException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the {@link Response}.
     */
    Response<Void> deleteByIdWithResponse(String id, Context context);

    /**
     * Begins definition for a new FarmBeats resource.
     * 
     * @param name resource name.
     * @return the first stage of the new FarmBeats definition.
     */
    FarmBeats.DefinitionStages.Blank define(String name);
}
