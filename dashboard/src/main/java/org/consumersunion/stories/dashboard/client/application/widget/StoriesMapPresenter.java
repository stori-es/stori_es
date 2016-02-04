package org.consumersunion.stories.dashboard.client.application.widget;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.MapBoundariesChangeEvent;
import org.consumersunion.stories.common.client.event.UpdateMapPinsEvent;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryPosition;

import com.google.gwt.maps.client.base.LatLng;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class StoriesMapPresenter extends PresenterWidget<StoriesMapPresenter.MyView>
        implements StoriesMapUiHandlers, UpdateMapPinsEvent.UpdateMapPinsHandler {
    interface MyView extends View, HasUiHandlers<StoriesMapUiHandlers> {
        void centerMap();

        void setStoriesPositions(List<StoryPosition> positions);

        void updatePins(List<Integer> data);

        void showMap();
    }

    @Inject
    StoriesMapPresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);

        getView().setUiHandlers(this);
    }

    @Override
    public void filterStories(LatLng northEast, LatLng southWest) {
        MapBoundariesChangeEvent.fire(this, northEast, southWest);
    }

    @Override
    public void onStoriesLoaded(UpdateMapPinsEvent event) {
        getView().updatePins(event.getStoryIds());
    }

    public void setStoriesPositions(List<StoryPosition> positions) {
        getView().setStoriesPositions(positions);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        getView().showMap();
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(UpdateMapPinsEvent.TYPE, this);
    }

    @Override
    protected void onHide() {
        super.onHide();

        getView().centerMap();
    }
}
