package org.consumersunion.stories.common.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = Shape.STRING)
public enum ContactType {
    EMAIL("EmailContact"),
    PHONE("PhoneContact"),
    GEOLOCATION("GeolocationContact"),
    SOCIAL("SocialContact");

    private final String value;

    ContactType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getJsonRepresentation() {
        return value;
    }

    @JsonCreator
    public static ContactType create(String value) {
        for (ContactType contactType : values()) {
            if (contactType.value.equals(value)) {
                return contactType;
            }
        }

        return null;
    }
}
