package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsersApiResponse extends AbstractApiResponse<UserResponse> {
    @Override
    @JsonProperty("users")
    public List<UserResponse> getData() {
        return super.getData();
    }

    @Override
    @JsonProperty("users")
    public void setData(List<UserResponse> data) {
        super.setData(data);
    }

}
