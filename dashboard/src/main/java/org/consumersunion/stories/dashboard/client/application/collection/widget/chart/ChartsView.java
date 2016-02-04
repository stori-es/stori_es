package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ChartsView extends ViewImpl implements ChartsPresenter.MyView {
    interface Binder extends UiBinder<Widget, ChartsView> {
    }

    @UiField
    Resources resource;
    @UiField
    SimplePanel timeDistribution;
    @UiField
    SimplePanel timeline;
    @UiField
    SimplePanel map;

    @Inject
    ChartsView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ChartsPresenter.SLOT_TIME_DISTRIBUTION) {
            timeDistribution.setWidget(content);
        } else if (slot == ChartsPresenter.SLOT_TIMELINE) {
            timeline.setWidget(content);
        } else if (slot == ChartsPresenter.SLOT_MAP) {
            map.setWidget(content);
        }
    }
}
