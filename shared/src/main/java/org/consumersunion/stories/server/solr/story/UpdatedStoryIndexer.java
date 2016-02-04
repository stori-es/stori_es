package org.consumersunion.stories.server.solr.story;

import java.util.Date;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;

public class UpdatedStoryIndexer implements Indexer {
    private final Story story;

    public UpdatedStoryIndexer(Story story) {
        this.story = story;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        SolrQuery query = new SolrQuery("id:" + story.getId());

        QueryResponse result = solrStoryServer.query(query);
        if (result.getResults().size() > 0) {
            IndexedStoryDocument storyDocument = new IndexedStoryDocument(result.getResults().get(0));
            storyDocument.setOwner(story.getOwner());
            storyDocument.setPermalink(story.getPermalink());
            storyDocument.setLastModified(new Date());
            storyDocument.setStoryVersion(story.getVersion());

            solrStoryServer.add(storyDocument.toDocument());
            solrStoryServer.commit();
        }
    }

    public String toString() {
        return "-- Indexing new updated story for " + story.getId();
    }
}
