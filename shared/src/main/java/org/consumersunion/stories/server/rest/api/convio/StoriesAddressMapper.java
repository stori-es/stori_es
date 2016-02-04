package org.consumersunion.stories.server.rest.api.convio;

import org.consumersunion.stories.common.server.service.datatransferobject.ConvioAddressObject;
import org.consumersunion.stories.common.shared.model.Address;
import org.modelmapper.PropertyMap;

public class StoriesAddressMapper extends PropertyMap<ConvioAddressObject, Address> {
    protected void configure() {
        map().setAddress1(source.getStreet1());
        map().setAddress2(source.getStreet2());
        map().setCountry(source.getCountry());
        map().setCity(source.getCity());
        map().setState(source.getState());
        map().setPostalCode(source.getZip());
    }
}
