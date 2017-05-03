package org.consumersunion.stories.server.index.profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.SupportDataUtils;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;
import org.springframework.stereotype.Component;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.FETCH_FORWARD;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.server.index.Indexer.BATCH_SIZE;

@Component
public class FullPersonIndexer {
    private final Indexer<ProfileDocument> profileIndexer;
    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final HttpServletResponse output;

    @Inject
    public FullPersonIndexer(
            Indexer<ProfileDocument> profileIndexer,
            SupportDataUtilsFactory supportDataUtilsFactory) {
        this(profileIndexer, supportDataUtilsFactory, null);
    }

    public FullPersonIndexer(
            Indexer<ProfileDocument> profileIndexer,
            SupportDataUtilsFactory supportDataUtilsFactory,
            HttpServletResponse output) {
        this.profileIndexer = profileIndexer;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.output = output;
    }

    public void index() throws Exception {
//        if (true) {
//            profileIndexer.deleteIndex();
//            return;
//        }

        print("Person indexing");

        // to debug, add 'LIMIT 100' or something
        String peopleSql = "SELECT id FROM profile";
        String personSql = "SELECT p.givenName, p.surname, u.handle " +
                "FROM profile p LEFT JOIN user u ON p.id=u.id WHERE p.id=?";
        String collectionsSql = "SELECT DISTINCT(cs.collection) " +
                "FROM systemEntity se JOIN collection_story cs ON cs.story=se.id " +
                "WHERE se.owner=?";
        String questionnairesSql = "SELECT DISTINCT(a.questionnaire) " +
                "FROM collection c " +
                "JOIN collection_story cs ON cs.collection=c.id " +
                "JOIN systemEntity se ON cs.story=se.id " +
                "JOIN document d ON d.systemEntity=cs.story " +
                "JOIN answerSet a ON d.id=a.id " +
                "WHERE se.owner=?";
        String byCollectionSql = "SELECT cs.collection, MIN(se.created), MAX(se.created), COUNT(*) " +
                "FROM systemEntity se JOIN collection_story cs ON se.id=cs.story " +
                "WHERE se.owner=? GROUP BY cs.collection";

        List<ProfileDocument> profiles = new ArrayList<ProfileDocument>();
        Connection conn = null;
        try {
            conn = PersistenceUtil.getConnection();
            PreparedStatement peopleStmt = conn.prepareStatement(peopleSql, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);
            PreparedStatement personStmt = conn.prepareStatement(personSql);
            PreparedStatement collectionsStmt = conn.prepareStatement(collectionsSql);
            PreparedStatement questionnairesStmt = conn.prepareStatement(questionnairesSql);
            PreparedStatement byCollectionStmt = conn.prepareStatement(byCollectionSql);
            SupportDataUtils supportDataUtils = supportDataUtilsFactory.create(conn);

            try {
                peopleStmt.setFetchSize(BATCH_SIZE);
                peopleStmt.setFetchDirection(FETCH_FORWARD);
                ResultSet peopleResults = peopleStmt.executeQuery();
                peopleResults.setFetchSize(BATCH_SIZE);
                peopleResults.setFetchDirection(FETCH_FORWARD);
                while (peopleResults.next()) {
                    int id = peopleResults.getInt(1);
                    personStmt.setInt(1, id);
                    ResultSet personResult = personStmt.executeQuery();
                    if (!personResult.next()) {
                        continue;
                    }

                    String givenName = personResult.getString(1);
                    String surname = personResult.getString(2);
                    String fullName = givenName + " " + surname;

                    List<String> emails = supportDataUtils.getEmailsFor(id);
                    List<String> phones = supportDataUtils.getPhonesFor(id);

                    Address primaryAddress = supportDataUtils.getPrimaryAddress(id);
                    Date[] firstLastStoryDates = supportDataUtils.getFirstLastStoryDate(id);

                    Set<Integer> collections = new HashSet<Integer>();
                    collectionsStmt.setInt(1, id);
                    ResultSet rs = collectionsStmt.executeQuery();
                    while (rs.next()) {
                        collections.add(rs.getInt(1));
                    }
                    rs.close();

                    Set<Integer> questionnaires = new HashSet<Integer>();
                    questionnairesStmt.setInt(1, id);
                    rs = questionnairesStmt.executeQuery();
                    while (rs.next()) {
                        questionnaires.add(rs.getInt(1));
                    }
                    rs.close();

                    Map<Integer, Date> firstStoryDateByCollection = new HashMap<Integer, Date>();
                    Map<Integer, Date> lastStoryDateByCollection = new HashMap<Integer, Date>();
                    Map<Integer, Integer> storyCountByCollection = new HashMap<Integer, Integer>();
                    byCollectionStmt.setInt(1, id);
                    rs = byCollectionStmt.executeQuery();
                    while (rs.next()) {
                        int collectionId = rs.getInt(1);
                        firstStoryDateByCollection.put(collectionId, rs.getTimestamp(2));
                        lastStoryDateByCollection.put(collectionId, rs.getTimestamp(3));
                        storyCountByCollection.put(collectionId, rs.getInt(4));
                    }
                    rs.close();

                    Set<Integer> readAuths = supportDataUtils.getNonStoryAuths(id, ROLE_READER);

                    ProfileDocument profileDocument = new ProfileDocument(id, givenName, surname, fullName,
                            personResult.getString(3), // handle
                            emails.size() > 0 ? emails.get(0) : null,
                            emails,
                            supportDataUtils.getPreferredEmailFormat(id),
                            primaryAddress,
                            phones.size() > 0 ? phones.get(0) : null,
                            phones,
                            supportDataUtils.getUpdatesOptIn(id),
                            collections,
                            questionnaires,
                            firstLastStoryDates[0],
                            firstLastStoryDates[1],
                            supportDataUtils.getStoryCount(id),
//                            firstStoryDateByCollection,
//                            lastStoryDateByCollection,
//                            storyCountByCollection,
                            readAuths);

                    profiles.add(profileDocument);

                    if (profiles.size() == BATCH_SIZE) {
                        profileIndexer.indexAsync(profiles);
                        profiles = new ArrayList<ProfileDocument>();
                        print(".");
                    }
                }
            } finally {
                peopleStmt.close();
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        if (!profiles.isEmpty()) {
            profileIndexer.index(profiles);
        }
        print("Done.");
    }

    private void print(String message) {
        if (output != null) {
            try {
                output.getWriter().print(message);
                output.flushBuffer();
            } catch (Exception ignored) {
            }
        }
    }
}
