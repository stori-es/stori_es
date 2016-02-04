package org.consumersunion.stories.dashboard.client.application.collection.widget;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;
import org.consumersunion.stories.dashboard.client.application.ui.MapToggler;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuWidget;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasRows;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class StoriesWidgetView extends ViewWithUiHandlers<StoriesWidgetUiHandlers>
        implements StoriesWidgetPresenter.MyView, MapToggler.MapTogglerHandler {
    interface Binder extends UiBinder<Widget, StoriesWidgetView> {
    }

    @UiField(provided = true)
    final MapToggler mapToggler;
    @UiField
    SimplePanel search;
    @UiField
    SimplePanel addTo;
    @UiField
    SimplePanel addToPanel;
    @UiField
    SimplePanel mapContainer;
    @UiField
    HTMLPanel storiesWrapper;
    @UiField
    SimplePanel storiesContentPanel;
    @UiField
    Resources resource;

    private final StoryTellerDashboardI18nLabels storyTellerLabels;
    private final NumberFormat numberFormat;

    @Inject
    StoriesWidgetView(
            Binder binder,
            StoryTellerDashboardI18nLabels storyTellerLabels,
            MapToggler mapToggler) {
        this.storyTellerLabels = storyTellerLabels;
        this.mapToggler = mapToggler;
        this.numberFormat = NumberFormat.getFormat("#,##0");

        mapToggler.addHandler(this);

        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public String getUpdateCountText(boolean selecting, int numberOfStories) {
        StoriesListContainer storiesListContainer = getUiHandlers().getStoriesListContainer();
        HasRows rowHandler = storiesListContainer.getRowHandler();

        String text;
        String totalRowCount = numberFormat.format(rowHandler.getRowCount());

        if (selecting) {
            String selectedRowCount = numberFormat.format(numberOfStories);
            text = storyTellerLabels.storiesCountWithSelection(totalRowCount, selectedRowCount);
        } else {
            text = storyTellerLabels.storiesCount(totalRowCount).asString();
        }

        return text;
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == StoriesWidgetPresenter.SLOT_SEARCH) {
            search.setWidget(content);
        } else if (slot == StoriesWidgetPresenter.SLOT_MAP) {
            mapContainer.setVisible(content != null);
            mapContainer.setWidget(content);
        } else if (slot == StoriesWidgetPresenter.SLOT_ADD_TO_PANEL) {
            addToPanel.setWidget(content);
        } else if (slot == StoriesWidgetPresenter.SLOT_STORIES) {
            storiesContentPanel.setWidget(content);
        }
    }

    @Override
    public void setAddToWidget(AddToMenuWidget addToWidget) {
        addTo.setWidget(addToWidget);
    }

    @Override
    public void onSwitchToMap() {
        getUiHandlers().switchToMap();
    }

    @Override
    public void onSwitchToList() {
        getUiHandlers().switchToList();
    }

    @Override
    public String getStoriesCountText(Integer count) {
        return storyTellerLabels.storiesCount(numberFormat.format(count)).asString();
    }

    @Override
    public void updateForCheckboxes(final boolean withCheckboxes) {
        $(storiesContentPanel).toggleClass(resource.generalStyleCss().storiesListWithCheckbox(), withCheckboxes);
    }
}
