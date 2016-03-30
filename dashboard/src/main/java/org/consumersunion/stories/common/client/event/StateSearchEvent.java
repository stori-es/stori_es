package org.consumersunion.stories.common.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class StateSearchEvent extends GwtEvent<StateSearchEvent.SearchHandler> {
    public interface SearchHandler extends EventHandler {
        void onStateSearch(StateSearchEvent event);
    }

    public static final Type<SearchHandler> TYPE = new Type<SearchHandler>();

    private final String state;

    protected StateSearchEvent(String state) {
        this.state = state;
    }

    public static void fire(HasHandlers source, String state) {
        source.fireEvent(new StateSearchEvent(state));
    }

    @Override
    public Type<SearchHandler> getAssociatedType() {
        return TYPE;
    }

    public String getState() {
        return state;
    }

    @Override
    protected void dispatch(SearchHandler handler) {
        handler.onStateSearch(this);
    }
}
