package org.consumersunion.stories.dashboard.client.application.widget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.googlecode.gwt.charts.client.corechart.AreaChart;
import com.gwtplatform.mvp.client.ViewImpl;

public class StoriesGrowthView extends ViewImpl implements StoriesGrowthPresenter.MyView {
    interface Binder extends UiBinder<Widget, StoriesGrowthView> {
    }

    @UiField
    FlowPanel panel;

    @Inject
    StoriesGrowthView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void displayChart(AreaChart chart) {
        panel.clear();
        panel.add(chart);
    }
}
