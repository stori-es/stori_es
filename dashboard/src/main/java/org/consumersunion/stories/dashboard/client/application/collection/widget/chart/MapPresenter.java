package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import org.consumersunion.stories.common.client.event.StateSearchEvent;
import org.consumersunion.stories.common.client.service.RpcMetricsServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class MapPresenter extends PresenterWidget<MapPresenter.MyView> implements MapUiHandlers {
    interface MyView extends View, HasUiHandlers<MapUiHandlers> {
        void displayData(StoriesCountByState countByState);

        void remove();
    }

    @AssistedInject
    MapPresenter(
            EventBus eventBus,
            MyView view,
            RpcMetricsServiceAsync metricsService,
            @Assisted CollectionData collectionData) {
        super(eventBus, view);

        view.setUiHandlers(this);
        metricsService.getStoriesCountByState(collectionData.getId(),
                new ResponseHandler<DatumResponse<StoriesCountByState>>() {
                    @Override
                    public void handleSuccess(DatumResponse<StoriesCountByState> result) {
                        onDataReceived(result);
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

    @Override
    public void onStateClicked(String stateCode) {
        StateSearchEvent.fire(this, stateCode);
    }

    private void onDataReceived(DatumResponse<StoriesCountByState> result) {
        StoriesCountByState countByState = result.getDatum();

        if (countByState.hasData()) {
            getView().displayData(countByState);
        } else {
            getView().remove();
        }
    }
}
