// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.devcenter.fluent.models;

import com.azure.core.annotation.Fluent;
import com.azure.json.JsonReader;
import com.azure.json.JsonSerializable;
import com.azure.json.JsonToken;
import com.azure.json.JsonWriter;
import com.azure.resourcemanager.devcenter.models.CatalogSyncType;
import com.azure.resourcemanager.devcenter.models.GitCatalog;
import java.io.IOException;
import java.util.Map;

/**
 * Properties of a catalog. These properties can be updated after the resource has been created.
 */
@Fluent
public class CatalogUpdateProperties implements JsonSerializable<CatalogUpdateProperties> {
    /*
     * Properties for a GitHub catalog type.
     */
    private GitCatalog gitHub;

    /*
     * Properties for an Azure DevOps catalog type.
     */
    private GitCatalog adoGit;

    /*
     * Indicates the type of sync that is configured for the catalog.
     */
    private CatalogSyncType syncType;

    /*
     * Resource tags.
     */
    private Map<String, String> tags;

    /**
     * Creates an instance of CatalogUpdateProperties class.
     */
    public CatalogUpdateProperties() {
    }

    /**
     * Get the gitHub property: Properties for a GitHub catalog type.
     * 
     * @return the gitHub value.
     */
    public GitCatalog gitHub() {
        return this.gitHub;
    }

    /**
     * Set the gitHub property: Properties for a GitHub catalog type.
     * 
     * @param gitHub the gitHub value to set.
     * @return the CatalogUpdateProperties object itself.
     */
    public CatalogUpdateProperties withGitHub(GitCatalog gitHub) {
        this.gitHub = gitHub;
        return this;
    }

    /**
     * Get the adoGit property: Properties for an Azure DevOps catalog type.
     * 
     * @return the adoGit value.
     */
    public GitCatalog adoGit() {
        return this.adoGit;
    }

    /**
     * Set the adoGit property: Properties for an Azure DevOps catalog type.
     * 
     * @param adoGit the adoGit value to set.
     * @return the CatalogUpdateProperties object itself.
     */
    public CatalogUpdateProperties withAdoGit(GitCatalog adoGit) {
        this.adoGit = adoGit;
        return this;
    }

    /**
     * Get the syncType property: Indicates the type of sync that is configured for the catalog.
     * 
     * @return the syncType value.
     */
    public CatalogSyncType syncType() {
        return this.syncType;
    }

    /**
     * Set the syncType property: Indicates the type of sync that is configured for the catalog.
     * 
     * @param syncType the syncType value to set.
     * @return the CatalogUpdateProperties object itself.
     */
    public CatalogUpdateProperties withSyncType(CatalogSyncType syncType) {
        this.syncType = syncType;
        return this;
    }

    /**
     * Get the tags property: Resource tags.
     * 
     * @return the tags value.
     */
    public Map<String, String> tags() {
        return this.tags;
    }

    /**
     * Set the tags property: Resource tags.
     * 
     * @param tags the tags value to set.
     * @return the CatalogUpdateProperties object itself.
     */
    public CatalogUpdateProperties withTags(Map<String, String> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Validates the instance.
     * 
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (gitHub() != null) {
            gitHub().validate();
        }
        if (adoGit() != null) {
            adoGit().validate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonWriter toJson(JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeStartObject();
        jsonWriter.writeJsonField("gitHub", this.gitHub);
        jsonWriter.writeJsonField("adoGit", this.adoGit);
        jsonWriter.writeStringField("syncType", this.syncType == null ? null : this.syncType.toString());
        jsonWriter.writeMapField("tags", this.tags, (writer, element) -> writer.writeString(element));
        return jsonWriter.writeEndObject();
    }

    /**
     * Reads an instance of CatalogUpdateProperties from the JsonReader.
     * 
     * @param jsonReader The JsonReader being read.
     * @return An instance of CatalogUpdateProperties if the JsonReader was pointing to an instance of it, or null if it
     * was pointing to JSON null.
     * @throws IOException If an error occurs while reading the CatalogUpdateProperties.
     */
    public static CatalogUpdateProperties fromJson(JsonReader jsonReader) throws IOException {
        return jsonReader.readObject(reader -> {
            CatalogUpdateProperties deserializedCatalogUpdateProperties = new CatalogUpdateProperties();
            while (reader.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = reader.getFieldName();
                reader.nextToken();

                if ("gitHub".equals(fieldName)) {
                    deserializedCatalogUpdateProperties.gitHub = GitCatalog.fromJson(reader);
                } else if ("adoGit".equals(fieldName)) {
                    deserializedCatalogUpdateProperties.adoGit = GitCatalog.fromJson(reader);
                } else if ("syncType".equals(fieldName)) {
                    deserializedCatalogUpdateProperties.syncType = CatalogSyncType.fromString(reader.getString());
                } else if ("tags".equals(fieldName)) {
                    Map<String, String> tags = reader.readMap(reader1 -> reader1.getString());
                    deserializedCatalogUpdateProperties.tags = tags;
                } else {
                    reader.skipChildren();
                }
            }

            return deserializedCatalogUpdateProperties;
        });
    }
}
