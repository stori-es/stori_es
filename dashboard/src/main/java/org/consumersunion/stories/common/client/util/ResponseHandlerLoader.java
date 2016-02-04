package org.consumersunion.stories.common.client.util;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.event.ShowLoadingEvent;
import org.consumersunion.stories.common.client.service.response.Response;

public abstract class ResponseHandlerLoader<R extends Response> extends ResponseHandler<R> {
    public ResponseHandlerLoader() {
        ShowLoadingEvent.fire(ResponseHandlerLoader.this);
    }

    @Override
    public void onFailure(Throwable e) {
        HideLoadingEvent.fire(this);

        super.onFailure(e);
    }

    @Override
    public void onSuccess(final R result) {
        HideLoadingEvent.fire(this);

        super.onSuccess(result);
    }
}
