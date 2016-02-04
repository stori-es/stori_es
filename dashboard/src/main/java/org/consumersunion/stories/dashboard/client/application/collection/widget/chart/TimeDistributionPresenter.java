package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import java.util.Date;

import org.consumersunion.stories.common.client.service.RpcMetricsServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.google.gwt.i18n.client.TimeZone;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class TimeDistributionPresenter extends PresenterWidget<TimeDistributionPresenter.MyView> {
    interface MyView extends View {
        void displayData(StoriesCountByDayAndTime countByDayAndTime);
    }

    @AssistedInject
    TimeDistributionPresenter(
            EventBus eventBus,
            MyView view,
            RpcMetricsServiceAsync metricsService,
            @Assisted CollectionData collectionData) {
        super(eventBus, view);

        metricsService.getStoriesCountByDayAndTime(collectionData.getId(), getTimezone(),
                new ResponseHandler<DatumResponse<StoriesCountByDayAndTime>>() {
                    @Override
                    public void handleSuccess(DatumResponse<StoriesCountByDayAndTime> result) {
                        getView().displayData(result.getDatum());
                    }
                });
    }

    @AssistedInject
    TimeDistributionPresenter(
            EventBus eventBus,
            MyView view,
            @Assisted StoriesCountByDayAndTime countByDayAndTime) {
        super(eventBus, view);

        getView().displayData(countByDayAndTime);
    }

    private String getTimezone() {
        Date date = new Date();
        return TimeZone.createTimeZone(date.getTimezoneOffset()).getISOTimeZoneString(date);
    }
}
