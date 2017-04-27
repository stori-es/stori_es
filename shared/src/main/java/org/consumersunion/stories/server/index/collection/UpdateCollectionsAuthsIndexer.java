package org.consumersunion.stories.server.index.collection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.consumersunion.stories.server.index.AuthIndexer;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.persistence.SupportDataUtils;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class UpdateCollectionsAuthsIndexer extends AuthIndexer<CollectionDocument> {
    private final Indexer<CollectionDocument> collectionIndexer;

    public UpdateCollectionsAuthsIndexer(
            Indexer<CollectionDocument> collectionIndexer,
            SupportDataUtilsFactory supportDataUtilsFactory,
            List<Integer> collections,
            Connection conn) {
        super(supportDataUtilsFactory, collections, conn);

        this.collectionIndexer = collectionIndexer;
    }

    @Override
    protected Indexer<CollectionDocument> getIndexer() {
        return collectionIndexer;
    }

    @Override
    protected void updateAuths(CollectionDocument collectionDocument, SupportDataUtils supportDataUtils)
            throws SQLException {
        collectionDocument.setReadAuths(
                supportDataUtils.getNonStoryAuths(collectionDocument.getId(), ROLE_READER));
        collectionDocument.setWriteAuths(
                supportDataUtils.getNonStoryAuths(collectionDocument.getId(), ROLE_CURATOR));
        collectionDocument.setAdminAuths(
                supportDataUtils.getNonStoryAuths(collectionDocument.getId(), ROLE_ADMIN)
        );
    }
}
