package org.consumersunion.stories.dashboard.client.application.util.location;

import com.google.gwt.maps.client.base.LatLng;

public interface DefaultLocationProvider {
    LatLng northEast();

    LatLng southWest();
}
