package org.consumersunion.stories.common.shared.dto.blocks;

import com.fasterxml.jackson.annotation.JsonProperty;

import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockStylePosition;
import static org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockStyleSize;

public class ApiBlockStyles {
    @JsonProperty("horizontal_position")
    private ApiBlockStylePosition horizontalPosition;
    private ApiBlockStyleSize size;

    public ApiBlockStylePosition getHorizontalPosition() {
        return horizontalPosition;
    }

    public void setHorizontalPosition(ApiBlockStylePosition horizontalPosition) {
        this.horizontalPosition = horizontalPosition;
    }

    public ApiBlockStyleSize getSize() {
        return size;
    }

    public void setSize(ApiBlockStyleSize size) {
        this.size = size;
    }
}
