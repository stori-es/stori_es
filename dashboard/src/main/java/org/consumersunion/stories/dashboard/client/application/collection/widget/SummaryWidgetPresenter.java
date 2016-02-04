package org.consumersunion.stories.dashboard.client.application.collection.widget;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.dashboard.client.application.collection.widget.chart.ChartsPresenter;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class SummaryWidgetPresenter extends PresenterWidget<SummaryWidgetPresenter.MyView> {
    interface MyView extends View {
        void setStoriesCount(int numberOfStories);
    }

    static final Object SLOT_CHARTS = new Object();

    private final ChartsPresenter chartsPresenter;

    @Inject
    SummaryWidgetPresenter(
            EventBus eventBus,
            MyView view,
            ChartsPresenter chartsPresenter) {
        super(eventBus, view);

        this.chartsPresenter = chartsPresenter;
    }

    public void init(CollectionData collectionData) {
        getView().setStoriesCount(collectionData.getStoriesCount());
        chartsPresenter.init(collectionData);
    }

    @Override
    protected void onBind() {
        super.onBind();

        setInSlot(SLOT_CHARTS, chartsPresenter);
    }
}
