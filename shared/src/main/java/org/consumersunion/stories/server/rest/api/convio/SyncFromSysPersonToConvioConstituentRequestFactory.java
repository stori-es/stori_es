package org.consumersunion.stories.server.rest.api.convio;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.server.business_logic.AuthorizationService;
import org.consumersunion.stories.server.persistence.PersistenceService;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;
import org.springframework.stereotype.Component;

@Component
public class SyncFromSysPersonToConvioConstituentRequestFactory {
    private final PersistenceService persistenceService;
    private final AuthorizationService authService;
    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final ConvioDataSynchronizationWorker worker;

    @Inject
    SyncFromSysPersonToConvioConstituentRequestFactory(
            PersistenceService persistenceService,
            AuthorizationService authService,
            SupportDataUtilsFactory supportDataUtilsFactory,
            ConvioDataSynchronizationWorker worker) {
        this.persistenceService = persistenceService;
        this.authService = authService;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.worker = worker;
    }

    public SyncFromSysPersonToConvioConstituentRequest create(Profile profile) {
        return new SyncFromSysPersonToConvioConstituentRequest(this, persistenceService, authService,
                supportDataUtilsFactory, worker, profile);
    }
}
