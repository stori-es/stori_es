package org.consumersunion.stories.common.client.util;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class EventBusWrapper implements HasHandlers {
    private final EventBus eventBus;

    @Inject
    EventBusWrapper(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }
}
