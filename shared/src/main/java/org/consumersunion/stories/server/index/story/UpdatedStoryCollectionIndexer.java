package org.consumersunion.stories.server.index.story;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.profile.ProfileDocument;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.ResultSetExtractor;
import org.consumersunion.stories.server.persistence.SupportDataUtils;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;
import org.springframework.stereotype.Component;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

@Component
public class UpdatedStoryCollectionIndexer {
    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final Indexer<StoryDocument> storyIndexer;
    private final Indexer<ProfileDocument> profileIndexer;

    @Inject
    UpdatedStoryCollectionIndexer(
            SupportDataUtilsFactory supportDataUtilsFactory,
            Indexer<StoryDocument> storyIndexer,
            Indexer<ProfileDocument> profileIndexer) {
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.storyIndexer = storyIndexer;
        this.profileIndexer = profileIndexer;
    }

    public void index(int storyId) {
        StoryDocument storyDocument = storyIndexer.get(storyId);
        Connection conn = PersistenceUtil.getConnection();
        try {
            SupportDataUtils dataUtils = supportDataUtilsFactory.create(conn);
            if (storyDocument != null) {
                storyDocument.setLastModified(new Date());
                storyDocument.setCollections(loadCollections(storyId));
                storyDocument.setCollectionsId(loadCollectionsId(storyId));
                storyDocument.setReadAuths(dataUtils.getStoryAuths(storyId, ROLE_READER));

                storyIndexer.index(storyDocument);

                updatePerson(conn, storyDocument.getAuthorId());
            }
        } catch (SQLException e) {
            throw new GeneralException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private void updatePerson(Connection conn, int authorId) throws SQLException {
        Set<Integer> collections = getCollections(conn, authorId);
        Set<Integer> questionnaires = getQuestionnaires(conn, authorId);

        ProfileDocument profileDocument = profileIndexer.get(authorId);
        if (profileDocument != null) {
            profileDocument.setCollections(collections);
            profileDocument.setQuestionnaires(questionnaires);

            profileIndexer.index(profileDocument);
        }
    }

    private Set<Integer> getQuestionnaires(Connection conn, Integer authorId) throws SQLException {
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

        Set<Integer> questionnaires = new HashSet<Integer>();
        while (rs.next()) {
            questionnaires.add(rs.getInt(1));
        }
        rs.close();

        return questionnaires;
    }

    private Set<Integer> getCollections(Connection conn, Integer authorId) throws SQLException {
        String collectionsSql = "SELECT DISTINCT(cs.collection) " +
                "FROM systemEntity se JOIN collection_story cs ON cs.story=se.id " +
                "WHERE se.owner=?";
        PreparedStatement collectionsStmt = conn.prepareStatement(collectionsSql);
        collectionsStmt.setInt(1, authorId);

        ResultSet rs = collectionsStmt.executeQuery();

        Set<Integer> collections = new HashSet<Integer>();
        while (rs.next()) {
            collections.add(rs.getInt(1));
        }
        rs.close();

        return collections;
    }

    private List<String> loadCollections(int storyId) throws SQLException {
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
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private Set<Integer> loadCollectionsId(int storyId) throws SQLException {
        Connection connection = null;
        try {
            connection = PersistenceUtil.getConnection();
            PreparedStatement collectionsStmt = connection.prepareStatement("SELECT cs.collection "
                    + "FROM collection_story cs WHERE cs.story = ?");

            collectionsStmt.setInt(1, storyId);
            return collectionsIdExtractor.extractData(collectionsStmt.executeQuery());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private final ResultSetExtractor<List<String>> collectionsExtractor = new CollectionsJsonExtractor();

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
