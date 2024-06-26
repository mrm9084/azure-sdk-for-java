// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.billing.models;

import com.azure.core.annotation.Immutable;
import com.azure.resourcemanager.billing.fluent.models.TransactionInner;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The list of transactions. */
@Immutable
public final class TransactionListResult {
    /*
     * The list of transactions.
     */
    @JsonProperty(value = "value", access = JsonProperty.Access.WRITE_ONLY)
    private List<TransactionInner> value;

    /*
     * Total number of records.
     */
    @JsonProperty(value = "totalCount", access = JsonProperty.Access.WRITE_ONLY)
    private Integer totalCount;

    /*
     * The link (url) to the next page of results.
     */
    @JsonProperty(value = "nextLink", access = JsonProperty.Access.WRITE_ONLY)
    private String nextLink;

    /** Creates an instance of TransactionListResult class. */
    public TransactionListResult() {
    }

    /**
     * Get the value property: The list of transactions.
     *
     * @return the value value.
     */
    public List<TransactionInner> value() {
        return this.value;
    }

    /**
     * Get the totalCount property: Total number of records.
     *
     * @return the totalCount value.
     */
    public Integer totalCount() {
        return this.totalCount;
    }

    /**
     * Get the nextLink property: The link (url) to the next page of results.
     *
     * @return the nextLink value.
     */
    public String nextLink() {
        return this.nextLink;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (value() != null) {
            value().forEach(e -> e.validate());
        }
    }
}
