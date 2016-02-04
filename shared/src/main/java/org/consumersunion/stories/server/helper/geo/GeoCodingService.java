package org.consumersunion.stories.server.helper.geo;

import org.consumersunion.stories.common.shared.model.Address;

public interface GeoCodingService {
    Localisation geoLocate(Address address) throws Exception;
}
