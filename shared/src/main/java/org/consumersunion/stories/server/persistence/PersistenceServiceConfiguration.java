package org.consumersunion.stories.server.persistence;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceServiceConfiguration {
    @Inject
    private List<Persister> persisters;

    @PostConstruct
    public void init() {
        PersistenceUtil.setPersisters(persisters);
    }
}
