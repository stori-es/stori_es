package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ChartModule extends AbstractPresenterModule {
    static final int CHART_HEIGHT = 350;
    static final int CHART_WIDTH = 900;

    @Override
    protected void configure() {
        bind(TimelinePresenter.MyView.class).to(TimelineView.class);
        bind(TimeDistributionPresenter.MyView.class).to(TimeDistributionView.class);
        bind(MapPresenter.MyView.class).to(MapView.class);
        bindPresenterWidget(ChartsPresenter.class, ChartsPresenter.MyView.class, ChartsView.class);

        install(new GinFactoryModuleBuilder().build(ChartFactory.class));
    }
}
