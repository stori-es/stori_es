package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfilesApiResponse extends AbstractApiResponse<ProfileResponse> {
    @Override
    @JsonProperty("profiles")
    public List<ProfileResponse> getData() {
        return super.getData();
    }

    @Override
    @JsonProperty("profiles")
    public void setData(List<ProfileResponse> data) {
        super.setData(data);
    }
}
