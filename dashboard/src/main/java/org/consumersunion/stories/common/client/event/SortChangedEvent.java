package org.consumersunion.stories.common.client.event;

import org.consumersunion.stories.common.shared.model.entity.SortField;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SortChangedEvent extends GwtEvent<SortChangedEvent.SortChangedHandler> {
    public interface SortChangedHandler extends EventHandler {
        void onSortChanged(SortChangedEvent event);
    }

    public static final Type<SortChangedHandler> TYPE = new Type<SortChangedHandler>();

    private final SortField sortField;

    private SortChangedEvent(SortField sortField) {
        this.sortField = sortField;
    }

    public static void fire(HasHandlers source, SortField sortField) {
        source.fireEvent(new SortChangedEvent(sortField));
    }

    public SortField getSortField() {
        return sortField;
    }

    @Override
    public Type<SortChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SortChangedHandler handler) {
        handler.onSortChanged(this);
    }
}
