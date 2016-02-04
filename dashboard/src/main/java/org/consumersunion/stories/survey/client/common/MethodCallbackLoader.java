package org.consumersunion.stories.survey.client.common;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.event.ShowLoadingEvent;
import org.fusesource.restygwt.client.Method;

public abstract class MethodCallbackLoader<T> extends MethodCallbackImpl<T> {

    public MethodCallbackLoader() {
        ShowLoadingEvent.fire(this);
    }

    @Override
    public void onFailure(Method method, Throwable throwable) {
        HideLoadingEvent.fire(this);

        super.onFailure(method, throwable);
    }

    @Override
    public void onSuccess(Method method, T response) {
        HideLoadingEvent.fire(this);

        super.onSuccess(method, response);
    }
}
