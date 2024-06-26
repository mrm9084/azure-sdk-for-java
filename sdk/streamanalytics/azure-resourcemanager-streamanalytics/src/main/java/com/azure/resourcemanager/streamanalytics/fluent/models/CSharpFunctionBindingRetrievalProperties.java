// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.streamanalytics.fluent.models;

import com.azure.core.annotation.Fluent;
import com.azure.resourcemanager.streamanalytics.models.UdfType;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The binding retrieval properties associated with a CSharp function.
 */
@Fluent
public final class CSharpFunctionBindingRetrievalProperties {
    /*
     * The CSharp code containing a single function definition.
     */
    @JsonProperty(value = "script")
    private String script;

    /*
     * The function type.
     */
    @JsonProperty(value = "udfType")
    private UdfType udfType;

    /**
     * Creates an instance of CSharpFunctionBindingRetrievalProperties class.
     */
    public CSharpFunctionBindingRetrievalProperties() {
    }

    /**
     * Get the script property: The CSharp code containing a single function definition.
     * 
     * @return the script value.
     */
    public String script() {
        return this.script;
    }

    /**
     * Set the script property: The CSharp code containing a single function definition.
     * 
     * @param script the script value to set.
     * @return the CSharpFunctionBindingRetrievalProperties object itself.
     */
    public CSharpFunctionBindingRetrievalProperties withScript(String script) {
        this.script = script;
        return this;
    }

    /**
     * Get the udfType property: The function type.
     * 
     * @return the udfType value.
     */
    public UdfType udfType() {
        return this.udfType;
    }

    /**
     * Set the udfType property: The function type.
     * 
     * @param udfType the udfType value to set.
     * @return the CSharpFunctionBindingRetrievalProperties object itself.
     */
    public CSharpFunctionBindingRetrievalProperties withUdfType(UdfType udfType) {
        this.udfType = udfType;
        return this;
    }

    /**
     * Validates the instance.
     * 
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
    }
}
