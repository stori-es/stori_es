package org.consumersunion.stories.server.business_logic;

import org.consumersunion.stories.server.solr.Indexer;

public interface IndexerService {
    void process(Indexer indexer);

    void processManual(Indexer indexer);
}
