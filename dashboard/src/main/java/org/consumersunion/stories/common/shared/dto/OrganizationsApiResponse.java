package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrganizationsApiResponse extends AbstractApiResponse<OrganizationResponse> {
    @Override
    @JsonProperty("organizations")
    public List<OrganizationResponse> getData() {
        return super.getData();
    }

    @Override
    @JsonProperty("organizations")
    public void setData(List<OrganizationResponse> data) {
        super.setData(data);
    }
}
