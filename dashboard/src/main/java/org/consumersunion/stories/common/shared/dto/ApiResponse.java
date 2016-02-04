package org.consumersunion.stories.common.shared.dto;

import java.util.List;

public interface ApiResponse<T extends BasicResponse> {
    Metadata getMetadata();

    void setMetadata(Metadata metadata);

    List<T> getData();

    void setData(List<T> data);
}
