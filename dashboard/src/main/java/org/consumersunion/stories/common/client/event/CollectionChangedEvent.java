package org.consumersunion.stories.common.client.event;

import org.consumersunion.stories.common.shared.model.Collection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Event signifying 'the {@link Collection}' now different. It's either a new {@link Collection} or the data of the
 * current {@link Collection} has changed.
 */
public class CollectionChangedEvent extends GwtEvent<CollectionChangedEvent.CollectionChangedHandler> {
    public interface CollectionChangedHandler extends EventHandler {
        void onCollectionChanged(CollectionChangedEvent event);
    }

    public static final Type<CollectionChangedHandler> TYPE = new Type<CollectionChangedHandler>();

    private final Collection collection;

    private CollectionChangedEvent(Collection collection) {
        this.collection = collection;
    }

    public static void fire(HasHandlers source, Collection collection) {
        source.fireEvent(new CollectionChangedEvent(collection));
    }

    @Override
    public Type<CollectionChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CollectionChangedHandler handler) {
        handler.onCollectionChanged(this);
    }

    public Collection getCollection() {
        return collection;
    }
}
