package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import java.util.Date;

import org.consumersunion.stories.common.client.service.RpcMetricsServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.google.gwt.i18n.client.TimeZone;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class TimelinePresenter extends PresenterWidget<TimelinePresenter.MyView> {
    interface MyView extends View {
        void displayData(Date publishedDate, Date updated, StoriesCountByDate countByDate);
    }

    @AssistedInject
    TimelinePresenter(
            EventBus eventBus,
            MyView view,
            RpcMetricsServiceAsync metricsService,
            @Assisted final CollectionData collectionData) {
        super(eventBus, view);

        metricsService.getStoriesCountByDate(collectionData.getId(), getTimezone(),
                new ResponseHandler<DatumResponse<StoriesCountByDate>>() {
                    @Override
                    public void handleSuccess(DatumResponse<StoriesCountByDate> result) {
                        Collection collection = collectionData.getCollection();
                        getView().displayData(collection.getPublishedDate(), collection.getUpdated(),
                                result.getDatum());
                    }
                });
    }

    @AssistedInject
    TimelinePresenter(
            EventBus eventBus,
            MyView view,
            @Assisted("lastModified") Date lastModified,
            @Assisted("published") Date published,
            @Assisted StoriesCountByDate countByDate) {
        super(eventBus, view);

        getView().displayData(published, lastModified, countByDate);
    }

    private String getTimezone() {
        return TimeZone.createTimeZone(0).getISOTimeZoneString(new Date());
    }
}
