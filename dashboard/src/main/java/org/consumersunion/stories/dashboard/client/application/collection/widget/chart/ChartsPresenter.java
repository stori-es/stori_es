package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import java.util.Date;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.util.StatesUtil;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Random;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ChartsPresenter extends PresenterWidget<ChartsPresenter.MyView> {
    interface MyView extends View {
    }

    static final Object SLOT_TIMELINE = new Object();
    static final Object SLOT_TIME_DISTRIBUTION = new Object();
    static final Object SLOT_MAP = new Object();

    private static final long THIRTY_DAYS = 2592000000L;
    private static final long ONE_DAY = 86400000L;

    private final ChartFactory chartFactory;

    @Inject
    ChartsPresenter(
            EventBus eventBus,
            MyView view,
            ChartFactory chartFactory) {
        super(eventBus, view);

        this.chartFactory = chartFactory;
    }

    public void init(final CollectionData collectionData) {
        // Lazy load Visualization API. Call is instant if already loaded
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(new Runnable() {
            @Override
            public void run() {
                showCharts(collectionData);
            }
        });
    }

    private void showCharts(CollectionData collectionData) {
        if (collectionData.getStoriesCount() > 0) {
            setInSlot(SLOT_TIMELINE, chartFactory.createTimeline(collectionData));
            setInSlot(SLOT_TIME_DISTRIBUTION, chartFactory.createTimeDistribution(collectionData));
            setInSlot(SLOT_MAP, chartFactory.createMap(collectionData));
        } else {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    setInSlot(SLOT_TIMELINE, createFakeTimeline());
                    setInSlot(SLOT_TIME_DISTRIBUTION, createFakeTimeDistribution());
                    setInSlot(SLOT_MAP, createFakeMap());
                }
            });
        }
    }

    private TimelinePresenter createFakeTimeline() {
        Date published = new Date();
        published.setTime(published.getTime() - THIRTY_DAYS / 2);

        Date modified = new Date();
        modified.setTime(modified.getTime() - THIRTY_DAYS);

        StoriesCountByDate countByDate = new StoriesCountByDate();
        Date now = new Date();
        for (int i = 14; i >= 1; i--) {
            countByDate.add(new Date(now.getTime() - i * ONE_DAY), Random.nextInt(2000));
        }

        return chartFactory.createTimeline(modified, published, countByDate);
    }

    private TimeDistributionPresenter createFakeTimeDistribution() {
        StoriesCountByDayAndTime countByDayAndTime = new StoriesCountByDayAndTime();
        for (int day = 1; day <= 7; day++) {
            for (int hour = 1; hour <= 24; hour++) {
                countByDayAndTime.add(day, hour, Random.nextInt(100));
            }
        }

        return chartFactory.createTimeDistribution(countByDayAndTime);
    }

    private MapPresenter createFakeMap() {
        StoriesCountByState countByState = new StoriesCountByState();

        for (String state : StatesUtil.getCodes()) {
            countByState.add(state, Random.nextInt(1000));
        }

        return chartFactory.createMap(countByState);
    }
}
