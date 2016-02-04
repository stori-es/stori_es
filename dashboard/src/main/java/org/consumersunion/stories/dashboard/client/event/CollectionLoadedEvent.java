package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.common.shared.model.Collection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CollectionLoadedEvent extends GwtEvent<CollectionLoadedEvent.CollectionLoadedHandler> {
    public interface CollectionLoadedHandler extends EventHandler {
        void onCollectionLoaded(CollectionLoadedEvent event);
    }

    public static final Type<CollectionLoadedHandler> TYPE = new Type<CollectionLoadedHandler>();

    private final Collection collection;

    private CollectionLoadedEvent(Collection collection) {
        this.collection = collection;
    }

    public static void fire(HasHandlers source, Collection collection) {
        source.fireEvent(new CollectionLoadedEvent(collection));
    }

    @Override
    public Type<CollectionLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    public Collection getCollection() {
        return collection;
    }

    @Override
    protected void dispatch(CollectionLoadedHandler handler) {
        handler.onCollectionLoaded(this);
    }
}
