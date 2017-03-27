package org.consumersunion.stories.server.solr.collection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.SolrServer;
import org.consumersunion.stories.server.solr.SupportDataUtils;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class UpdatedCollectionIndexer implements Indexer {

    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final Collection collection;

    public UpdatedCollectionIndexer(
            SupportDataUtilsFactory supportDataUtilsFactory,
            Collection collection) {
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.collection = collection;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        SolrQuery query = new SolrQuery("id:" + collection.getId());

        Connection connection = PersistenceUtil.getConnection();
        try {
            SupportDataUtils supportDataUtils = supportDataUtilsFactory.create(connection);

            QueryResponse result = solrCollectionServer.query(query);
            if (!result.getResults().isEmpty()) {
                CollectionDocument collectionDocument = new CollectionDocument(result.getResults().get(0));
                collectionDocument.setDeleted(collection.getDeleted());
                collectionDocument.setTitle(collection.getTitle());
                collectionDocument.setLastModified(new Date());
                collectionDocument.setReadAuths(
                        supportDataUtils.getNonStoryAuths(collection.getId(), ROLE_READER));
                collectionDocument.setWriteAuths(
                        supportDataUtils.getNonStoryAuths(collection.getId(), ROLE_CURATOR));
                collectionDocument.setAdminAuths(
                        supportDataUtils.getNonStoryAuths(collection.getId(), ROLE_ADMIN));

                solrCollectionServer.add(collectionDocument.toDocument());
                solrCollectionServer.commit();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    public String toString() {
        return "-- Indexing new updated collection for " + collection.getId();
    }
}
