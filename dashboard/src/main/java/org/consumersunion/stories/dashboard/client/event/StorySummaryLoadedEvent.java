package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class StorySummaryLoadedEvent extends GwtEvent<StorySummaryLoadedEvent.StorySummaryLoadedHandler> {
    public interface StorySummaryLoadedHandler extends EventHandler {
        void onStorySummaryLoaded(StorySummaryLoadedEvent event);
    }

    public static final Type<StorySummaryLoadedHandler> TYPE = new Type<StorySummaryLoadedHandler>();

    private final StorySummary storySummary;

    private StorySummaryLoadedEvent(StorySummary storySummary) {
        this.storySummary = storySummary;
    }

    public static void fire(HasHandlers source, StorySummary storySummary) {
        source.fireEvent(new StorySummaryLoadedEvent(storySummary));
    }

    public StorySummary getStorySummary() {
        return storySummary;
    }

    @Override
    public Type<StorySummaryLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StorySummaryLoadedHandler handler) {
        handler.onStorySummaryLoaded(this);
    }
}
