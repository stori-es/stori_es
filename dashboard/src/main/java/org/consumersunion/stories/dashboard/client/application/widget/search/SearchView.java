package org.consumersunion.stories.dashboard.client.application.widget.search;

import java.util.List;

import org.consumersunion.stories.common.shared.model.SortDropDownItem;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown.DropDownHandler;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class SearchView extends ViewWithUiHandlers<SearchUiHandlers> implements SearchPresenter.MyView {
    interface Binder extends UiBinder<Widget, SearchView> {
    }

    @UiField(provided = true)
    final ClickableDropDown<SortDropDownItem> sortDropDown;

    @UiField
    TextBox search;
    @UiField
    DivElement numberShown;
    @UiField
    Button clearSearch;

    @Inject
    SearchView(
            Binder uiBinder,
            ClickableDropDown<SortDropDownItem> sortDropDown) {
        this.sortDropDown = sortDropDown;

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void init(SearchProvider searchProvider) {
        search.getElement().setAttribute("placeholder", searchProvider.getSearchPlaceholder());

        sortDropDown.loadOptions((List<SortDropDownItem>) searchProvider.getSortFields(), false);
        sortDropDown.setTitle(searchProvider.getDefaultSortFieldLabel());
        sortDropDown.setDropDownHandler(new DropDownHandler<SortDropDownItem>() {
            @Override
            public void onLoadSpecificItem(SortDropDownItem item) {
                getUiHandlers().onSortChanged();
            }
        });
    }

    @Override
    public void setSearchToken(String searchToken) {
        search.setText(searchToken);
        updateClearSearchVisibility();
    }

    @Override
    public String getSearchToken() {
        return search.getText();
    }

    @Override
    public void setSortField(SortDropDownItem sortField) {
        sortDropDown.setSelection(sortField);
    }

    @Override
    public void setSearchResult(String result) {
        numberShown.setInnerHTML(result);
    }

    @Override
    public SortDropDownItem getCurrentSort() {
        return sortDropDown.getSelection();
    }

    @UiHandler("clearSearch")
    void onClearSearch(ClickEvent event) {
        search.setText("");
        onSearchTextChanged();
    }

    @UiHandler("search")
    void onSearchKeyUp(KeyUpEvent event) {
        updateClearSearchVisibility();

        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            onSearchTextChanged();
        }
    }

    private void updateClearSearchVisibility() {
        clearSearch.setVisible(!search.getText().isEmpty());
    }

    private void onSearchTextChanged() {
        getUiHandlers().onSearchTextChanged();
    }
}
