package org.consumersunion.stories.dashboard.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SearchResultEvent extends GwtEvent<SearchResultEvent.SearchResultHandler> {
    public interface SearchResultHandler extends EventHandler {
        void onSearchResultReceived(SearchResultEvent event);
    }

    public static final Type<SearchResultHandler> TYPE = new Type<SearchResultHandler>();

    private final String result;

    private SearchResultEvent(String result) {
        this.result = result;
    }

    public static void fire(HasHandlers source, String result) {
        source.fireEvent(new SearchResultEvent(result));
    }

    @Override
    public Type<SearchResultHandler> getAssociatedType() {
        return TYPE;
    }

    public String getResult() {
        return result;
    }

    @Override
    protected void dispatch(SearchResultHandler handler) {
        handler.onSearchResultReceived(this);
    }
}
