package org.consumersunion.stories.dashboard.client.application.collection.widget;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import static com.google.gwt.query.client.GQuery.$;

public class SummaryWidgetView extends ViewImpl implements SummaryWidgetPresenter.MyView {
    interface Binder extends UiBinder<Widget, SummaryWidgetView> {
    }

    @UiField
    Resources resource;
    @UiField
    DivElement count;
    @UiField
    SimplePanel charts;
    @UiField
    DivElement noSummary;

    private final StoryTellerDashboardI18nLabels storyTellerLabels;
    private final NumberFormat numberFormat;

    @Inject
    SummaryWidgetView(Binder binder,
            StoryTellerDashboardI18nLabels storyTellerLabels) {
        this.storyTellerLabels = storyTellerLabels;
        this.numberFormat = NumberFormat.getFormat("#,##0");

        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public void setStoriesCount(int numberOfStories) {
        if (numberOfStories > 0) {
            count.setInnerHTML(storyTellerLabels.storiesCount(numberFormat.format(numberOfStories)).asString());
            $("span", count).addClass(resource.generalStyleCss().bold());
            $(count).show();
            $(noSummary).hide();
        } else {
            $(count).hide();
            $(noSummary).show();
        }
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == SummaryWidgetPresenter.SLOT_CHARTS) {
            charts.setWidget(content);
        }
    }
}
