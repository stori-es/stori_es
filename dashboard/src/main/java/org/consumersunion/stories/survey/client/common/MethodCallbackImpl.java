package org.consumersunion.stories.survey.client.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public abstract class MethodCallbackImpl<T> implements MethodCallback<T>, HasHandlers {

    private static final Logger logger = Logger.getLogger(MethodCallbackImpl.class.getName());

    @Inject
    protected static EventBus eventBus;

    @Override
    public void onFailure(Method method, Throwable throwable) {
        logger.log(Level.SEVERE, throwable.getMessage());
    }

    @Override
    public void onSuccess(Method method, T response) {
        handleSuccess(response);
    }

    public abstract void handleSuccess(T result);

    @Override
    public void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }
}
