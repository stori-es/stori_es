package org.consumersunion.stories.common.shared.model.type;

public enum DataType {
    DATA_EMAIL("EMAIL"),
    DATA_PHONE_NUMBER("PHONE_NUMBER"),
    DATA_NAME("NAME"),
    DATA_ZIP("ZIP"),
    DATA_STRING("STRING");

    private final String code;

    DataType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
