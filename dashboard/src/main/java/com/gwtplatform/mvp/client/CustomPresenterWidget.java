package com.gwtplatform.mvp.client;

import com.google.web.bindery.event.shared.EventBus;

public abstract class CustomPresenterWidget<V extends View> extends PresenterWidget<V> {
    public CustomPresenterWidget(boolean autoBind, EventBus eventBus, V view) {
        super(autoBind, eventBus, view);
    }

    public CustomPresenterWidget(EventBus eventBus, V view) {
        super(eventBus, view);
    }

    public void forceReveal() {
        if (!isVisible()) {
            internalReveal();
        }
    }
}
