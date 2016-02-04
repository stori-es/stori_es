package org.consumersunion.stories.common.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ResponseStatus {
    SUCCESS,
    FAIL,
    ERROR;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static ResponseStatus create(String value) {
        return valueOf(value.toUpperCase());
    }

    public static ResponseStatus fromCode(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) {
            return SUCCESS;
        } else if (statusCode < 400) {
            return FAIL;
        } else {
            return ERROR;
        }
    }
}
