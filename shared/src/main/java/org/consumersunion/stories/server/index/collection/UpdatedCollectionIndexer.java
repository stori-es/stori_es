package org.consumersunion.stories.server.index.collection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.SupportDataUtils;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;
import org.springframework.stereotype.Component;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

@Component
public class UpdatedCollectionIndexer {
    private final Indexer<CollectionDocument> collectionIndexer;
    private final SupportDataUtilsFactory supportDataUtilsFactory;

    @Inject
    public UpdatedCollectionIndexer(
            Indexer<CollectionDocument> collectionIndexer,
            SupportDataUtilsFactory supportDataUtilsFactory) {
        this.collectionIndexer = collectionIndexer;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
    }

    public void index(Collection collection) {
        Connection connection = PersistenceUtil.getConnection();
        try {
            SupportDataUtils supportDataUtils = supportDataUtilsFactory.create(connection);

            CollectionDocument collectionDocument = collectionIndexer.get(collection.getId());
            if (collectionDocument != null) {
                collectionDocument.setDeleted(collection.getDeleted());
                collectionDocument.setTitle(collection.getTitle());
                collectionDocument.setLastModified(new Date());
                collectionDocument.setReadAuths(
                        supportDataUtils.getNonStoryAuths(collection.getId(), ROLE_READER));
                collectionDocument.setWriteAuths(
                        supportDataUtils.getNonStoryAuths(collection.getId(), ROLE_CURATOR));
                collectionDocument.setAdminAuths(
                        supportDataUtils.getNonStoryAuths(collection.getId(), ROLE_ADMIN));

                collectionIndexer.index(collectionDocument);
            }
        } catch (SQLException e) {
            rollback(connection);
        } finally {
            closeConnection(connection);
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }
}
