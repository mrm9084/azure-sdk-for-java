// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.chaos.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.azure.json.JsonReader;
import com.azure.json.JsonSerializable;
import com.azure.json.JsonToken;
import com.azure.json.JsonWriter;
import java.io.IOException;
import java.util.Map;

/**
 * The identity of a resource.
 */
@Fluent
public final class ResourceIdentity implements JsonSerializable<ResourceIdentity> {
    /*
     * String of the resource identity type.
     */
    private ResourceIdentityType type;

    /*
     * The list of user identities associated with the Experiment. The user identity dictionary key references will be
     * ARM resource ids in the form:
     * '/subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.ManagedIdentity/
     * userAssignedIdentities/{identityName}'.
     */
    private Map<String, UserAssignedIdentity> userAssignedIdentities;

    /*
     * GUID that represents the principal ID of this resource identity.
     */
    private String principalId;

    /*
     * GUID that represents the tenant ID of this resource identity.
     */
    private String tenantId;

    /**
     * Creates an instance of ResourceIdentity class.
     */
    public ResourceIdentity() {
    }

    /**
     * Get the type property: String of the resource identity type.
     * 
     * @return the type value.
     */
    public ResourceIdentityType type() {
        return this.type;
    }

    /**
     * Set the type property: String of the resource identity type.
     * 
     * @param type the type value to set.
     * @return the ResourceIdentity object itself.
     */
    public ResourceIdentity withType(ResourceIdentityType type) {
        this.type = type;
        return this;
    }

    /**
     * Get the userAssignedIdentities property: The list of user identities associated with the Experiment. The user
     * identity dictionary key references will be ARM resource ids in the form:
     * '/subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.ManagedIdentity/userAssignedIdentities/{identityName}'.
     * 
     * @return the userAssignedIdentities value.
     */
    public Map<String, UserAssignedIdentity> userAssignedIdentities() {
        return this.userAssignedIdentities;
    }

    /**
     * Set the userAssignedIdentities property: The list of user identities associated with the Experiment. The user
     * identity dictionary key references will be ARM resource ids in the form:
     * '/subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.ManagedIdentity/userAssignedIdentities/{identityName}'.
     * 
     * @param userAssignedIdentities the userAssignedIdentities value to set.
     * @return the ResourceIdentity object itself.
     */
    public ResourceIdentity withUserAssignedIdentities(Map<String, UserAssignedIdentity> userAssignedIdentities) {
        this.userAssignedIdentities = userAssignedIdentities;
        return this;
    }

    /**
     * Get the principalId property: GUID that represents the principal ID of this resource identity.
     * 
     * @return the principalId value.
     */
    public String principalId() {
        return this.principalId;
    }

    /**
     * Get the tenantId property: GUID that represents the tenant ID of this resource identity.
     * 
     * @return the tenantId value.
     */
    public String tenantId() {
        return this.tenantId;
    }

    /**
     * Validates the instance.
     * 
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (type() == null) {
            throw LOGGER.atError()
                .log(new IllegalArgumentException("Missing required property type in model ResourceIdentity"));
        }
        if (userAssignedIdentities() != null) {
            userAssignedIdentities().values().forEach(e -> {
                if (e != null) {
                    e.validate();
                }
            });
        }
    }

    private static final ClientLogger LOGGER = new ClientLogger(ResourceIdentity.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonWriter toJson(JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeStartObject();
        jsonWriter.writeStringField("type", this.type == null ? null : this.type.toString());
        jsonWriter.writeMapField("userAssignedIdentities", this.userAssignedIdentities,
            (writer, element) -> writer.writeJson(element));
        return jsonWriter.writeEndObject();
    }

    /**
     * Reads an instance of ResourceIdentity from the JsonReader.
     * 
     * @param jsonReader The JsonReader being read.
     * @return An instance of ResourceIdentity if the JsonReader was pointing to an instance of it, or null if it was
     * pointing to JSON null.
     * @throws IllegalStateException If the deserialized JSON object was missing any required properties.
     * @throws IOException If an error occurs while reading the ResourceIdentity.
     */
    public static ResourceIdentity fromJson(JsonReader jsonReader) throws IOException {
        return jsonReader.readObject(reader -> {
            ResourceIdentity deserializedResourceIdentity = new ResourceIdentity();
            while (reader.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = reader.getFieldName();
                reader.nextToken();

                if ("type".equals(fieldName)) {
                    deserializedResourceIdentity.type = ResourceIdentityType.fromString(reader.getString());
                } else if ("userAssignedIdentities".equals(fieldName)) {
                    Map<String, UserAssignedIdentity> userAssignedIdentities
                        = reader.readMap(reader1 -> UserAssignedIdentity.fromJson(reader1));
                    deserializedResourceIdentity.userAssignedIdentities = userAssignedIdentities;
                } else if ("principalId".equals(fieldName)) {
                    deserializedResourceIdentity.principalId = reader.getString();
                } else if ("tenantId".equals(fieldName)) {
                    deserializedResourceIdentity.tenantId = reader.getString();
                } else {
                    reader.skipChildren();
                }
            }

            return deserializedResourceIdentity;
        });
    }
}
