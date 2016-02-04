package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import org.consumersunion.stories.common.client.service.RpcMetricsServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class MapPresenter extends PresenterWidget<MapPresenter.MyView> {
    interface MyView extends View {
        void displayData(StoriesCountByState countByState);
    }

    @AssistedInject
    MapPresenter(
            EventBus eventBus,
            MyView view,
            RpcMetricsServiceAsync metricsService,
            @Assisted CollectionData collectionData) {
        super(eventBus, view);

        metricsService.getStoriesCountByState(collectionData.getId(),
                new ResponseHandler<DatumResponse<StoriesCountByState>>() {
                    @Override
                    public void handleSuccess(DatumResponse<StoriesCountByState> result) {
                        getView().displayData(result.getDatum());
                    }
                });
    }

    @AssistedInject
    MapPresenter(
            EventBus eventBus,
            MyView view,
            @Assisted StoriesCountByState countByState) {
        super(eventBus, view);

        getView().displayData(countByState);
    }
}
