package org.consumersunion.stories.dashboard.client.application.stories;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.model.StorySortFieldDropDownItem;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown;
import org.consumersunion.stories.dashboard.client.application.ui.ClickableDropDown.DropDownHandler;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuWidget;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class StoriesView extends ViewWithUiHandlers<StoriesUiHandlers>
        implements StoriesPresenter.MyView {
    interface Binder extends UiBinder<Widget, StoriesView> {
    }

    @UiField(provided = true)
    final ClickableDropDown<StorySortFieldDropDownItem> sortDropDown;

    @UiField
    HTML totalCount;
    @UiField
    TextBox search;
    @UiField
    SimplePanel addToPanel;
    @UiField
    SimplePanel addTo;
    @UiField
    SimplePanel storiesList;
    @UiField
    Resources resource;

    private final StoryTellerDashboardI18nLabels labels;
    private final NumberFormat numberFormat;

    @Inject
    StoriesView(
            Binder uiBinder,
            StoryTellerDashboardI18nLabels labels,
            ClickableDropDown<StorySortFieldDropDownItem> sortDropDown) {
        this.labels = labels;
        this.sortDropDown = sortDropDown;
        this.numberFormat = NumberFormat.getFormat("#,##0");

        initWidget(uiBinder.createAndBindUi(this));
        setupSortDropDown();
        setupDropDownEvents();

        search.getElement().setAttribute("placeholder", labels.searchStories());
    }

    @Override
    public void setAddToWidget(AddToMenuWidget addToWidget) {
        addTo.setWidget(addToWidget);
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == StoriesPresenter.SLOT_ADD_TO_PANEL) {
            addToPanel.setWidget(content);
        } else if (slot == StoriesPresenter.SLOT_STORIES) {
            storiesList.setWidget(content);
        }
    }

    @Override
    public void updateCount(boolean selecting, int numberOfStories) {
        String text;
        String totalRowCount = numberFormat.format(getUiHandlers().getRowHandler().getRowCount());

        if (selecting) {
            String selectedRowCount = numberFormat.format(numberOfStories);
            text = labels.storiesCountWithSelection(totalRowCount, selectedRowCount);
        } else {
            text = labels.storiesCount(totalRowCount).asString();
        }

        totalCount.setHTML(text);
    }

    @Override
    public StorySortFieldDropDownItem getSort() {
        return sortDropDown.getSelection();
    }

    @Override
    public void setStoriesCount(int count) {
        totalCount.setHTML(labels.storiesCount(numberFormat.format(count)));
    }

    @Override
    public void updateForCheckboxes(boolean withCheckboxes) {
        $(storiesList).toggleClass(resource.generalStyleCss().storiesListWithCheckbox(), withCheckboxes);
    }

    @Override
    public void setSearchToken(String token) {
        search.setText(token);
    }

    @UiHandler("search")
    void onSearchKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            getUiHandlers().filterStories(search.getText());
        }
    }

    @UiHandler("clearSearch")
    void onClearSearch(ClickEvent event) {
        search.setText("");
        getUiHandlers().filterStories("");
    }

    private void setupSortDropDown() {
        sortDropDown.loadOptions(StorySortFieldDropDownItem.sortList(), false);
        StorySortFieldDropDownItem defaultSortField = StorySortFieldDropDownItem.defaultSortField();
        sortDropDown.setSelection(defaultSortField);
        sortDropDown.setTitle(defaultSortField.getLabel());
        sortDropDown.getElement().setId("stories-storiesview-sort-dropdown");
    }

    private void setupDropDownEvents() {
        sortDropDown.setDropDownHandler(new DropDownHandler<StorySortFieldDropDownItem>() {
            public void onLoadSpecificItem(StorySortFieldDropDownItem item) {
                sortDropDown.setTitle(item.getLabel());
                getUiHandlers().onSortChanged(item.getSortField());
            }
        });
    }
}
