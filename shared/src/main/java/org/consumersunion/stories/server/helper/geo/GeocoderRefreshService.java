package org.consumersunion.stories.server.helper.geo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.consumersunion.stories.server.persistence.PersistenceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.consumersunion.stories.server.persistence.ContactPersister.RetrieveRefreshAddresses;

@Component
public class GeocoderRefreshService {
    private static final int NB_DAYS = 30;
    private static final int MAX_RESULTS = 10000;
    private final static Logger logger = Logger.getLogger(GeocoderRefreshService.class.getName());

    @Inject
    private GoogleGeoCodingProvider geoCodingProvider;
    @Inject
    private PersistenceService persistenceService;

    // Triggers every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void refreshGeoCodes() {
        if ("TRUE".equals(System.getProperty("sys.noGeoCode"))) {
            List<Address> addresses = persistenceService.process(new RetrieveRefreshAddresses(NB_DAYS, MAX_RESULTS));

            for (Address address : addresses) {
                try {
                    Localisation localisation = geoCodingProvider.geoLocate(address);
                    localisation.updateAddress(address);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }

            persistenceService.process(new ContactPersister.UpdateAddress(addresses));
        }
    }
}
