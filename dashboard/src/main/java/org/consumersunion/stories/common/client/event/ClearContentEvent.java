package org.consumersunion.stories.common.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ClearContentEvent extends GwtEvent<ClearContentEvent.ClearContentHandler> {
    public interface ClearContentHandler extends EventHandler {
        void onClearContent(ClearContentEvent event);
    }

    public static final Type<ClearContentHandler> TYPE = new Type<ClearContentHandler>();

    private ClearContentEvent() {
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new ClearContentEvent());
    }

    @Override
    public Type<ClearContentHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ClearContentHandler handler) {
        handler.onClearContent(this);
    }
}
