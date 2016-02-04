package org.consumersunion.stories.common.client.service.response;

public interface PagedResponse {
    int getTotalCount();

    void setTotalCount(final int totalCount);

    int getStart();

    void setStart(final int start);
}
