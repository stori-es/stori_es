package org.consumersunion.stories.server.index.collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.persistence.CollectionPersister;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.SupportDataUtils;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;
import org.springframework.stereotype.Component;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.server.index.Indexer.BATCH_SIZE;

@Component
public class FullCollectionIndexer {
    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final CollectionPersister collectionPersister;
    private final Indexer<CollectionDocument> collectionIndexer;

    private HttpServletResponse output;

    @Inject
    public FullCollectionIndexer(
            Indexer<CollectionDocument> collectionIndexer,
            SupportDataUtilsFactory supportDataUtilsFactory,
            CollectionPersister collectionPersister) {
        this.collectionIndexer = collectionIndexer;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.collectionPersister = collectionPersister;
    }

    public FullCollectionIndexer(
            HttpServletResponse output,
            SupportDataUtilsFactory supportDataUtilsFactory,
            CollectionPersister collectionPersister,
            Indexer<CollectionDocument> collectionIndexer) {
        this.output = output;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.collectionPersister = collectionPersister;
        this.collectionIndexer = collectionIndexer;
    }

    public void index()
            throws Exception {
        print("Collection indexing");

        List<CollectionDocument> collections = new ArrayList<CollectionDocument>();
        Connection conn = null;
        try {
            conn = PersistenceUtil.getConnection();
            PreparedStatement collectionsStmt = conn.prepareStatement("SELECT id FROM collection");
            PreparedStatement tagsStmt = conn.prepareStatement("SELECT value FROM tag WHERE systemEntity=?");

            SupportDataUtils supportDataUtils = supportDataUtilsFactory.create(conn);

            try {
                for (ResultSet collectionResults = collectionsStmt.executeQuery(); collectionResults.next(); ) {
                    int collectionId = collectionResults.getInt(1);
                    Collection collection = null;

                    try {
                        collection = collectionPersister.get(collectionId);
                    } catch (NotFoundException ignored) {
                    }

                    if (collection != null) {
                        Set<String> collectionTags = new LinkedHashSet<String>();
                        Set<Integer> collectionReadAuths = new LinkedHashSet<Integer>();
                        Set<Integer> collectionWriteAuths = new LinkedHashSet<Integer>();
                        Set<Integer> collectionAdminAuths = new LinkedHashSet<Integer>();

                        tagsStmt.setInt(1, collectionId);
                        for (ResultSet tagsResult = tagsStmt.executeQuery(); tagsResult.next(); ) {
                            collectionTags.add(tagsResult.getString(1));
                        }

                        collectionReadAuths.addAll(supportDataUtils.getNonStoryAuths(collectionId, ROLE_READER));
                        collectionWriteAuths.addAll(supportDataUtils.getNonStoryAuths(collectionId, ROLE_CURATOR));
                        collectionAdminAuths.addAll(supportDataUtils.getNonStoryAuths(collectionId, ROLE_ADMIN));

                        Set<String> admins = supportDataUtils.getAdminNames(collectionId);
                        CollectionDocument collectionDocument = new CollectionDocument(collection, collectionTags,
                                collectionReadAuths, collectionWriteAuths, collectionAdminAuths, admins);
                        collections.add(collectionDocument);

                        if (collections.size() == BATCH_SIZE) {
                            collectionIndexer.index(collections);
                            collections = new ArrayList<CollectionDocument>();
                            print(".");
                        }
                    }
                }
            } finally {
                collectionsStmt.close();
                tagsStmt.close();
            }

            if (collections.size() > 0) {
                collectionIndexer.index(collections);
                print("Done.");
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    private void print(String content) {
        if (output != null) {
            try {
                output.getWriter().print(content);
                output.flushBuffer();
                output.getOutputStream().flush();
            } catch (Exception ignored) {
            }
        }
    }
}
