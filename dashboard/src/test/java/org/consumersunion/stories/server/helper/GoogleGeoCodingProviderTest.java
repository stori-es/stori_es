package org.consumersunion.stories.server.helper;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Address.GeoCodeStatus;
import org.consumersunion.stories.server.helper.geo.GeoCodingService;
import org.consumersunion.stories.server.helper.geo.Localisation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext.xml"})
public class GoogleGeoCodingProviderTest {
    private static final String ADDRESS = "2113 Juniata Gap Road";
    private static final String CITY = "Altoona";
    private static final String STATE = "PA";
    private static final String POSTAL_CODE = "16601-5301";
    private static final String COUNTRY = "US";

    @Autowired
    GeoCodingService geoCodingService;

    @Test
    public void testGoogleGeoCoderService() throws Exception {
        // given
        Address address = new Address();
        address.setAddress1(ADDRESS);
        address.setCity(CITY);
        address.setState(STATE);
        address.setPostalCode(POSTAL_CODE);
        address.setCountry(COUNTRY);

        // when
        Localisation localisation = geoCodingService.geoLocate(address);

        // then
        assertEquals(GeoCodeStatus.SUCCESS, localisation.getGeoCodeStatus());
        assertNotNull(localisation.getLatitude());
        assertNotNull(localisation.getLongitude());
    }
}
