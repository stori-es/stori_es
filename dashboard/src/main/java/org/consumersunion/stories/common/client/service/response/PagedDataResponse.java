package org.consumersunion.stories.common.client.service.response;

public class PagedDataResponse<T> extends DataResponse<T> implements PagedResponse {
    private int totalCount;
    private int start;

    public PagedDataResponse() {
        super();
    }

    public PagedDataResponse(Response template) {
        super(template);
    }

    @Override
    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public void setTotalCount(final int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(final int start) {
        this.start = start;
    }
}
