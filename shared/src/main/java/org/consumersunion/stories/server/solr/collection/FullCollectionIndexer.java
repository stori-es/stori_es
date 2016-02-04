package org.consumersunion.stories.server.solr.collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.persistence.CollectionPersister;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.SupportDataUtils;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;
import org.springframework.stereotype.Component;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

@Component
public class FullCollectionIndexer implements Indexer {
    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final CollectionPersister collectionPersister;

    private HttpServletResponse output = null;

    @Inject
    public FullCollectionIndexer(
            SupportDataUtilsFactory supportDataUtilsFactory,
            CollectionPersister collectionPersister) {
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.collectionPersister = collectionPersister;
    }

    public FullCollectionIndexer(
            HttpServletResponse output,
            SupportDataUtilsFactory supportDataUtilsFactory,
            CollectionPersister collectionPersister) {
        this.output = output;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.collectionPersister = collectionPersister;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        if (output != null) {
            try {
                output.getWriter().print("Collection indexing");
                output.flushBuffer();
                output.getOutputStream().flush();
            } catch (Exception e) {
            }
        }

        List<SolrInputDocument> collections = new ArrayList<SolrInputDocument>();
        Connection conn = null;
        try {
            conn = PersistenceUtil.getConnection();
            PreparedStatement collectionsStmt = conn.prepareStatement("SELECT id FROM collection");
            PreparedStatement tagsStmt = conn.prepareStatement("SELECT value FROM tag WHERE systemEntity=?");
            // TODO: Might make more sense to use 'supportDataUtils.getNonStoryAuths(...)' as in UpdateCollectionsAuths.
            PreparedStatement authsStmt = conn
                    .prepareStatement(
                            "SELECT DISTINCT(acl.sid) FROM collection c JOIN acl_entry acl ON c.id=acl" +
                                    ".acl_object_identity WHERE c.id=? AND acl.mask>=?");

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

                        authsStmt.setInt(1, collectionId);
                        authsStmt.setInt(2, ROLE_READER);
                        for (ResultSet authsResult = authsStmt.executeQuery(); authsResult.next(); ) {
                            collectionReadAuths.add(authsResult.getInt(1));
                        }
                        authsStmt.setInt(1, collectionId);
                        authsStmt.setInt(2, ROLE_CURATOR);
                        for (ResultSet authsResult = authsStmt.executeQuery(); authsResult.next(); ) {
                            collectionWriteAuths.add(authsResult.getInt(1));
                        }
                        authsStmt.setInt(1, collectionId);
                        authsStmt.setInt(2, ROLE_ADMIN);
                        for (ResultSet authsResult = authsStmt.executeQuery(); authsResult.next(); ) {
                            collectionAdminAuths.add(authsResult.getInt(1));
                        }

                        Set<String> admins = supportDataUtils.getAdminNames(collectionId);
                        CollectionDocument collectionDocument =
                                new CollectionDocument(collection, collectionTags, collectionReadAuths,
                                        collectionWriteAuths, collectionAdminAuths, admins);
                        collections.add(collectionDocument.toDocument());

                        if (collections.size() == BATCH_SIZE) {
                            solrCollectionServer.add(collections);
                            solrCollectionServer.commit();
                            collections = new ArrayList<SolrInputDocument>();
                            if (output != null) {
                                try {
                                    output.getWriter().print(".");
                                    output.flushBuffer();
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }
                }
            } finally {
                collectionsStmt.close();
            }

            if (collections.size() > 0) {
                solrCollectionServer.add(collections);
                solrCollectionServer.commit();
                if (output != null) {
                    try {
                        output.getWriter().println(" Done.");
                        output.flushBuffer();
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
