package org.consumersunion.stories.server.solr.story;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.ResultSetExtractor;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.SupportDataUtils;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;
import org.consumersunion.stories.server.solr.person.ProfileDocument;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;
import org.json.JSONObject;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class UpdatedStoryCollectionIndexer implements Indexer {
    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final int storyId;

    public UpdatedStoryCollectionIndexer(
            SupportDataUtilsFactory supportDataUtilsFactory,
            int storyId) {
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.storyId = storyId;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        SolrQuery query = new SolrQuery("id:" + storyId);

        QueryResponse result = solrStoryServer.query(query);

        Connection conn = PersistenceUtil.getConnection();
        SupportDataUtils dataUtils = supportDataUtilsFactory.create(conn);
        try {
            if (!result.getResults().isEmpty()) {
                IndexedStoryDocument storyDocument = new IndexedStoryDocument(result.getResults().get(0));
                storyDocument.setLastModified(new Date());
                storyDocument.setCollections(loadCollections());
                storyDocument.setCollectionsId(loadCollectionsId());
                storyDocument.setReadAuths(dataUtils.getStoryAuths(storyId, ROLE_READER));

                solrStoryServer.add(storyDocument.toDocument());
                solrStoryServer.commit();

                updatePerson(conn, storyDocument.getAuthorId(), solrPersonServer);
            }
        } finally {
            conn.close();
        }
    }

    public String toString() {
        return "-- Indexing new collections for " + storyId;
    }

    private void updatePerson(Connection conn, Integer authorId, SolrServer solrPersonServer)
            throws SQLException, SolrServerException, IOException {
        Collection<Integer> collections = getCollections(conn, authorId);
        Collection<Integer> questionnaires = getQuestionnaires(conn, authorId);

        SolrQuery query = new SolrQuery("id:" + authorId);

        QueryResponse result = solrPersonServer.query(query);
        if (result.getResults().size() > 0) {
            ProfileDocument profileDocument = new ProfileDocument(result.getResults().get(0));
            profileDocument.setCollections(collections);
            profileDocument.setQuestionnaires(questionnaires);

            solrPersonServer.add(profileDocument.toDocument());
            solrPersonServer.commit();
        }
    }

    private Collection<Integer> getQuestionnaires(Connection conn, Integer authorId) throws SQLException {
        String questionnairesSql = "SELECT DISTINCT(a.questionnaire) " +
                "FROM collection c " +
                "JOIN collection_story cs ON cs.collection=c.id " +
                "JOIN systemEntity se ON cs.story=se.id " +
                "JOIN document d ON d.systemEntity=cs.story " +
                "JOIN answerSet a ON d.id=a.id " +
                "WHERE se.owner=?";
        PreparedStatement questionnairesStmt = conn.prepareStatement(questionnairesSql);
        questionnairesStmt.setInt(1, authorId);

        ResultSet rs = questionnairesStmt.executeQuery();

        Collection<Integer> questionnaires = new HashSet<Integer>();
        while (rs.next()) {
            questionnaires.add(rs.getInt(1));
        }
        rs.close();

        return questionnaires;
    }

    private Collection<Integer> getCollections(Connection conn, Integer authorId) throws SQLException {
        String collectionsSql = "SELECT DISTINCT(cs.collection) " +
                "FROM systemEntity se JOIN collection_story cs ON cs.story=se.id " +
                "WHERE se.owner=?";
        PreparedStatement collectionsStmt = conn.prepareStatement(collectionsSql);
        collectionsStmt.setInt(1, authorId);

        ResultSet rs = collectionsStmt.executeQuery();

        Collection<Integer> collections = new HashSet<Integer>();
        while (rs.next()) {
            collections.add(rs.getInt(1));
        }
        rs.close();

        return collections;
    }

    private List<String> loadCollections() throws Exception {
        Connection connection = null;
        try {
            connection = PersistenceUtil.getConnection();
            PreparedStatement collectionsStmt = connection.prepareStatement("SELECT e.id, d.title, MAX(de.version) "
                    + "FROM systemEntity e "
                    + "JOIN collection c ON e.id=c.id "
                    + "JOIN entity n ON e.id=n.id "
                    + "JOIN collection_story cs ON c.id = cs.collection "
                    + "JOIN document d ON d.systemEntity=c.id AND d.systemEntityRelation='BODY' "
                    + "JOIN systemEntity de ON de.id=d.id AND d.version=de.version "
                    + "WHERE cs.story = ? "
                    + "GROUP BY de.id");

            collectionsStmt.setInt(1, storyId);
            return collectionsExtractor.extractData(collectionsStmt.executeQuery());
        } catch (SQLException e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private Set<Integer> loadCollectionsId() throws Exception {
        Connection connection = null;
        try {
            connection = PersistenceUtil.getConnection();
            PreparedStatement collectionsStmt = connection.prepareStatement("SELECT cs.collection "
                    + "FROM collection_story cs WHERE cs.story = ?");

            collectionsStmt.setInt(1, storyId);
            return collectionsIdExtractor.extractData(collectionsStmt.executeQuery());
        } catch (SQLException e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private final ResultSetExtractor<List<String>> collectionsExtractor = new ResultSetExtractor<List<String>>() {
        @Override
        public List<String> extractData(ResultSet resultSet) throws SQLException {
            List<String> collections = new ArrayList<String>();
            while (resultSet.next()) {
                try {
                    JSONObject collection = new JSONObject();
                    collection.put("id", resultSet.getInt(1));
                    collection.put("title", resultSet.getString(2));
                    collections.add(collection.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            resultSet.close();

            return collections;
        }
    };

    private final ResultSetExtractor<Set<Integer>> collectionsIdExtractor = new ResultSetExtractor<Set<Integer>>() {
        @Override
        public Set<Integer> extractData(ResultSet resultSet) throws SQLException {
            Set<Integer> collections = new LinkedHashSet<Integer>();
            while (resultSet.next()) {
                collections.add(resultSet.getInt(1));
            }
            resultSet.close();

            return collections;
        }
    };
}
