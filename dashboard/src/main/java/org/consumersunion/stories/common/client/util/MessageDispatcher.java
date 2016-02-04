package org.consumersunion.stories.common.client.util;

import org.consumersunion.stories.common.client.event.AlertMessageEvent;
import org.consumersunion.stories.common.client.widget.MessageStyle;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class MessageDispatcher implements HasHandlers {
    private final EventBus eventBus;

    @Inject
    MessageDispatcher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void displayMessage(MessageStyle type, String content) {
        AlertMessageEvent.fire(this, type, content);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }
}
