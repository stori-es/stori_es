package org.consumersunion.stories.common.client.service.datatransferobject;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Coordinates implements Serializable, IsSerializable {
    private double latitude;
    private double longitude;

    Coordinates() {
    }

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
