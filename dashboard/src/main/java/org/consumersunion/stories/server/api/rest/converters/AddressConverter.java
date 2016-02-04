package org.consumersunion.stories.server.api.rest.converters;

import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.ContactType;
import org.consumersunion.stories.common.shared.dto.Location;
import org.consumersunion.stories.common.shared.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter extends AbstractConverter<Address, ApiContact> {
    @Override
    public ApiContact convert(Address address) {
        ApiContact result = new ApiContact();

        result.setContactType(ContactType.GEOLOCATION);
        result.setTitle(address.getRelation());
        result.setLocation(Location.builder()
                .streetAddress1(address.getAddress1())
                .streetAddress2(address.getAddress2())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build());

        return result;
    }
}
