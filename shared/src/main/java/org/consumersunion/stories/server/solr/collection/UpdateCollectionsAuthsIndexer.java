package org.consumersunion.stories.server.solr.collection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.consumersunion.stories.server.solr.AuthIndexer;
import org.consumersunion.stories.server.solr.SupportDataUtils;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class UpdateCollectionsAuthsIndexer extends AuthIndexer {
    public UpdateCollectionsAuthsIndexer(
            SupportDataUtilsFactory supportDataUtilsFactory,
            List<Integer> collections,
            Connection conn) {
        super(supportDataUtilsFactory, collections, conn);
    }

    @Override
    protected SolrInputDocument getDocument(SolrDocument solrDocument, SupportDataUtils supportDataUtils)
            throws SQLException {
        CollectionDocument collectionDocument = new CollectionDocument(solrDocument);
        collectionDocument.setReadAuths(
                supportDataUtils.getNonStoryAuths(collectionDocument.getId(), ROLE_READER));
        collectionDocument.setWriteAuths(
                supportDataUtils.getNonStoryAuths(collectionDocument.getId(), ROLE_CURATOR));
        collectionDocument.setAdminAuths(
                supportDataUtils.getNonStoryAuths(collectionDocument.getId(),
                        ROLE_ADMIN)
        );

        return collectionDocument.toDocument();
    }

    @Override
    protected SolrServer getEffectiveServer(SolrServer solrStoryServer,
            SolrServer solrCollectionServer,
            SolrServer solrPersonServer) {
        return solrCollectionServer;
    }

    public String toString() {
        return "-- Indexing updated auths for collections";
    }
}
