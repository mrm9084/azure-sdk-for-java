// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) TypeSpec Code Generator.
package com.azure.compute.batch.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.annotation.Generated;
import com.azure.json.JsonReader;
import com.azure.json.JsonSerializable;
import com.azure.json.JsonToken;
import com.azure.json.JsonWriter;
import java.io.IOException;

/**
 * A single file or multiple files to be downloaded to a Compute Node.
 */
@Fluent
public final class ResourceFile implements JsonSerializable<ResourceFile> {

    /*
     * The storage container name in the auto storage Account. The autoStorageContainerName, storageContainerUrl and httpUrl properties are mutually exclusive and one of them must be specified.
     */
    @Generated
    private String autoStorageContainerName;

    /*
     * The URL of the blob container within Azure Blob Storage. The autoStorageContainerName, storageContainerUrl and httpUrl properties are mutually exclusive and one of them must be specified. This URL must be readable and listable from compute nodes. There are three ways to get such a URL for a container in Azure storage: include a Shared Access Signature (SAS) granting read and list permissions on the container, use a managed identity with read and list permissions, or set the ACL for the container to allow public access.
     */
    @Generated
    private String storageContainerUrl;

    /*
     * The URL of the file to download. The autoStorageContainerName, storageContainerUrl and httpUrl properties are mutually exclusive and one of them must be specified. If the URL points to Azure Blob Storage, it must be readable from compute nodes. There are three ways to get such a URL for a blob in Azure storage: include a Shared Access Signature (SAS) granting read permissions on the blob, use a managed identity with read permission, or set the ACL for the blob or its container to allow public access.
     */
    @Generated
    private String httpUrl;

    /*
     * The blob prefix to use when downloading blobs from an Azure Storage container. Only the blobs whose names begin with the specified prefix will be downloaded. The property is valid only when autoStorageContainerName or storageContainerUrl is used. This prefix can be a partial filename or a subdirectory. If a prefix is not specified, all the files in the container will be downloaded.
     */
    @Generated
    private String blobPrefix;

    /*
     * The location on the Compute Node to which to download the file(s), relative to the Task's working directory. If the httpUrl property is specified, the filePath is required and describes the path which the file will be downloaded to, including the filename. Otherwise, if the autoStorageContainerName or storageContainerUrl property is specified, filePath is optional and is the directory to download the files to. In the case where filePath is used as a directory, any directory structure already associated with the input data will be retained in full and appended to the specified filePath directory. The specified relative path cannot break out of the Task's working directory (for example by using '..').
     */
    @Generated
    private String filePath;

    /*
     * The file permission mode attribute in octal format. This property applies only to files being downloaded to Linux Compute Nodes. It will be ignored if it is specified for a resourceFile which will be downloaded to a Windows Compute Node. If this property is not specified for a Linux Compute Node, then a default value of 0770 is applied to the file.
     */
    @Generated
    private String fileMode;

    /*
     * The reference to the user assigned identity to use to access Azure Blob Storage specified by storageContainerUrl or httpUrl.
     */
    @Generated
    private BatchNodeIdentityReference identityReference;

    /**
     * Creates an instance of ResourceFile class.
     */
    @Generated
    public ResourceFile() {
    }

    /**
     * Get the autoStorageContainerName property: The storage container name in the auto storage Account. The
     * autoStorageContainerName, storageContainerUrl and httpUrl properties are mutually exclusive and one of them must
     * be specified.
     *
     * @return the autoStorageContainerName value.
     */
    @Generated
    public String getAutoStorageContainerName() {
        return this.autoStorageContainerName;
    }

    /**
     * Set the autoStorageContainerName property: The storage container name in the auto storage Account. The
     * autoStorageContainerName, storageContainerUrl and httpUrl properties are mutually exclusive and one of them must
     * be specified.
     *
     * @param autoStorageContainerName the autoStorageContainerName value to set.
     * @return the ResourceFile object itself.
     */
    @Generated
    public ResourceFile setAutoStorageContainerName(String autoStorageContainerName) {
        this.autoStorageContainerName = autoStorageContainerName;
        return this;
    }

    /**
     * Get the storageContainerUrl property: The URL of the blob container within Azure Blob Storage. The
     * autoStorageContainerName, storageContainerUrl and httpUrl properties are mutually exclusive and one of them must
     * be specified. This URL must be readable and listable from compute nodes. There are three ways to get such a URL
     * for a container in Azure storage: include a Shared Access Signature (SAS) granting read and list permissions on
     * the container, use a managed identity with read and list permissions, or set the ACL for the container to allow
     * public access.
     *
     * @return the storageContainerUrl value.
     */
    @Generated
    public String getStorageContainerUrl() {
        return this.storageContainerUrl;
    }

    /**
     * Set the storageContainerUrl property: The URL of the blob container within Azure Blob Storage. The
     * autoStorageContainerName, storageContainerUrl and httpUrl properties are mutually exclusive and one of them must
     * be specified. This URL must be readable and listable from compute nodes. There are three ways to get such a URL
     * for a container in Azure storage: include a Shared Access Signature (SAS) granting read and list permissions on
     * the container, use a managed identity with read and list permissions, or set the ACL for the container to allow
     * public access.
     *
     * @param storageContainerUrl the storageContainerUrl value to set.
     * @return the ResourceFile object itself.
     */
    @Generated
    public ResourceFile setStorageContainerUrl(String storageContainerUrl) {
        this.storageContainerUrl = storageContainerUrl;
        return this;
    }

    /**
     * Get the httpUrl property: The URL of the file to download. The autoStorageContainerName, storageContainerUrl and
     * httpUrl properties are mutually exclusive and one of them must be specified. If the URL points to Azure Blob
     * Storage, it must be readable from compute nodes. There are three ways to get such a URL for a blob in Azure
     * storage: include a Shared Access Signature (SAS) granting read permissions on the blob, use a managed identity
     * with read permission, or set the ACL for the blob or its container to allow public access.
     *
     * @return the httpUrl value.
     */
    @Generated
    public String getHttpUrl() {
        return this.httpUrl;
    }

    /**
     * Set the httpUrl property: The URL of the file to download. The autoStorageContainerName, storageContainerUrl and
     * httpUrl properties are mutually exclusive and one of them must be specified. If the URL points to Azure Blob
     * Storage, it must be readable from compute nodes. There are three ways to get such a URL for a blob in Azure
     * storage: include a Shared Access Signature (SAS) granting read permissions on the blob, use a managed identity
     * with read permission, or set the ACL for the blob or its container to allow public access.
     *
     * @param httpUrl the httpUrl value to set.
     * @return the ResourceFile object itself.
     */
    @Generated
    public ResourceFile setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
        return this;
    }

    /**
     * Get the blobPrefix property: The blob prefix to use when downloading blobs from an Azure Storage container. Only
     * the blobs whose names begin with the specified prefix will be downloaded. The property is valid only when
     * autoStorageContainerName or storageContainerUrl is used. This prefix can be a partial filename or a subdirectory.
     * If a prefix is not specified, all the files in the container will be downloaded.
     *
     * @return the blobPrefix value.
     */
    @Generated
    public String getBlobPrefix() {
        return this.blobPrefix;
    }

    /**
     * Set the blobPrefix property: The blob prefix to use when downloading blobs from an Azure Storage container. Only
     * the blobs whose names begin with the specified prefix will be downloaded. The property is valid only when
     * autoStorageContainerName or storageContainerUrl is used. This prefix can be a partial filename or a subdirectory.
     * If a prefix is not specified, all the files in the container will be downloaded.
     *
     * @param blobPrefix the blobPrefix value to set.
     * @return the ResourceFile object itself.
     */
    @Generated
    public ResourceFile setBlobPrefix(String blobPrefix) {
        this.blobPrefix = blobPrefix;
        return this;
    }

    /**
     * Get the filePath property: The location on the Compute Node to which to download the file(s), relative to the
     * Task's working directory. If the httpUrl property is specified, the filePath is required and describes the path
     * which the file will be downloaded to, including the filename. Otherwise, if the autoStorageContainerName or
     * storageContainerUrl property is specified, filePath is optional and is the directory to download the files to. In
     * the case where filePath is used as a directory, any directory structure already associated with the input data
     * will be retained in full and appended to the specified filePath directory. The specified relative path cannot
     * break out of the Task's working directory (for example by using '..').
     *
     * @return the filePath value.
     */
    @Generated
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * Set the filePath property: The location on the Compute Node to which to download the file(s), relative to the
     * Task's working directory. If the httpUrl property is specified, the filePath is required and describes the path
     * which the file will be downloaded to, including the filename. Otherwise, if the autoStorageContainerName or
     * storageContainerUrl property is specified, filePath is optional and is the directory to download the files to. In
     * the case where filePath is used as a directory, any directory structure already associated with the input data
     * will be retained in full and appended to the specified filePath directory. The specified relative path cannot
     * break out of the Task's working directory (for example by using '..').
     *
     * @param filePath the filePath value to set.
     * @return the ResourceFile object itself.
     */
    @Generated
    public ResourceFile setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    /**
     * Get the fileMode property: The file permission mode attribute in octal format. This property applies only to
     * files being downloaded to Linux Compute Nodes. It will be ignored if it is specified for a resourceFile which
     * will be downloaded to a Windows Compute Node. If this property is not specified for a Linux Compute Node, then a
     * default value of 0770 is applied to the file.
     *
     * @return the fileMode value.
     */
    @Generated
    public String getFileMode() {
        return this.fileMode;
    }

    /**
     * Set the fileMode property: The file permission mode attribute in octal format. This property applies only to
     * files being downloaded to Linux Compute Nodes. It will be ignored if it is specified for a resourceFile which
     * will be downloaded to a Windows Compute Node. If this property is not specified for a Linux Compute Node, then a
     * default value of 0770 is applied to the file.
     *
     * @param fileMode the fileMode value to set.
     * @return the ResourceFile object itself.
     */
    @Generated
    public ResourceFile setFileMode(String fileMode) {
        this.fileMode = fileMode;
        return this;
    }

    /**
     * Get the identityReference property: The reference to the user assigned identity to use to access Azure Blob
     * Storage specified by storageContainerUrl or httpUrl.
     *
     * @return the identityReference value.
     */
    @Generated
    public BatchNodeIdentityReference getIdentityReference() {
        return this.identityReference;
    }

    /**
     * Set the identityReference property: The reference to the user assigned identity to use to access Azure Blob
     * Storage specified by storageContainerUrl or httpUrl.
     *
     * @param identityReference the identityReference value to set.
     * @return the ResourceFile object itself.
     */
    @Generated
    public ResourceFile setIdentityReference(BatchNodeIdentityReference identityReference) {
        this.identityReference = identityReference;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Generated
    @Override
    public JsonWriter toJson(JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeStartObject();
        jsonWriter.writeStringField("autoStorageContainerName", this.autoStorageContainerName);
        jsonWriter.writeStringField("storageContainerUrl", this.storageContainerUrl);
        jsonWriter.writeStringField("httpUrl", this.httpUrl);
        jsonWriter.writeStringField("blobPrefix", this.blobPrefix);
        jsonWriter.writeStringField("filePath", this.filePath);
        jsonWriter.writeStringField("fileMode", this.fileMode);
        jsonWriter.writeJsonField("identityReference", this.identityReference);
        return jsonWriter.writeEndObject();
    }

    /**
     * Reads an instance of ResourceFile from the JsonReader.
     *
     * @param jsonReader The JsonReader being read.
     * @return An instance of ResourceFile if the JsonReader was pointing to an instance of it, or null if it was
     * pointing to JSON null.
     * @throws IOException If an error occurs while reading the ResourceFile.
     */
    @Generated
    public static ResourceFile fromJson(JsonReader jsonReader) throws IOException {
        return jsonReader.readObject(reader -> {
            ResourceFile deserializedResourceFile = new ResourceFile();
            while (reader.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = reader.getFieldName();
                reader.nextToken();
                if ("autoStorageContainerName".equals(fieldName)) {
                    deserializedResourceFile.autoStorageContainerName = reader.getString();
                } else if ("storageContainerUrl".equals(fieldName)) {
                    deserializedResourceFile.storageContainerUrl = reader.getString();
                } else if ("httpUrl".equals(fieldName)) {
                    deserializedResourceFile.httpUrl = reader.getString();
                } else if ("blobPrefix".equals(fieldName)) {
                    deserializedResourceFile.blobPrefix = reader.getString();
                } else if ("filePath".equals(fieldName)) {
                    deserializedResourceFile.filePath = reader.getString();
                } else if ("fileMode".equals(fieldName)) {
                    deserializedResourceFile.fileMode = reader.getString();
                } else if ("identityReference".equals(fieldName)) {
                    deserializedResourceFile.identityReference = BatchNodeIdentityReference.fromJson(reader);
                } else {
                    reader.skipChildren();
                }
            }
            return deserializedResourceFile;
        });
    }
}
