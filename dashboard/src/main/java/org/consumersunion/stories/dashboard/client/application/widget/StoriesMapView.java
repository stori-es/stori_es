package org.consumersunion.stories.dashboard.client.application.widget;

import java.util.List;
import java.util.Map;

import org.consumersunion.stories.common.shared.service.datatransferobject.StoryPosition;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.dragend.DragEndMapEvent;
import com.google.gwt.maps.client.events.dragend.DragEndMapHandler;
import com.google.gwt.maps.client.events.zoom.ZoomChangeMapEvent;
import com.google.gwt.maps.client.events.zoom.ZoomChangeMapHandler;
import com.google.gwt.maps.client.overlays.Animation;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerImage;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.utility.markerclustererplus.client.MarkerClusterer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class StoriesMapView extends ViewWithUiHandlers<StoriesMapUiHandlers>
        implements StoriesMapPresenter.MyView {
    interface Binder extends UiBinder<Widget, StoriesMapView> {
    }

    private static final Float USA_LAT = 37.54f;
    private static final Float USA_LNG = -96.40f;
    private static final String DOT_ICON = "/images/littleDot.png";
    private static final String PIN_ICON = "/images/pin.png";

    @UiField
    SimplePanel mapContainer;

    private final List<Integer> storiesInCurrentPage;
    private final Map<Integer, Marker> storyIdMarkerMap;

    private MapWidget mapWidget;
    private HandlerRegistration dragHandler;
    private HandlerRegistration zoomHandler;
    private MarkerClusterer markerClusterer;

    @Inject
    StoriesMapView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                initMap();
            }
        });

        storyIdMarkerMap = Maps.newHashMap();
        storiesInCurrentPage = Lists.newArrayList();
    }

    @Override
    public void centerMap() {
        if (zoomHandler != null) {
            zoomHandler.removeHandler();
        }

        if (dragHandler != null) {
            dragHandler.removeHandler();
        }

        mapWidget.setZoom(4);
        mapWidget.setCenter(LatLng.newInstance(USA_LAT, USA_LNG));
        setupMapWidgetEvents();
    }

    @Override
    public void updatePins(List<Integer> data) {
        for (Integer id : storiesInCurrentPage) {
            Marker marker = storyIdMarkerMap.get(id);

            if (marker != null) {
                marker.setIcon(MarkerImage.newInstance(DOT_ICON));
            }
        }

        storiesInCurrentPage.clear();
        storiesInCurrentPage.addAll(data);

        for (Integer id : storiesInCurrentPage) {
            Marker marker = storyIdMarkerMap.get(id);

            if (marker != null) {
                marker.setIcon(MarkerImage.newInstance(PIN_ICON));
                marker.setMap(mapWidget);
            }
        }
    }

    @Override
    public void setStoriesPositions(List<StoryPosition> positions) {
        for (Marker marker : storyIdMarkerMap.values()) {
            marker.clear();
        }

        storyIdMarkerMap.clear();
        markerClusterer.clearMarkers();

        for (StoryPosition position : positions) {
            Float lat = position.getLatitude();
            Float lng = position.getLongitude();

            LatLng center = LatLng.newInstance(lat, lng);
            MarkerOptions options = MarkerOptions.newInstance();
            options.setPosition(center);
            options.setAnimation(Animation.DROP);
            options.setOptimized(true);

            Marker marker = Marker.newInstance(options);
            marker.setIcon(MarkerImage.newInstance(DOT_ICON));
            storyIdMarkerMap.put(position.getStoryId(), marker);

            markerClusterer.addMarker(marker, true);
        }

        markerClusterer.repaint();
    }

    @Override
    public void showMap() {
        mapWidget.triggerResize();
    }

    private void initMap() {
        MapOptions mapOptions = MapOptions.newInstance();
        mapOptions.setMapTypeControl(false);
        mapOptions.setStreetViewControl(false);
        mapWidget = new MapWidget(mapOptions);
        mapWidget.setSize("100%", "380px");
        mapContainer.setWidget(mapWidget);

        markerClusterer = MarkerClusterer.newInstance(mapWidget);

        centerMap();
    }

    private void setupMapWidgetEvents() {
        dragHandler = mapWidget.addDragEndHandler(new DragEndMapHandler() {
            @Override
            public void onEvent(DragEndMapEvent dragEndMapEvent) {
                onPositionChanged();
            }
        });

        zoomHandler = mapWidget.addZoomChangeHandler(new ZoomChangeMapHandler() {
            @Override
            public void onEvent(ZoomChangeMapEvent zoomChangeMapEvent) {
                onPositionChanged();
            }
        });
    }

    private void onPositionChanged() {
        LatLng northEast = mapWidget.getBounds().getNorthEast();
        LatLng southWest = mapWidget.getBounds().getSouthWest();

        getUiHandlers().filterStories(northEast, southWest);
    }
}
