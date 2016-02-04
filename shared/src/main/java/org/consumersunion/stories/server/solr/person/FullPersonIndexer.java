package org.consumersunion.stories.server.solr.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.SupportDataUtils;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;
import org.springframework.stereotype.Component;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

@Component
public class FullPersonIndexer implements Indexer {
    private final SupportDataUtilsFactory supportDataUtilsFactory;
    private final HttpServletResponse output;

    @Inject
    public FullPersonIndexer(SupportDataUtilsFactory supportDataUtilsFactory) {
        this(supportDataUtilsFactory, null);
    }

    public FullPersonIndexer(SupportDataUtilsFactory supportDataUtilsFactory, HttpServletResponse output) {
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.output = output;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        if (output != null) {
            try {
                output.getWriter().print("Collection indexing");
                output.flushBuffer();
            } catch (Exception ignored) {
            }
        }

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

        List<SolrInputDocument> people = new ArrayList<SolrInputDocument>();
        Connection conn = null;
        try {
            conn = PersistenceUtil.getConnection();
            PreparedStatement peopleStmt = conn.prepareStatement(peopleSql);
            PreparedStatement personStmt = conn.prepareStatement(personSql);
            PreparedStatement collectionsStmt = conn.prepareStatement(collectionsSql);
            PreparedStatement questionnairesStmt = conn.prepareStatement(questionnairesSql);
            PreparedStatement byCollectionStmt = conn.prepareStatement(byCollectionSql);
            SupportDataUtils supportDataUtils = supportDataUtilsFactory.create(conn);

            try {
                for (ResultSet peopleResults = peopleStmt.executeQuery(); peopleResults.next(); ) {
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

                    Collection<Integer> collections = new HashSet<Integer>();
                    collectionsStmt.setInt(1, id);
                    ResultSet rs = collectionsStmt.executeQuery();
                    while (rs.next()) {
                        collections.add(rs.getInt(1));
                    }
                    rs.close();

                    Collection<Integer> questionnaires = new HashSet<Integer>();
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
                            firstStoryDateByCollection,
                            lastStoryDateByCollection,
                            storyCountByCollection,
                            readAuths);

                    people.add(profileDocument.toDocument());

                    if (people.size() == BATCH_SIZE) {
                        solrPersonServer.add(people);
                        solrPersonServer.commit();
                        people = new ArrayList<SolrInputDocument>();
                        if (output != null) {
                            try {
                                output.getWriter().print(".");
                                output.flushBuffer();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            } finally {
                peopleStmt.close();
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        if (!people.isEmpty()) {
            solrPersonServer.add(people);
            solrPersonServer.commit();
        }
        if (output != null) {
            try {
                output.getWriter().println(" Done.");
                output.flushBuffer();
            } catch (Exception e) {
            }
        }
    }

    public String toString() {
        return "-- Full indexation of people";
    }
}
