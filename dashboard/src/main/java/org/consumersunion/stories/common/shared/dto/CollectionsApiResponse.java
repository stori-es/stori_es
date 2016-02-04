package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CollectionsApiResponse extends AbstractApiResponse<CollectionResponse> {
    @Override
    @JsonProperty("collections")
    public List<CollectionResponse> getData() {
        return super.getData();
    }

    @Override
    @JsonProperty("collections")
    public void setData(List<CollectionResponse> data) {
        super.setData(data);
    }
}
