package org.consumersunion.stories.server.solr.story;

import java.util.Date;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;

public class UpdatedStoryDocumentIndexer implements Indexer {
    private final Integer storyId;
    private final Document document;

    public UpdatedStoryDocumentIndexer(Integer storyId, Document document) {
        this.storyId = storyId;
        this.document = document;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        SolrQuery query = new SolrQuery("id:" + storyId);

        QueryResponse result = solrStoryServer.query(query);
        if (result.getResults().size() > 0) {
            IndexedStoryDocument storyDocument = new IndexedStoryDocument(result.getResults().get(0));

            if (document instanceof AnswerSet) {
                updateFromAnswerSet(storyDocument);
            } else {
                updateFromDocument(storyDocument);
            }

            solrStoryServer.add(storyDocument.toDocument());
            solrStoryServer.commit();
        }
    }

    private void updateFromAnswerSet(IndexedStoryDocument storyDocument) {
        storyDocument.setLastModified(document.getUpdated());
    }

    private void updateFromDocument(IndexedStoryDocument storyDocument) {
        if (document != null) {
            storyDocument.setTitle(document.getTitle());
            storyDocument.setPrimaryText(document.getFirstContent());
            storyDocument.setDefaultContentId(document.getId());
            storyDocument.setLastModified(document.getUpdated());
        } else {
            storyDocument.setTitle(null);
            storyDocument.setPrimaryText(null);
            storyDocument.setDefaultContentId(null);
            storyDocument.setLastModified(new Date());
        }
    }

    public String toString() {
        return "-- Indexing new content for " + storyId;
    }
}
