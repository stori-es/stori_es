package org.consumersunion.stories.server.persistence;

import java.sql.SQLException;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.consumersunion.stories.server.solr.SupportDataUtils;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthPersister {
    private final PersistenceService persistenceService;
    private final SupportDataUtilsFactory supportDataUtilsFactory;

    @Inject
    AuthPersister(
            PersistenceService persistenceService,
            SupportDataUtilsFactory supportDataUtilsFactory) {
        this.persistenceService = persistenceService;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
    }

    public Set<Integer> getNonStoryAuths(int id, final int minRole) {
        return persistenceService.process(new RetrieveFunc<Set<Integer>>(id) {
            @Override
            protected Set<Integer> retrieveConcrete() throws SQLException {
                SupportDataUtils dataUtils = supportDataUtilsFactory.create(conn);

                return dataUtils.getNonStoryAuths(input, minRole);
            }
        });
    }
}
