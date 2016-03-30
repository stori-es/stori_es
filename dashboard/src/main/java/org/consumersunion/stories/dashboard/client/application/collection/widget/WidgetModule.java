package org.consumersunion.stories.dashboard.client.application.collection.widget;

import org.consumersunion.stories.dashboard.client.application.collection.widget.chart.ChartModule;
import org.consumersunion.stories.dashboard.client.application.collection.widget.collectionstoken
        .CollectionsTokenModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class WidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new ChartModule());
        install(new CollectionsTokenModule());

        bindPresenterWidget(StoriesWidgetPresenter.class, StoriesWidgetPresenter.MyView.class, StoriesWidgetView.class);
        bindPresenterWidget(SummaryWidgetPresenter.class, SummaryWidgetPresenter.MyView.class, SummaryWidgetView.class);
    }
}
