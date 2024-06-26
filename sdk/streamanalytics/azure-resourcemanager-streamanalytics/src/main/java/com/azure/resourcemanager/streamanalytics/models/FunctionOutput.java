// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.streamanalytics.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes the output of a function.
 */
@Fluent
public final class FunctionOutput {
    /*
     * The (Azure Stream Analytics supported) data type of the function output. A list of valid Azure Stream Analytics
     * data types are described at https://msdn.microsoft.com/en-us/library/azure/dn835065.aspx
     */
    @JsonProperty(value = "dataType")
    private String dataType;

    /**
     * Creates an instance of FunctionOutput class.
     */
    public FunctionOutput() {
    }

    /**
     * Get the dataType property: The (Azure Stream Analytics supported) data type of the function output. A list of
     * valid Azure Stream Analytics data types are described at
     * https://msdn.microsoft.com/en-us/library/azure/dn835065.aspx.
     * 
     * @return the dataType value.
     */
    public String dataType() {
        return this.dataType;
    }

    /**
     * Set the dataType property: The (Azure Stream Analytics supported) data type of the function output. A list of
     * valid Azure Stream Analytics data types are described at
     * https://msdn.microsoft.com/en-us/library/azure/dn835065.aspx.
     * 
     * @param dataType the dataType value to set.
     * @return the FunctionOutput object itself.
     */
    public FunctionOutput withDataType(String dataType) {
        this.dataType = dataType;
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
