package org.consumersunion.stories.server.solr;

import org.apache.solr.client.solrj.SolrServer;

public interface IndexChecker {
    void check(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception;
}
