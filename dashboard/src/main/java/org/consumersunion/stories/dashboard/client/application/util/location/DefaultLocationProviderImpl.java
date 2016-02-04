package org.consumersunion.stories.dashboard.client.application.util.location;

import com.google.gwt.maps.client.base.LatLng;

public class DefaultLocationProviderImpl implements DefaultLocationProvider {
    private static final LatLng DEFAULT_NORTH_EAST = LatLng.newInstance(49.86, -67.74);
    private static final LatLng DEFAULT_SOUTH_WEST = LatLng.newInstance(23.59, -125.40);

    @Override
    public LatLng northEast() {
        return DEFAULT_NORTH_EAST;
    }

    @Override
    public LatLng southWest() {
        return DEFAULT_SOUTH_WEST;
    }
}
