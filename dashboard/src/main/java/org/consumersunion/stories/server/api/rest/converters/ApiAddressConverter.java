package org.consumersunion.stories.server.api.rest.converters;

import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.ContactType;
import org.consumersunion.stories.common.shared.dto.Location;
import org.consumersunion.stories.common.shared.model.Address;
import org.springframework.stereotype.Component;

@Component
public class ApiAddressConverter extends AbstractConverter<ApiContact, Address> {
    @Override
    public Address convert(ApiContact contact) {
        if (contact.getContactType() != ContactType.GEOLOCATION) {
            return null;
        }

        Address address = new Address();
        Location location = contact.getLocation();

        address.setRelation(contact.getTitle());
        address.setAddress1(location.getStreetAddress1());
        address.setAddress2(location.getStreetAddress2());
        address.setCity(location.getCity());
        address.setState(location.getState());
        address.setCountry(location.getCountry());
        address.setPostalCode(location.getPostalCode());
        address.setLatitude(location.getLatitude());
        address.setLongitude(location.getLongitude());

        return address;
    }
}
