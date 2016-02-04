package org.consumersunion.stories.common.client.service.response;

public class DatumResponse<T> extends Response {
    protected T datum;

    public DatumResponse() {
        super();
    }

    public DatumResponse(Response template) {
        super(template);
    }

    public T getDatum() {
        return datum;
    }

    public void setDatum(final T datum) {
        this.datum = datum;
    }
}
