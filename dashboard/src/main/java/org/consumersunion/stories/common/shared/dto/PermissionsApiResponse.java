package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PermissionsApiResponse extends AbstractApiResponse<PermissionResponse> {
    @Override
    @JsonProperty("permissions")
    public List<PermissionResponse> getData() {
        return super.getData();
    }

    @Override
    @JsonProperty("permissions")
    public void setData(List<PermissionResponse> data) {
        super.setData(data);
    }
}
