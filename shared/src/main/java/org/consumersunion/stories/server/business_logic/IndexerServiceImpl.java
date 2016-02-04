package org.consumersunion.stories.server.business_logic;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.server.solr.CoreIndexer;
import org.consumersunion.stories.server.solr.Indexer;
import org.springframework.stereotype.Service;

@Service
public class IndexerServiceImpl implements IndexerService {
    private final Provider<CoreIndexer> coreIndexerProvider;

    @Inject
    IndexerServiceImpl(Provider<CoreIndexer> coreIndexerProvider) {
        this.coreIndexerProvider = coreIndexerProvider;
    }

    @Override
    public void process(Indexer indexer) {
        CoreIndexer coreIndexer = coreIndexerProvider.get();

        coreIndexer.process(indexer);
    }

    @Override
    public void processManual(Indexer indexer) {
        CoreIndexer coreIndexer = coreIndexerProvider.get();

        coreIndexer.processManual(indexer);
    }
}
