package org.consumersunion.stories.server.solr.story;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.consumersunion.stories.server.solr.AuthIndexer;
import org.consumersunion.stories.server.solr.SolrServer;
import org.consumersunion.stories.server.solr.SupportDataUtils;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class UpdateStoriesAuthsIndexer extends AuthIndexer {
    public UpdateStoriesAuthsIndexer(
            SupportDataUtilsFactory supportDataUtilsFactory,
            List<Integer> stories,
            Connection conn) {
        super(supportDataUtilsFactory, stories, conn);
    }

    @Override
    protected SolrInputDocument getDocument(
            SolrDocument solrDocument,
            SupportDataUtils supportDataUtils) throws SQLException {
        IndexedStoryDocument storyDocument = new IndexedStoryDocument(solrDocument);
        storyDocument.setReadAuths(supportDataUtils.getStoryAuths(storyDocument.getId(), ROLE_READER));

        return storyDocument.toDocument();
    }

    @Override
    protected SolrServer getEffectiveServer(
            SolrServer solrStoryServer,
            SolrServer solrCollectionServer,
            SolrServer solrPersonServer) {
        return solrStoryServer;
    }

    public String toString() {
        return "-- Indexing updated auths for stories";
    }
}
