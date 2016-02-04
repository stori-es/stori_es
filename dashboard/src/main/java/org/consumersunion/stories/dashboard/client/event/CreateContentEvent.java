package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.document.Document;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Event used to notify the container that a new content was created
 */
public class CreateContentEvent extends GwtEvent<CreateContentEvent.CreateNewContentHandler> {
    public interface CreateNewContentHandler extends EventHandler {
        void onCreateNewContent(CreateContentEvent event);
    }

    public static final Type<CreateNewContentHandler> TYPE = new Type<CreateNewContentHandler>();

    private final Document document;
    private final ContentKind contentKind;

    private CreateContentEvent(Document document, ContentKind contentKind) {
        this.document = document;
        this.contentKind = contentKind;
    }

    public static void fire(HasHandlers source, Document document, ContentKind contentKind) {
        source.fireEvent(new CreateContentEvent(document, contentKind));
    }

    public ContentKind getContentKind() {
        return contentKind;
    }

    public Document getDocument() {
        return document;
    }

    @Override
    public Type<CreateNewContentHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CreateNewContentHandler handler) {
        handler.onCreateNewContent(this);
    }
}
