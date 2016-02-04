package org.consumersunion.stories.server.helper.geo;

import java.math.BigDecimal;
import java.util.Date;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Address.GeoCodeProvider;
import org.consumersunion.stories.common.shared.model.Address.GeoCodeStatus;

public class Localisation {
    private GeoCodeStatus geoCodeStatus;
    private GeoCodeProvider geoCodeProvider;
    private Double longitude;
    private Double latitude;
    private Integer range;

    public void updateAddress(Address address) {
        address.setGeoCodeDate(new Date());
        address.setGeoCodeProvider(geoCodeProvider.name());
        address.setGeoCodeStatus(geoCodeStatus.name());

        if (geoCodeStatus == GeoCodeStatus.SUCCESS) {
            address.setLongitude(BigDecimal.valueOf(longitude));
            address.setLatitude(BigDecimal.valueOf(latitude));
        }
    }

    public GeoCodeStatus getGeoCodeStatus() {
        return geoCodeStatus;
    }

    public void setGeoCodeStatus(GeoCodeStatus geoCodeStatus) {
        this.geoCodeStatus = geoCodeStatus;
    }

    public GeoCodeProvider getGeoCodeProvider() {
        return geoCodeProvider;
    }

    public void setGeoCodeProvider(GeoCodeProvider geoCodeProvider) {
        this.geoCodeProvider = geoCodeProvider;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }
}
