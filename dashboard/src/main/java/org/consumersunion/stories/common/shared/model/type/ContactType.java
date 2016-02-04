package org.consumersunion.stories.common.shared.model.type;

import java.util.Arrays;
import java.util.List;

import org.consumersunion.stories.common.shared.model.document.BlockType;

public enum ContactType {
    HOME("Home"),
    MOBILE("Mobile"),
    OTHER("Other"),
    WORK("Work");

    private final String code;

    ContactType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static ContactType valueOfCode(String code) {
        for (ContactType contactType : values()) {
            if (contactType.code().equals(code)) {
                return contactType;
            }
        }

        return null;
    }

    public static ContactType fromBlockType(BlockType standardType) {
        switch (standardType) {
            case EMAIL:
            case PHONE:
                return HOME;
            case EMAIL_WORK:
            case PHONE_WORK:
                return WORK;
            case EMAIL_OTHER:
            case PHONE_OTHER:
                return OTHER;
            case PHONE_MOBILE:
                return MOBILE;
            default:
                return null;
        }
    }

    public static List<ContactType> getEmailValues() {
        return Arrays.asList(HOME, WORK, OTHER);
    }
}
