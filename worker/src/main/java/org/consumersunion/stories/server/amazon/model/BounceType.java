package org.consumersunion.stories.server.amazon.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import static org.consumersunion.stories.server.amazon.model.BounceSubType.ATTACHMENT_REJECTED;
import static org.consumersunion.stories.server.amazon.model.BounceSubType.CONTENT_REJECTED;
import static org.consumersunion.stories.server.amazon.model.BounceSubType.GENERAL;
import static org.consumersunion.stories.server.amazon.model.BounceSubType.MAILBOX_FULL;
import static org.consumersunion.stories.server.amazon.model.BounceSubType.MESSAGE_TOO_LARGE;
import static org.consumersunion.stories.server.amazon.model.BounceSubType.NO_EMAIL;
import static org.consumersunion.stories.server.amazon.model.BounceSubType.SUPPRESSED;

public enum BounceType {
    UNDETERMINED("Undetermined", new BounceSubType[]{BounceSubType.UNDETERMINED}),
    PERMANENT("Permanent", new BounceSubType[]{GENERAL, NO_EMAIL, SUPPRESSED}),
    TRANSIENT("Transient",
            new BounceSubType[]{GENERAL, MAILBOX_FULL, MESSAGE_TOO_LARGE, CONTENT_REJECTED, ATTACHMENT_REJECTED});

    @JsonCreator
    public static BounceType fromValue(String value) {
        return BounceType.valueOf(value.toUpperCase());
    }

    private final String value;
    private final BounceSubType[] bounceSubTypes;

    BounceType(String value, BounceSubType[] bounceSubTypes) {
        this.value = value;
        this.bounceSubTypes = bounceSubTypes;
    }

    public String getValue() {
        return value;
    }

    public BounceSubType[] getSubTypes() {
        return bounceSubTypes;
    }
}
