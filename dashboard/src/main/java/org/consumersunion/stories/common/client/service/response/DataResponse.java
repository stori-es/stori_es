package org.consumersunion.stories.common.client.service.response;

import java.util.List;

public class DataResponse<T> extends Response {
    protected List<T> data;

    public DataResponse() {
        super();
    }

    public DataResponse(Response template) {
        super(template);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(final List<T> data) {
        this.data = data;
    }
}
