package org.consumersunion.stories.common.shared.dto.blocks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiBlockConstraints {
    private boolean required;
    @JsonProperty("minimum_length")
    private Integer minimumLength;
    @JsonProperty("maximum_length")
    private Integer maximumLength;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Integer getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(Integer minimumLength) {
        this.minimumLength = minimumLength;
    }

    public Integer getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(Integer maximumLength) {
        this.maximumLength = maximumLength;
    }
}
