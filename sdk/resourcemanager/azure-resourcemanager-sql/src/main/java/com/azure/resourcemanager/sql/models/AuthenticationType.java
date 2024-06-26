// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.resourcemanager.sql.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Defines values for AuthenticationType. */
public enum AuthenticationType {
    /** Enum value SQL. */
    SQL("SQL"),

    /** Enum value ADPassword. */
    ADPASSWORD("ADPassword");

    /** The actual serialized value for a AuthenticationType instance. */
    private final String value;

    AuthenticationType(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a AuthenticationType instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed AuthenticationType object, or null if unable to parse.
     */
    @JsonCreator
    public static AuthenticationType fromString(String value) {
        AuthenticationType[] items = AuthenticationType.values();
        for (AuthenticationType item : items) {
            if (item.toString().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
