package org.consumersunion.stories.server.solr;

import org.apache.solr.client.solrj.SolrServer;

public interface Indexer {
    int BATCH_SIZE = 250;

    void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception;
}
