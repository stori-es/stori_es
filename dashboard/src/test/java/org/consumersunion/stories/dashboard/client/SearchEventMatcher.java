package org.consumersunion.stories.dashboard.client;

import org.consumersunion.stories.common.client.event.SearchEvent;
import org.consumersunion.stories.common.shared.model.SortDropDownItem;
import org.mockito.ArgumentMatcher;

public class SearchEventMatcher<T extends SortDropDownItem> extends ArgumentMatcher<SearchEvent> {
    private final String searchToken;
    private final T sortField;

    public SearchEventMatcher(String searchToken, T sortField) {
        this.searchToken = searchToken;
        this.sortField = sortField;
    }

    @Override
    public boolean matches(Object argument) {
        SearchEvent other = (SearchEvent) argument;

        return other.getSearchToken().equals(searchToken)
                && other.getSortDropDownItem().getSortField().equals(sortField.getSortField());
    }
}
