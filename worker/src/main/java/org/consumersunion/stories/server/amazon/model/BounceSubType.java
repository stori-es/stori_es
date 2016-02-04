package org.consumersunion.stories.server.amazon.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BounceSubType {
    UNDETERMINED("Undetermined"),
    GENERAL("General"),
    NO_EMAIL("NoEmail"),
    SUPPRESSED("Suppressed"),
    MAILBOX_FULL("MailboxFull"),
    MESSAGE_TOO_LARGE("MessageTooLarge"),
    CONTENT_REJECTED("ContentRejected"),
    ATTACHMENT_REJECTED("AttachmentRejected");

    @JsonCreator
    public static BounceSubType fromValue(String value) {
        for (BounceSubType bounceSubType : values()) {
            if (bounceSubType.getValue().equals(value)) {
                return bounceSubType;
            }
        }

        return BounceSubType.UNDETERMINED;
    }

    private final String value;

    BounceSubType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
