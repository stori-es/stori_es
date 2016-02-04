package org.consumersunion.stories.common.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResetPasswordRequest {
    public static final int MIN_LENGTH = 6;

    private final String code;
    private final String password;
    private final String confirmPassword;

    @JsonCreator
    public ResetPasswordRequest(
            @JsonProperty("code") String code,
            @JsonProperty("password") String password,
            @JsonProperty("confirmPassword") String confirmPassword) {
        this.code = code;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getCode() {
        return code;
    }
}
