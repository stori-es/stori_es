package org.consumersunion.stories.common.server.service.datatransferobject;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.consumersunion.stories.common.shared.model.Address;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @see {@linktourl https://docs.google.com/a/navigo
 * .com/spreadsheet/ccc?key=0AuOmo8TRihR8dEFsQzNxT2J6SFY1YUZQQmNpRjAxTUE#gid=0"}
 */
public class ConvioAddressObject implements IsSerializable {
    private String street2;
    @JsonIgnore
    private String street3;
    private String country;
    private String street1;
    private String state;
    private String zip;
    private String city;

    public static ConvioAddressObject fromSysAddress(Address address) {
        final ConvioAddressObject convioAddress = new ConvioAddressObject();
        convioAddress.setStreet1(address.getAddress1());
        convioAddress.setStreet2(address.getAddress2());
        convioAddress.setCity(address.getCity());
        convioAddress.setState(address.getState());
        convioAddress.setZip(address.getPostalCode());
        convioAddress.setCountry(address.getCountry());

        return convioAddress;
    }

    public String getStreet3() {
        return street3;
    }

    public void setStreet3(String street3) {
        this.street3 = street3;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
