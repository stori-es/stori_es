package org.consumersunion.stories.common.shared.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Address implements Serializable, Comparable<Address>, IsSerializable {
    public enum GeoCodeStatus {
        SUCCESS, SKIPPED, FAILED
    }

    public enum GeoCodeProvider {
        GOOGLE
    }

    public static final String RELATION_HOME = "Home";
    public static final String RELATION_WORK = "Work";
    public static final String RELATION_OTHER = "Other";

    /**
     * @see #getEntity()
     */
    private int entity;

    /**
     * @see #getRelation()
     */
    private String relation;
    /**
     * @see #getAddress1()
     */
    private String address1;
    /**
     * @see #getAddress2()
     */
    private String address2;
    /**
     * @see #getCity()
     */
    private String city;
    /**
     * @see #getState()
     */
    private String state;
    /**
     * @see #getCountry()
     */
    private String country;
    /**
     * @see #getPostalCode()
     */
    private String postalCode;
    /**
     * @see #getLatitude()
     */
    private BigDecimal latitude;
    /**
     * @see #getLongitude()
     */
    private BigDecimal longitude;
    private String geoCodeStatus;
    private String geoCodeProvider;
    private Date geoCodeDate;
    private int idx;

    /**
     * Standard constructor.
     */
    public Address(int entity) {
        this.entity = entity;
    }

    // For serialization
    public Address() {
    }

    public Address(
            int entity,
            String relation,
            String address1,
            String address2,
            String city,
            String state,
            String country,
            String postalCode,
            BigDecimal latitude,
            BigDecimal longitude) {
        this.entity = entity;
        this.relation = relation;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * The ID of the {@link Entity} associated to the address. Together with
     * {@link #getRelation()}, this defines what the Address "is". Once an
     * address is associated to an Entity, it doesn't make sense to change it.
     * The Address may be deleted, but switching the {@link Entity} doesn't make
     * sense and it is therefore immutable.
     */
    public int getEntity() {
        return entity;
    }

    public void setEntity(final int entity) {
        this.entity = entity;
    }

    /**
     * The relation of the Address to the Entity. TODO: At the time of the
     * writing of these docs, the relations have not been fleshed out. Once they
     * are, we should reference or include that information here.
     */
    public String getRelation() {
        return relation;
    }

    /**
     * @see #getRelation()
     */
    public void setRelation(final String relation) {
        this.relation = relation;
    }

    /**
     * The first line of the address.
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @see #getAddress1()
     */
    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    /**
     * The second line of the address.
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @see #getAddress2()
     */
    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    /**
     * The city.
     */
    public String getCity() {
        return city;
    }

    /**
     * @see #getCity()
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * The state.
     */
    public String getState() {
        return state;
    }

    /**
     * @see #getState()
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * The country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * @see #getCountry()
     */
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     * The postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @see #getPostalCode()
     */
    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * The latitude of the Address. The nullity of this value should mirror
     * {@link #getLongitude()}
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * @see #getLatitude()
     */
    public void setLatitude(final BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * The longitude of the Address. The nullity of this value should mirror
     * {@link #getLatitude()}
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * @see #getLongitude()
     */
    public void setLongitude(final BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getGeoCodeStatus() {
        return geoCodeStatus;
    }

    public void setGeoCodeStatus(String geoCodeStatus) {
        this.geoCodeStatus = geoCodeStatus;
    }

    public String getGeoCodeProvider() {
        return geoCodeProvider;
    }

    public void setGeoCodeProvider(String geoCodeProvider) {
        this.geoCodeProvider = geoCodeProvider;
    }

    public Date getGeoCodeDate() {
        return geoCodeDate;
    }

    public void setGeoCodeDate(Date geoCodeDate) {
        this.geoCodeDate = geoCodeDate;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }

    @Override
    public int compareTo(Address o) {
        if (this.entity == o.entity) {
            if (Objects.equal(this.relation, o.relation)) {
                return ComparisonChain.start()
                        .compare(country, o.country, Ordering.natural().nullsFirst())
                        .compare(state, o.state, Ordering.natural().nullsFirst())
                        .compare(city, o.city, Ordering.natural().nullsFirst())
                        .compare(address1, o.address1, Ordering.natural().nullsFirst())
                        .compare(address2, o.address2, Ordering.natural().nullsFirst())
                        .result();
            } else {
                if (Address.RELATION_OTHER.equals(this.relation)) {
                    return 1;
                }
                if (Address.RELATION_OTHER.equals(o.relation)) {
                    return -1;
                }
                return this.relation.compareTo(o.relation);
            }
        } else {
            return this.entity > o.entity ? 1 : -1;
        }
    }

    public String joinAddress() {
        return Joiner.on(" ").skipNulls().join(address1, address2, city, state, postalCode);
    }

    public boolean shouldMerge(Address newAddress) {
        return (Strings.isNullOrEmpty(address1) || Objects.equal(getAddress1(), newAddress.getAddress1())) &&
                (Strings.isNullOrEmpty(address2) || Objects.equal(getAddress2(), newAddress.getAddress2())) &&
                (Strings.isNullOrEmpty(city) || Objects.equal(getCity(), newAddress.getCity())) &&
                (Strings.isNullOrEmpty(state) || Objects.equal(getState(), newAddress.getState())) &&
                (Strings.isNullOrEmpty(country) || Objects.equal(getCountry(), newAddress.getCountry())) &&
                (Strings.isNullOrEmpty(postalCode) || Objects.equal(getPostalCode(), newAddress.getPostalCode()));
    }

    /**
     * Performs a 'soft match' primarily for the benefit of Convio synchronization. Specifically we compare:
     * <p/>
     * * entity fields match
     * * all other fields match ignoring space and case
     * <p/>
     * We ignore the 'relation' field.
     */
    public boolean softMatch(Address address) {
        if (address == null) {
            return false;
        } else if (this.entity != address.entity) {
            return false;
        }
        // The general test is: 'both null' OR 'both not NULL and soft string matches'.
        else if (!((this.address1 == null && address.address1 == null) ||
                (this.address1 != null && address.address1 != null &&
                        regularizedStringsEquals(address1, address.address1)))) {
            return false;
        } else if (!((this.address2 == null && address.address2 == null) ||
                (this.address2 != null && address.address2 != null &&
                        regularizedStringsEquals(this.address2, address.address2)))) {
            return false;
        } else if (!((this.city == null && address.city == null) ||
                (this.city != null && address.city != null &&
                        regularizedStringsEquals(this.city, address.city)))) {
            return false;
        } else if (!((this.state == null && address.state == null) ||
                (this.state != null && address.state != null &&
                        regularizedStringsEquals(this.state, address.state)))) {
            return false;
        } else if (!((this.country == null && address.country == null) ||
                (this.country != null && address.country != null &&
                        regularizedStringsEquals(this.country, address.country)))) {
            return false;
        } else if (!((this.postalCode == null && address.postalCode == null) ||
                (this.postalCode != null && address.postalCode != null &&
                        regularizedStringsEquals(this.postalCode, address.postalCode)))) {
            return false;
        }

        return true;
    }

    private boolean regularizedStringsEquals(String string1, String string2) {
        String cleanedString1 = Strings.nullToEmpty(string1).replaceAll("\\s", "");
        String cleanedString2 = Strings.nullToEmpty(string2).replaceAll("\\s", "");

        return cleanedString1.equalsIgnoreCase(cleanedString2);
    }
}
