package org.consumersunion.stories.common.client.api;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.util.MessageDispatcher;

import com.google.gwt.http.client.Response;
import com.gwtplatform.dispatch.rest.client.RestCallback;

public class RestCallbackAdapter<R> implements RestCallback<R> {
    @Inject
    protected static MessageDispatcher messageDispatcher;

    @Override
    public void setResponse(Response response) {
    }

    @Override
    public void onFailure(Throwable caught) {
    }

    @Override
    public void onSuccess(R result) {
    }
}
