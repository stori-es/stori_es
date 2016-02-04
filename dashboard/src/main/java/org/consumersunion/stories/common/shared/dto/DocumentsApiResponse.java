package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentsApiResponse extends AbstractApiResponse<DocumentResponse> {
    @Override
    @JsonProperty("documents")
    public List<DocumentResponse> getData() {
        return super.getData();
    }

    @Override
    @JsonProperty("documents")
    public void setData(List<DocumentResponse> data) {
        super.setData(data);
    }
}
