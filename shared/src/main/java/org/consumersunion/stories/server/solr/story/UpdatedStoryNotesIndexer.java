package org.consumersunion.stories.server.solr.story;

import java.util.Date;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.SolrServer;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;

public class UpdatedStoryNotesIndexer implements Indexer {
    private final Integer storyId;
    private final String documentText;

    public UpdatedStoryNotesIndexer(Integer storyId, String documentText) {
        this.storyId = storyId;
        this.documentText = documentText;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        SolrQuery query = new SolrQuery("id:" + storyId);

        QueryResponse result = solrStoryServer.query(query);
        if (result.getResults().size() > 0) {
            IndexedStoryDocument storyDocument = new IndexedStoryDocument(result.getResults().get(0));
            storyDocument.getStoryNotes().add(documentText);
            storyDocument.setLastModified(new Date());

            solrStoryServer.add(storyDocument.toDocument());
            solrStoryServer.commit();
        }
    }

    public String toString() {
        return "-- Indexing new Note for " + storyId;
    }
}
