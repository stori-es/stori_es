package org.consumersunion.stories.common.shared.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    public static class Builder {
        private final Location location;

        private Builder() {
            location = new Location();
        }

        public Builder streetAddress1(String streetAddress1) {
            location.setStreetAddress1(streetAddress1);
            return this;
        }

        public Builder streetAddress2(String streetAddress2) {
            location.setStreetAddress2(streetAddress2);
            return this;
        }

        public Builder city(String city) {
            location.setCity(city);
            return this;
        }

        public Builder state(String state) {
            location.setState(state);
            return this;
        }

        public Builder country(String country) {
            location.setCountry(country);
            return this;
        }

        public Builder postalCode(String postalCode) {
            location.setPostalCode(postalCode);
            return this;
        }

        public Builder latitude(BigDecimal latitude) {
            location.setLatitude(latitude);
            return this;
        }

        public Builder longitude(BigDecimal longitude) {
            location.setLongitude(longitude);
            return this;
        }

        public Location build() {
            return location;
        }
    }

    @JsonProperty("street_address_1")
    private String streetAddress1;
    @JsonProperty("street_address_2")
    private String streetAddress2;
    private String city;
    private String state;
    private String country;
    @JsonProperty("postal_code")
    private String postalCode;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static Builder builder() {
        return new Builder();
    }

    public String getStreetAddress1() {
        return streetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}
