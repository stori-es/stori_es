package org.consumersunion.stories.server.solr.story;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.server.persistence.AnswerSetPersister;
import org.consumersunion.stories.server.persistence.DocumentPersister;
import org.consumersunion.stories.server.persistence.DocumentPersister.EntityAndRelationParams;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.ResultSetExtractor;
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.SupportDataUtils;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.BODY;

@Component
public class FullStoryIndexer implements Indexer {
    private final HttpServletResponse output;
    private final DocumentPersister documentPersister;
    private final AnswerSetPersister answerSetPersister;
    private final SupportDataUtilsFactory supportDataUtilsFactory;

    @Inject
    FullStoryIndexer(
            DocumentPersister documentPersister,
            AnswerSetPersister answerSetPersister,
            SupportDataUtilsFactory supportDataUtilsFactory) {
        this.answerSetPersister = answerSetPersister;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.output = null;
        this.documentPersister = documentPersister;
    }

    public FullStoryIndexer(
            HttpServletResponse output,
            DocumentPersister documentPersister,
            AnswerSetPersister answerSetPersister,
            SupportDataUtilsFactory supportDataUtilsFactory) {
        this.documentPersister = documentPersister;
        this.output = output;
        this.answerSetPersister = answerSetPersister;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        if (output != null) {
            try {
                output.getWriter().print("Collection indexing");
                output.flushBuffer();
            } catch (Exception e) {
            }
        }

        String storySql = "SELECT id FROM story ORDER BY id ASC LIMIT " + BATCH_SIZE + " OFFSET ?";
        String storyDocSql = "SELECT s.id AS storyId," // 1
                + "d.primaryAuthor AS author_id," // 2
                + "d.permalink AS permalink," // 3
                // Yes, we want to pull the Document's 'public', and NOT Story. The question is whether we can show the
                // Document, not whether we can show the 'Story' in general (whatever that would mean).
                + "de.public AS public," // 4
                + "d.title AS title, " // 5
                + "bc.content AS text, " // 6
                + "p.surname AS surname, " // 7
                + "p.givenName AS givenName, " // 8
                + "d.id AS document_id, " // 9
                + "s.firstPublished, " // 10
                + "s.owner AS owner," // 11
                + "e.created," // 12
                + "e.lastModified, " // 13
                + "e.version, " // 14
                // LIMIT 1 is used here since we only query the ID, which doesn't change across document versions
                + "(SELECT a.id FROM document a WHERE a.systemEntity=s.id AND a.systemEntityRelation='ANSWER_SET' " +
                "LIMIT 1) AS answerSetId "
                + "FROM document d "
                + "JOIN systemEntity de ON d.id=de.id "
                + "JOIN profile p ON p.id=d.primaryAuthor "
                + "JOIN block_content bc ON d.id=bc.document AND d.version=bc.version "
                + "JOIN story s ON d.systemEntity = s.id "
                + "JOIN systemEntity e ON s.id=e.id "
                + "LEFT OUTER JOIN block_content bc2 ON (bc.document=bc2.document AND bc2.version > bc.version) "
                + "WHERE d.systemEntityRelation='BODY' AND bc2.document IS NULL AND s.id=?";
        String answerSetOnlySql = "SELECT s.id AS storyId," // 1
                + "d.primaryAuthor AS author_id," // 2
                + "d.permalink AS permalink," // 3
                + "e.public AS public," // 4
                + "NULL AS title, " // 5
                + "NULL AS text, " // 6
                + "p.surname AS surname, " // 7
                + "p.givenName AS givenName, " // 8
                + "d.id AS document_id, " // 9
                + "s.firstPublished, " // 10
                + "s.owner AS owner," // 11
                + "e.created," // 12
                + "e.lastModified, " // 13
                + "e.version, " // 14
                + // 15
                "(SELECT a.id FROM document a WHERE a.systemEntity=s.id AND a.systemEntityRelation='ANSWER_SET' LIMIT" +
                " 1) AS answerSetId "
                + "FROM document d " + "JOIN story s ON d.systemEntity = s.id "
                + "JOIN profile p ON p.id=d.primaryAuthor " + "JOIN systemEntity e ON s.id=e.id "
                + "WHERE d.systemEntityRelation='ANSWER_SET' AND s.id=?";
        String answerSetTextSql = "SELECT a.label, a.reportValue " +
                "FROM answer a " +
                "JOIN answerSet aSet ON a.answerSet=aSet.id " +
                "JOIN document d ON aSet.id=d.id " +
                "JOIN story s ON s.id=d.systemEntity " +
                "WHERE reportValue!='' AND s.id=? ORDER BY a.idx";
        String storyDocumentSql = "SELECT se.id, se.version "
                + " FROM document d JOIN systemEntity se ON d.id=se.id LEFT JOIN profile p ON d.primaryAuthor=p.id "
                + " WHERE d.systemEntity=? AND systemEntityRelation=?";
        String storyTextSql = "SELECT bc1.content FROM block_content bc1"
                + " LEFT OUTER JOIN block_content bc2 ON (bc1.document=bc2.document AND bc2.version > bc1.version)"
                + " WHERE bc2.document IS NULL AND bc1.document=?";
        String addressSql = "SELECT a.entity, a.relation, a.city, a.state, a.postalCode, a.address1, a.geoCodeStatus," +
                " a.latitude, a.longitude " +
                "               FROM address a " +
                "               WHERE a.entity=?";

        PreparedStatement storyDocStmt = null, answerSetOnlyStmt = null, tagsStmt = null,
                collectionsStmt = null, selectAddress = null, answerSetStmt = null, ownerNameStmt = null,
                documentIdStmt = null, storyTextStmt = null, documentsSelect = null;

        Connection conn = null;
        SupportDataUtils supportDataUtils = null;
        /**
         * To debug particular stories, ucomment and adjust the 'narrowing' settings. This will set the indexer at
         * the particular cycle and the boundaries will match as if you were doing a full index from the first item.
         *
         * <h2>Debug Notes</h2>
         *
         * If there are issues with particulars Stories, it can be useful to narrow the index to the particular
         * problem area rather than always re-indexing everything. To do this reliably, it's a good idea to
         * to keep the index batch  boundaries the same, espicially if there's any chance the issue is caused by a
         * boundary condition.
         *
         *  After loading the appropriate DB snapshot, run the following SQL:
         *  <pre>
         *  SELECT COUNT(*) FROM story WHERE id &lt; [crit story id];
         *  </pre>
         *  Take that number and int divide by the {@link Indexer#BATCH_SIZE} size (100 at time of writing). This gives
         *  the cycle count.
         */
        // int cycleCount = 463;
        int cycleCount = 0;

        List<SolrInputDocument> stories;
        try {
            while (true) {
                // DEBUG: to narrow the document index, you have to initial the SQL assets on the initial cycle.
                // if ((cycleCount % 10) == 0 || cycleCount == 463) {
                if ((cycleCount % 10) == 0) {
                    if (conn != null) {
                        conn.close();
                    }
                    conn = PersistenceUtil.getConnection();
                    supportDataUtils = supportDataUtilsFactory.create(conn);
                    closeIfNotNull(storyDocStmt, answerSetOnlyStmt, tagsStmt, collectionsStmt,
                            selectAddress, answerSetStmt, ownerNameStmt, documentIdStmt);
                    storyDocStmt = conn.prepareStatement(storyDocSql);
                    answerSetOnlyStmt = conn.prepareStatement(answerSetOnlySql);
                    tagsStmt = conn.prepareStatement("SELECT value FROM tag WHERE systemEntity=?");
                    collectionsStmt = conn.prepareStatement("SELECT e.id, d.title FROM systemEntity e" +
                            " JOIN collection c ON e.id=c.id" +
                            " JOIN entity n ON e.id=n.id" +
                            " JOIN document d ON d.systemEntity=c.id AND d.systemEntityRelation='BODY'" +
                            " JOIN document d2 ON d.id=d2.id" +
                            " JOIN collection_story cs ON c.id = cs.collection" +
                            " WHERE d.version>d2.version AND cs.story = ?" +
                            " UNION" +
                            " SELECT e.id, d.title FROM systemEntity e" +
                            " JOIN collection c ON e.id=c.id" +
                            " JOIN entity n ON e.id=n.id" +
                            " JOIN document d ON d.systemEntity=c.id AND d.systemEntityRelation='BODY'" +
                            " JOIN collection_story cs ON c.id = cs.collection" +
                            " WHERE cs.story = ?" +
                            " GROUP BY d.id HAVING COUNT(*) = 1");
                    selectAddress = conn.prepareStatement(addressSql);
                    answerSetStmt = conn.prepareStatement(answerSetTextSql);
                    ownerNameStmt = conn.prepareStatement(
                            "SELECT COALESCE((SELECT CONCAT_WS(' ', givenName, surname) AS name FROM profile WHERE " +
                                    "id=?), (SELECT name AS name FROM organization WHERE id=?)) AS name FROM dual");
                    documentIdStmt = conn.prepareStatement(storyDocumentSql);
                    storyTextStmt = conn.prepareStatement(storyTextSql);
                    documentsSelect = conn.prepareStatement(
                            "SELECT d.id, d.systemEntityRelation FROM document d " +
                                    "WHERE d.systemEntity=?");
                }
                PreparedStatement storiesStmt = conn.prepareStatement(storySql);
                storiesStmt.setFetchSize(BATCH_SIZE);
                storiesStmt.setInt(1, BATCH_SIZE * cycleCount);
                stories = new ArrayList<SolrInputDocument>();

                int docCount = 0;
                for (ResultSet storyResults = storiesStmt.executeQuery(); storyResults.next(); ) {
                    int storyId = storyResults.getInt(1);
                    storyDocStmt.setInt(1, storyId);
                    ResultSet storyDocResults = storyDocStmt.executeQuery();

                    IndexedStoryDocument indexedStoryDocument = null;
                    if (storyDocResults.next()) {
                        indexedStoryDocument = storyExtractor.extractData(storyDocResults);
                    } else { // probably an answer set only
                        answerSetOnlyStmt.setInt(1, storyResults.getInt(1));
                        ResultSet answerSetResults = answerSetOnlyStmt.executeQuery();
                        if (answerSetResults.next()) {
                            indexedStoryDocument = storyExtractor.extractData(answerSetResults);
                            indexedStoryDocument.setDefaultContentId(null);
                        }
                    }

                    // Get additional story / document data.
                    if (indexedStoryDocument != null) {
                        Integer answerSetId = indexedStoryDocument.getAnswerSetId();
                        if (answerSetId != null) { // Extract the questionnaire data.
                            AnswerSet answerSet = answerSetPersister.get(answerSetId, conn);
                            if (answerSet != null) {
                                indexedStoryDocument.setQuestionnaireId(answerSet.getQuestionnaire());

                                List<Document> bodies = documentPersister.retrieveDocumentsByEntityAndRelation(
                                        new EntityAndRelationParams(answerSet.getQuestionnaire(), BODY, 0));
                                // At the moment, only ever one.
                                if (bodies.size() > 0) {
                                    indexedStoryDocument.setQuestionnaireTitle(bodies.get(0).getTitle());
                                }
                            }
                        }

                        // set owner name
                        ownerNameStmt.setInt(1, indexedStoryDocument.getOwnerId());
                        ownerNameStmt.setInt(2, indexedStoryDocument.getOwnerId());
                        ResultSet ownerNameResults = ownerNameStmt.executeQuery();
                        if (ownerNameResults.next() || ownerNameResults.getString(1) == null) {
                            indexedStoryDocument.setStoryOwner(ownerNameResults.getString(1));
                        } else {
                            System.out.println(
                                    "No owner name found for owner ID: " + indexedStoryDocument.getOwnerId() + "; " +
                                            "story: " + indexedStoryDocument.getId());
                        }

                        // Load tags
                        tagsStmt.setInt(1, indexedStoryDocument.getId());
                        indexedStoryDocument.setTags(tagsExtractor.extractData(tagsStmt.executeQuery()));

                        // Load collections
                        collectionsStmt.setInt(1, indexedStoryDocument.getId());
                        collectionsStmt.setInt(2, indexedStoryDocument.getId());
                        indexedStoryDocument.setCollections(
                                collectionsExtractor.extractData(collectionsStmt.executeQuery()));
                        indexedStoryDocument.setCollectionsId(collectionsIdExtractor.extractData(collectionsStmt
                                .executeQuery()));

                        // Load Read authorization
                        indexedStoryDocument.setReadAuths(supportDataUtils.getStoryAuths(indexedStoryDocument.getId(),
                                AuthConstants.ROLE_READER));
                        indexedStoryDocument.setAdmins(supportDataUtils.getAdminNames(indexedStoryDocument.getId()));

                        // Load City and State
                        selectAddress.setInt(1, storyId);
                        Address address = addressesExtractor.extractData(selectAddress.executeQuery());
                        if (address != null) {
                            indexedStoryDocument.setAddress(address);
                        }

                        // load answer set text
                        answerSetStmt.setInt(1, indexedStoryDocument.getId());
                        ResultSet aTextRs = answerSetStmt.executeQuery();
                        String aText = "";
                        String previousLabel = null;
                        while (aTextRs.next()) {
                            String newLabel = aTextRs.getString(1);
                            if (previousLabel == null || !previousLabel.equals(newLabel)) {
                                aText += " " + newLabel + ":";
                            }
                            aText += " " + aTextRs.getString(2);
                        }
                        indexedStoryDocument.setAnswerText(aText.trim());

                        List<String> emails = supportDataUtils.getEmailsFor(indexedStoryDocument.getAuthorId());
                        if (emails.size() > 0) {
                            indexedStoryDocument.setAuthorPrimaryEmail(emails.get(0));
                        }
                        indexedStoryDocument.setAuthorEmails(emails);

                        List<String> phones = supportDataUtils.getPhonesFor(indexedStoryDocument.getAuthorId());
                        indexedStoryDocument.setAuthorPhones(phones);
                        if (phones.size() > 0) {
                            indexedStoryDocument.setAuthorPrimaryPhone(phones.get(0));
                        }

                        // Extraction of Notes attached to Story
                        documentIdStmt.setInt(1, indexedStoryDocument.getId());
                        documentIdStmt.setString(2, SystemEntityRelation.NOTE.name());
                        List<String> notes = new ArrayList<String>();

                        List<Integer> documentIds = documentId.extractData(documentIdStmt.executeQuery());
                        for (Integer id : documentIds) {
                            storyTextStmt.setInt(1, id);

                            String note = documentText.extractData(storyTextStmt.executeQuery());
                            if (!Strings.isNullOrEmpty(note)) {
                                notes.add(note);
                            }
                        }
                        indexedStoryDocument.setStoryNotes(notes);

                        //Extraction of Notes attached to Author
                        documentIdStmt.setInt(1, indexedStoryDocument.getOwnerId());
                        documentIdStmt.setString(2, SystemEntityRelation.NOTE.name());
                        notes = new ArrayList<String>();

                        documentIds = documentId.extractData(documentIdStmt.executeQuery());
                        for (Integer id : documentIds) {
                            storyTextStmt.setInt(1, id);

                            String note = documentText.extractData(storyTextStmt.executeQuery());
                            if (!Strings.isNullOrEmpty(note)) {
                                notes.add(note);
                            }
                        }
                        indexedStoryDocument.setAuthorNotes(notes);

                        documentsSelect.setInt(1, indexedStoryDocument.getId());
                        Map<String, java.util.Collection<Integer>> documents = documentsExtractor.extractData(
                                documentsSelect.executeQuery());
                        indexedStoryDocument.setDocumentIds(documents);

                        stories.add(indexedStoryDocument.toDocument());
                    } else {
                        System.out.println("No story for: " + storyResults.getInt(1));
                    }
                    docCount += 1;
                } // stmt for loop

                if (stories.size() > 0) {
                    solrStoryServer.add(stories);
                    stories = new ArrayList<SolrInputDocument>();

                    if (output != null) {
                        try {
                            output.getWriter().print(".");
                            output.flushBuffer();
                        } catch (Exception e) {
                        }
                    }
                }
                cycleCount += 1;
                if (cycleCount % 10 == 0) {
                    solrStoryServer.commit(false, false);
                    storyDocStmt.close();
                    answerSetOnlyStmt.close();
                    tagsStmt.close();
                    collectionsStmt.close();
                    selectAddress.close();
                    answerSetStmt.close();
                    ownerNameStmt.close();
                    documentIdStmt.close();
                    storyTextStmt.close();
                    documentsSelect.close();
                }

                if (docCount < BATCH_SIZE) { // then there are no more Stories to process
                    break; // exit the while loop
                }
            } // main 'while' loop
        } catch (SQLException e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        if (stories.size() > 0) {
            solrStoryServer.add(stories);
            solrStoryServer.commit();
        }
        if (output != null) {
            try {
                output.getWriter().println(" Done.");
                output.flushBuffer();
            } catch (Exception e) {
            }
        }
    }

    private void closeIfNotNull(PreparedStatement... stmts) throws SQLException {
        for (int i = 0; i < stmts.length; i += 1) {
            if (stmts[i] != null) {
                stmts[i].close();
            }
        }
    }

    public String toString() {
        return "-- Full indexation of stories";
    }

    private final ResultSetExtractor<IndexedStoryDocument> storyExtractor = new
            ResultSetExtractor<IndexedStoryDocument>() {
                @Override
                public IndexedStoryDocument extractData(ResultSet resultSet) throws SQLException {
                    IndexedStoryDocument story = new IndexedStoryDocument();
                    story.setId(resultSet.getInt(1));
                    story.setAuthorId(resultSet.getInt(2));
                    story.setPermalink(resultSet.getString(3));
                    story.setStoryBodyPrivacy(resultSet.getInt(4) == 0 ? false : true);
                    story.setTitle(resultSet.getString(5));
                    story.setPrimaryText(resultSet.getString(6));
                    story.setAuthorSurname(resultSet.getString(7));
                    story.setAuthorGivenName(resultSet.getString(8));
                    story.setAuthorFullName();
                    story.setDefaultContentId(resultSet.getInt(9));
                    story.setOwner(resultSet.getInt(11));
                    story.setCreated(resultSet.getTimestamp(12));
                    story.setLastModified(resultSet.getTimestamp(13));
                    story.setStoryVersion(resultSet.getInt(14));
                    story.setAnswerSetId(resultSet.getInt(15));
                    if (resultSet.wasNull()) {
                        story.setAnswerSetId(null);
                    }

                    return story;
                }
            };

    private final ResultSetExtractor<Map<String, java.util.Collection<Integer>>> documentsExtractor = new
            ResultSetExtractor<Map<String, java.util.Collection<Integer>>>() {
                @Override
                public Map<String, java.util.Collection<Integer>> extractData(ResultSet resultSet) throws SQLException {
                    Multimap<String, Integer> documentIds = LinkedListMultimap.create();

                    while (resultSet.next()) {
                        Integer id = resultSet.getInt(1);
                        String relation = resultSet.getString(2);

                        documentIds.put(relation, id);
                    }

                    return documentIds.asMap();
                }
            };

    private final ResultSetExtractor<Set<String>> tagsExtractor = new ResultSetExtractor<Set<String>>() {
        @Override
        public Set<String> extractData(ResultSet resultSet) throws SQLException {
            Set<String> tags = new LinkedHashSet<String>();
            while (resultSet.next()) {
                tags.add(resultSet.getString(1));
            }
            resultSet.close();

            return tags;
        }
    };

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

    private final ResultSetExtractor<Address> addressesExtractor = new ResultSetExtractor<Address>() {
        @Override
        public Address extractData(ResultSet resultSet) throws SQLException {
            Address bestAddress = null;
            int bestScore = -1;
            try {
                while (resultSet.next()) {
                    int candidateScore = 0;
                    Address candidate = new Address();
                    candidate.setGeoCodeStatus(resultSet.getString(7));
                    candidate.setCity(resultSet.getString(3));
                    candidate.setState(resultSet.getString(4));
                    candidate.setPostalCode(resultSet.getString(5));
                    candidate.setAddress1(resultSet.getString(6));

                    if (!Strings.isNullOrEmpty(candidate.getPostalCode())) {
                        candidateScore += 4;
                    } else if (!Strings.isNullOrEmpty(candidate.getState())) {
                        // That is, we have a state, but no zip
                        candidateScore += 1;
                        if (!Strings.isNullOrEmpty(candidate.getCity())) {
                            // No zip, but do have city and state (city considered worthless on it's own).
                            candidateScore += 1;
                        }
                    }

                    if (!Strings.isNullOrEmpty(candidate.getGeoCodeStatus())) {
                        Address.GeoCodeStatus geoStatus = Address.GeoCodeStatus.valueOf(candidate.getGeoCodeStatus());
                        if (geoStatus == Address.GeoCodeStatus.SUCCESS) {
                            candidate.setLatitude(resultSet.getBigDecimal(8));
                            candidate.setLongitude(resultSet.getBigDecimal(9));
                        }
                    }

                    if (candidateScore > bestScore) {
                        bestScore = candidateScore;
                        bestAddress = candidate;
                    }
                }
            } finally {
                resultSet.close();
            }

            return bestAddress;
        }
    };

    private final ResultSetExtractor<List<Integer>> documentId = new ResultSetExtractor<List<Integer>>() {
        @Override
        public List<Integer> extractData(ResultSet resultSet) throws SQLException {
            List<Integer> documentIdList = new ArrayList<Integer>();
            while (resultSet.next()) {
                documentIdList.add(resultSet.getInt(1));
            }
            resultSet.close();

            return documentIdList;
        }
    };

    private final ResultSetExtractor<String> documentText = new ResultSetExtractor<String>() {
        @Override
        public String extractData(ResultSet resultSet) throws SQLException {
            List<String> documentTexts = new ArrayList<String>();
            while (resultSet.next()) {
                documentTexts.add(resultSet.getString(1));
            }
            resultSet.close();

            if (documentTexts.isEmpty()) {
                return null;
            } else {
                return documentTexts.get(0);
            }
        }
    };
}
