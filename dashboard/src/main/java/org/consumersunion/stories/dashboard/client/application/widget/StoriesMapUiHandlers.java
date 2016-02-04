package org.consumersunion.stories.dashboard.client.application.widget;

import com.google.gwt.maps.client.base.LatLng;
import com.gwtplatform.mvp.client.UiHandlers;

public interface StoriesMapUiHandlers extends UiHandlers {
    void filterStories(LatLng northEast, LatLng southWest);
}
