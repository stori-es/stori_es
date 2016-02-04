package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.common.shared.model.Collection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CollectionDeletedEvent extends GwtEvent<CollectionDeletedEvent.CollectionDeletedHandler> {
    public interface CollectionDeletedHandler extends EventHandler {
        void onCollectionDeleted(CollectionDeletedEvent event);
    }

    public static final Type<CollectionDeletedHandler> TYPE = new Type<CollectionDeletedHandler>();

    private final Collection collection;

    private CollectionDeletedEvent(Collection collection) {
        this.collection = collection;
    }

    public static void fire(HasHandlers source, Collection collection) {
        source.fireEvent(new CollectionDeletedEvent(collection));
    }

    public Collection getCollection() {
        return collection;
    }

    @Override
    public Type<CollectionDeletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CollectionDeletedHandler handler) {
        handler.onCollectionDeleted(this);
    }
}
