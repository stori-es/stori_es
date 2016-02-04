package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StoriesApiResponse extends AbstractApiResponse<StoryResponse> {
    @Override
    @JsonProperty("stories")
    public List<StoryResponse> getData() {
        return super.getData();
    }

    @Override
    @JsonProperty("stories")
    public void setData(List<StoryResponse> data) {
        super.setData(data);
    }
}
