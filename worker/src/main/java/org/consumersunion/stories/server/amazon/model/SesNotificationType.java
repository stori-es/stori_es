package org.consumersunion.stories.server.amazon.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SesNotificationType {
    BOUNCE("Bounce"),
    COMPLAINT("Complaint"),
    DELIVERY("Delivery");

    @JsonCreator
    public static SesNotificationType fromValue(String value) {
        return SesNotificationType.valueOf(value.toUpperCase());
    }

    private final String value;

    SesNotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
