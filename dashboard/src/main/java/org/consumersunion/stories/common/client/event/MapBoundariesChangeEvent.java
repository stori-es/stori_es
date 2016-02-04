package org.consumersunion.stories.common.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.maps.client.base.LatLng;

public class MapBoundariesChangeEvent extends GwtEvent<MapBoundariesChangeEvent.MapBoundariesChangeHandler> {
    public interface MapBoundariesChangeHandler extends EventHandler {
        void onBoundariesChanged(MapBoundariesChangeEvent event);
    }

    public static final Type<MapBoundariesChangeHandler> TYPE = new Type<MapBoundariesChangeHandler>();

    private final LatLng northEast;
    private final LatLng southWest;

    private MapBoundariesChangeEvent(LatLng northEast, LatLng southWest) {
        this.northEast = northEast;
        this.southWest = southWest;
    }

    public static void fire(HasHandlers source, LatLng northEast, LatLng southWest) {
        source.fireEvent(new MapBoundariesChangeEvent(northEast, southWest));
    }

    @Override
    public Type<MapBoundariesChangeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MapBoundariesChangeHandler handler) {
        handler.onBoundariesChanged(this);
    }

    public LatLng getNorthEast() {
        return northEast;
    }

    public LatLng getSouthWest() {
        return southWest;
    }
}
