package org.consumersunion.stories.dashboard.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class AttachmentChangedEvent extends GwtEvent<AttachmentChangedEvent.AttachmentChangedHandler> {
    public interface AttachmentChangedHandler extends EventHandler {
        void onAttachmentChanged(AttachmentChangedEvent event);
    }

    public static final Type<AttachmentChangedHandler> TYPE = new Type<AttachmentChangedHandler>();

    private AttachmentChangedEvent() {
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new AttachmentChangedEvent());
    }

    @Override
    public Type<AttachmentChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AttachmentChangedHandler handler) {
        handler.onAttachmentChanged(this);
    }
}
