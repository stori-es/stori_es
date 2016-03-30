package org.consumersunion.stories.common.shared.model;

import java.io.Serializable;

import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.IsSerializable;

public enum Locale implements Serializable, IsSerializable {
    UNKNOWN(""),
    ENGLISH("en"),
    SPANISH("es");

    private final String code;

    Locale(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Locale fromCode(String code) {
        for (Locale locale : values()) {
            if (locale.code.equals(Strings.nullToEmpty(code).toLowerCase())) {
                return locale;
            }
        }

        return Locale.UNKNOWN;
    }
}
