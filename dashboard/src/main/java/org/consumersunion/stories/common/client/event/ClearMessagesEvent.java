package org.consumersunion.stories.common.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ClearMessagesEvent extends GwtEvent<ClearMessagesEvent.ClearMessagesHandler> {
    public interface ClearMessagesHandler extends EventHandler {
        void onClearMessages(ClearMessagesEvent event);
    }

    public static final Type<ClearMessagesHandler> TYPE = new Type<ClearMessagesHandler>();

    private ClearMessagesEvent() {
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new ClearMessagesEvent());
    }

    @Override
    public Type<ClearMessagesHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ClearMessagesHandler handler) {
        handler.onClearMessages(this);
    }
}
