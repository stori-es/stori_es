package org.consumersunion.stories.common.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ApiLocale {
    EN_US("en-US"),
    ES_US("es-US");

    private final String localeString;

    ApiLocale(String localeString) {
        this.localeString = localeString;
    }

    @JsonCreator
    public static ApiLocale parse(String value) {
        for (ApiLocale apiLocale : values()) {
            if (apiLocale.localeString.equals(value)) {
                return apiLocale;
            }
        }

        throw new IllegalArgumentException();
    }

    @JsonValue
    public String getLocaleString() {
        return localeString;
    }
}
