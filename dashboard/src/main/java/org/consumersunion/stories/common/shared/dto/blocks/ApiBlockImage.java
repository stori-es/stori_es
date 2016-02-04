package org.consumersunion.stories.common.shared.dto.blocks;

import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockStylePosition;
import org.consumersunion.stories.common.shared.dto.blocks.BlockConstants.ApiBlockStyleSize;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiBlockImage {
    private String href;
    @JsonProperty("horizontal_position")
    private ApiBlockStylePosition horizontalPosition;
    private ApiBlockStyleSize size;
    private String caption;
    @JsonProperty("alt_text")
    private String altText;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }
}
