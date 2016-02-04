package org.consumersunion.stories.dashboard.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CancelNewDocumentEvent extends GwtEvent<CancelNewDocumentEvent.CancelNewDocumentHandler> {
    public interface CancelNewDocumentHandler extends EventHandler {
        void onCancelNewDocument(CancelNewDocumentEvent event);
    }

    public static final Type<CancelNewDocumentHandler> TYPE = new Type<CancelNewDocumentHandler>();

    private CancelNewDocumentEvent() {
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new CancelNewDocumentEvent());
    }

    @Override
    public Type<CancelNewDocumentHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CancelNewDocumentHandler handler) {
        handler.onCancelNewDocument(this);
    }
}
