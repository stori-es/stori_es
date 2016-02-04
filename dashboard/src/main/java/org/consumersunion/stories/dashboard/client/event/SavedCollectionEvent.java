package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.common.shared.model.Collection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class SavedCollectionEvent extends SavedEvent<Collection, SavedCollectionEvent.SaveCollectionHandler> {
    public interface SaveCollectionHandler extends EventHandler {
        void onCollectionSaved(SavedCollectionEvent event);
    }

    public static final Type<SaveCollectionHandler> TYPE = new Type<SaveCollectionHandler>();

    private SavedCollectionEvent(Collection data) {
        super(data);
    }

    public static void fire(HasHandlers source, Collection data) {
        source.fireEvent(new SavedCollectionEvent(data));
    }

    @Override
    public Type<SaveCollectionHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SaveCollectionHandler handler) {
        handler.onCollectionSaved(this);
    }
}
