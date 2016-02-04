package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThemesApiResponse extends AbstractApiResponse<ThemeResponse> {
    @Override
    @JsonProperty("themes")
    public List<ThemeResponse> getData() {
        return super.getData();
    }

    @Override
    @JsonProperty("themes")
    public void setData(List<ThemeResponse> data) {
        super.setData(data);
    }
}
