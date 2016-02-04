package org.consumersunion.stories.server.solr;

import java.sql.Connection;

import javax.inject.Inject;

import org.consumersunion.stories.server.business_logic.SystemEntityService;
import org.springframework.stereotype.Component;

@Component
public class SupportDataUtilsFactory {
    private final SystemEntityService systemEntityService;

    @Inject
    SupportDataUtilsFactory(SystemEntityService systemEntityService) {
        this.systemEntityService = systemEntityService;
    }

    public SupportDataUtils create(Connection connection) {
        return new SupportDataUtils(systemEntityService, connection);
    }
}
