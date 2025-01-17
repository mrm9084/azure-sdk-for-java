// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.alertsmanagement.models;

import com.azure.core.annotation.Fluent;
import com.azure.json.JsonReader;
import com.azure.json.JsonSerializable;
import com.azure.json.JsonToken;
import com.azure.json.JsonWriter;
import com.azure.resourcemanager.alertsmanagement.fluent.models.AlertInner;
import java.io.IOException;
import java.util.List;

/**
 * List the alerts.
 */
@Fluent
public final class AlertsList implements JsonSerializable<AlertsList> {
    /*
     * URL to fetch the next set of alerts.
     */
    private String nextLink;

    /*
     * List of alerts
     */
    private List<AlertInner> value;

    /**
     * Creates an instance of AlertsList class.
     */
    public AlertsList() {
    }

    /**
     * Get the nextLink property: URL to fetch the next set of alerts.
     * 
     * @return the nextLink value.
     */
    public String nextLink() {
        return this.nextLink;
    }

    /**
     * Set the nextLink property: URL to fetch the next set of alerts.
     * 
     * @param nextLink the nextLink value to set.
     * @return the AlertsList object itself.
     */
    public AlertsList withNextLink(String nextLink) {
        this.nextLink = nextLink;
        return this;
    }

    /**
     * Get the value property: List of alerts.
     * 
     * @return the value value.
     */
    public List<AlertInner> value() {
        return this.value;
    }

    /**
     * Set the value property: List of alerts.
     * 
     * @param value the value value to set.
     * @return the AlertsList object itself.
     */
    public AlertsList withValue(List<AlertInner> value) {
        this.value = value;
        return this;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonWriter toJson(JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeStartObject();
        jsonWriter.writeStringField("nextLink", this.nextLink);
        jsonWriter.writeArrayField("value", this.value, (writer, element) -> writer.writeJson(element));
        return jsonWriter.writeEndObject();
    }

    /**
     * Reads an instance of AlertsList from the JsonReader.
     * 
     * @param jsonReader The JsonReader being read.
     * @return An instance of AlertsList if the JsonReader was pointing to an instance of it, or null if it was pointing
     * to JSON null.
     * @throws IOException If an error occurs while reading the AlertsList.
     */
    public static AlertsList fromJson(JsonReader jsonReader) throws IOException {
        return jsonReader.readObject(reader -> {
            AlertsList deserializedAlertsList = new AlertsList();
            while (reader.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = reader.getFieldName();
                reader.nextToken();

                if ("nextLink".equals(fieldName)) {
                    deserializedAlertsList.nextLink = reader.getString();
                } else if ("value".equals(fieldName)) {
                    List<AlertInner> value = reader.readArray(reader1 -> AlertInner.fromJson(reader1));
                    deserializedAlertsList.value = value;
                } else {
                    reader.skipChildren();
                }
            }

            return deserializedAlertsList;
        });
    }
}
