package org.consumersunion.stories.dashboard.client.application.collection.widget;

import org.consumersunion.stories.dashboard.client.application.collection.widget.chart.ChartModule;
import org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbyquestionnaire
        .CollectionsByQuestionnaireModule;
import org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbystory
        .CollectionsByStoryModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class WidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new ChartModule());
        install(new CollectionsByQuestionnaireModule());
        install(new CollectionsByStoryModule());

        bindPresenterWidget(StoriesWidgetPresenter.class, StoriesWidgetPresenter.MyView.class, StoriesWidgetView.class);
        bindPresenterWidget(SummaryWidgetPresenter.class, SummaryWidgetPresenter.MyView.class, SummaryWidgetView.class);
    }
}
