package org.consumersunion.stories.dashboard.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

abstract class SavedEvent<T, H extends EventHandler> extends GwtEvent<H> {
    private final T data;

    protected SavedEvent(T data) {
        this.data = data;
    }

    public T get() {
        return data;
    }
}
