package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Event used to notify that the user triggered the create/add document action
 */
public class AddNewDocumentEvent extends GwtEvent<AddNewDocumentEvent.AddNewDocumentHandler> {
    public interface AddNewDocumentHandler extends EventHandler {
        void onAddNewDocument(AddNewDocumentEvent event);
    }

    public static final Type<AddNewDocumentHandler> TYPE = new Type<AddNewDocumentHandler>();

    private final SystemEntityRelation entityRelation;

    private AddNewDocumentEvent(SystemEntityRelation entityRelation) {
        this.entityRelation = entityRelation;
    }

    public static void fire(HasHandlers source, SystemEntityRelation entityRelation) {
        source.fireEvent(new AddNewDocumentEvent(entityRelation));
    }

    @Override
    public Type<AddNewDocumentHandler> getAssociatedType() {
        return TYPE;
    }

    public SystemEntityRelation getEntityRelation() {
        return entityRelation;
    }

    @Override
    protected void dispatch(AddNewDocumentHandler handler) {
        handler.onAddNewDocument(this);
    }
}
