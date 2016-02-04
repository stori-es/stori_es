package org.consumersunion.stories.common.client.event;

import org.consumersunion.stories.common.shared.model.SortDropDownItem;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SearchEvent extends GwtEvent<SearchEvent.SearchHandler> {
    public interface SearchHandler extends EventHandler {
        void onSearch(SearchEvent event);
    }

    public static final Type<SearchHandler> TYPE = new Type<SearchHandler>();

    private final String searchToken;
    private final SortDropDownItem sortField;

    protected SearchEvent(String searchToken, SortDropDownItem sortField) {
        this.searchToken = searchToken;
        this.sortField = sortField;
    }

    public static void fire(HasHandlers source, String searchToken, SortDropDownItem sortField) {
        source.fireEvent(new SearchEvent(searchToken, sortField));
    }

    @Override
    public Type<SearchHandler> getAssociatedType() {
        return TYPE;
    }

    public String getSearchToken() {
        return searchToken;
    }

    public <T extends SortDropDownItem> T getSortDropDownItem() {
        return (T) sortField;
    }

    @Override
    protected void dispatch(SearchHandler handler) {
        handler.onSearch(this);
    }
}
