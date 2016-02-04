package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.common.shared.model.document.Document;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class SavedDocumentEvent extends SavedEvent<Document, SavedDocumentEvent.SavedDocumentHandler> {
    public interface SavedDocumentHandler extends EventHandler {
        void onDocumentSaved(SavedDocumentEvent event);
    }

    public static final Type<SavedDocumentHandler> TYPE = new Type<SavedDocumentHandler>();

    private SavedDocumentEvent(Document data) {
        super(data);
    }

    public static void fire(HasHandlers source, Document data) {
        source.fireEvent(new SavedDocumentEvent(data));
    }

    @Override
    public Type<SavedDocumentHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SavedDocumentHandler handler) {
        handler.onDocumentSaved(this);
    }
}
