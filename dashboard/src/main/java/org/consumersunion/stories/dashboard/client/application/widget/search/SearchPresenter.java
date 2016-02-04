package org.consumersunion.stories.dashboard.client.application.widget.search;

import org.consumersunion.stories.common.client.event.SearchEvent;
import org.consumersunion.stories.common.shared.model.SortDropDownItem;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.dashboard.client.event.SearchResultEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class SearchPresenter extends PresenterWidget<SearchPresenter.MyView>
        implements SearchUiHandlers, SearchResultEvent.SearchResultHandler {
    interface MyView extends View, HasUiHandlers<SearchUiHandlers> {
        void setSearchToken(String searchToken);

        void init(SearchProvider searchProvider);

        SortDropDownItem getCurrentSort();

        String getSearchToken();

        void setSortField(SortDropDownItem sortField);

        void setSearchResult(String result);
    }

    @Inject
    SearchPresenter(
            EventBus eventBus,
            MyView view,
            @Assisted SearchProvider searchProvider) {
        super(eventBus, view);

        getView().setUiHandlers(this);

        getView().init(searchProvider);
    }

    public void init(String searchToken, SortDropDownItem sortField) {
        getView().setSearchToken(searchToken);
        getView().setSortField(sortField);
    }

    public <T extends SortField> T getSortField() {
        return (T) getView().getCurrentSort().getSortField();
    }

    @Override
    public void onSearchResultReceived(SearchResultEvent event) {
        getView().setSearchResult(event.getResult());
    }

    @Override
    public void onSearchTextChanged() {
        fireSearchEvent();
    }

    @Override
    public void onSortChanged() {
        fireSearchEvent();
    }

    @Override
    protected void onBind() {
        super.onBind();

        addRegisteredHandler(SearchResultEvent.TYPE, this);
    }

    private void fireSearchEvent() {
        SearchEvent.fire(this, getView().getSearchToken(), getView().getCurrentSort());
    }
}
