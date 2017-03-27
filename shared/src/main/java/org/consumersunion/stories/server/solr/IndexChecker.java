package org.consumersunion.stories.server.solr;

public interface IndexChecker {
    void check(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception;
}
