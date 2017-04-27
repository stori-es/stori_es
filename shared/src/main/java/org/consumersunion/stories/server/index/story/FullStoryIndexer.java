package org.consumersunion.stories.server.index.story;

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

import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.persistence.AnswerSetPersister;
import org.consumersunion.stories.server.persistence.DocumentPersister;
import org.consumersunion.stories.server.persistence.DocumentPersister.EntityAndRelationParams;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.ResultSetExtractor;
import org.consumersunion.stories.server.persistence.SupportDataUtils;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.BODY;

@Component
public class FullStoryIndexer {
    private static final int BATCH_SIZE = 250;

    private final HttpServletResponse output;
    private final DocumentPersister documentPersister;
    private final Indexer<StoryDocument> storyIndexer;
    private final AnswerSetPersister answerSetPersister;
    private final SupportDataUtilsFactory supportDataUtilsFactory;

    @Inject
    FullStoryIndexer(
            Indexer<StoryDocument> storyIndexer,
            DocumentPersister documentPersister,
            AnswerSetPersister answerSetPersister,
            SupportDataUtilsFactory supportDataUtilsFactory) {
        this.storyIndexer = storyIndexer;
        this.answerSetPersister = answerSetPersister;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
        this.output = null;
        this.documentPersister = documentPersister;
    }

    public FullStoryIndexer(
            HttpServletResponse output,
            Indexer<StoryDocument> storyIndexer,
            DocumentPersister documentPersister,
            AnswerSetPersister answerSetPersister,
            SupportDataUtilsFactory supportDataUtilsFactory) {
        this.storyIndexer = storyIndexer;
        this.documentPersister = documentPersister;
        this.output = output;
        this.answerSetPersister = answerSetPersister;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
    }

    public void index() throws Exception {
        print("Collection indexing");

        String storiesSql = "SELECT s.id AS storyId," // 1
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
                + "LEFT JOIN story s ON s.id = d.systemEntity "
                + "LEFT JOIN systemEntity e ON s.id = e.id AND d.version = e.version "
                + "JOIN systemEntity de ON d.id=de.id "
                + "JOIN profile p ON p.id=d.primaryAuthor "
                + "JOIN block_content bc ON d.id=bc.document AND d.version=bc.version "
                + "LEFT OUTER JOIN block_content bc2 ON (bc.document=bc2.document AND bc2.version > bc.version) "
                + "WHERE d.systemEntityRelation='BODY' AND bc2.document IS NULL "
                + "ORDER BY s.id ASC LIMIT " + BATCH_SIZE + " OFFSET ?";
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
                + "FROM document d "
                + "JOIN story s ON d.systemEntity = s.id "
                + "JOIN profile p ON p.id=d.primaryAuthor "
                + "JOIN systemEntity e ON s.id=e.id "
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

        PreparedStatement storiesStmt = null, answerSetOnlyStmt = null, tagsStmt = null,
                collectionsStmt = null, selectAddress = null, answerSetStmt = null, ownerNameStmt = null,
                documentIdStmt = null, storyTextStmt = null, documentsSelect = null;

        Connection conn = null;
        SupportDataUtils supportDataUtils = null;

        int cycleCount = 0;
        List<StoryDocument> stories;
        try {
            while (true) {
                if ((cycleCount % 10) == 0) {
                    if (conn != null) {
                        conn.close();
                    }
                    conn = PersistenceUtil.getConnection();
                    supportDataUtils = supportDataUtilsFactory.create(conn);
                    closeIfNotNull(storiesStmt, answerSetOnlyStmt, tagsStmt, collectionsStmt,
                            selectAddress, answerSetStmt, ownerNameStmt, documentIdStmt);
                    storiesStmt = conn.prepareStatement(storiesSql, TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY);
                    answerSetOnlyStmt = conn.prepareStatement(answerSetOnlySql);
                    tagsStmt = conn.prepareStatement("SELECT value FROM tag WHERE systemEntity=?");
                    collectionsStmt = conn.prepareStatement("SELECT e.id, d.title FROM systemEntity e" +
                            " JOIN collection c ON e.id=c.id" +
                            " JOIN entity n ON e.id=n.id" +
                            " JOIN document d ON d.systemEntity=c.id AND d.systemEntityRelation='BODY'" +
                            "    AND d.version = (SELECT MAX(d2.version) FROM document d2 WHERE d2.id = d.id)" +
                            " JOIN collection_story cs ON c.id = cs.collection" +
                            " WHERE cs.story = ?" +
                            " GROUP BY d.id");
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
                storiesStmt.setInt(1, BATCH_SIZE * cycleCount);

                stories = new ArrayList<StoryDocument>();

                ResultSet storyResults = storiesStmt.executeQuery();
                if (storyResults.next()) {
                    storyResults.beforeFirst();
                } else {
                    break;
                }
                while (storyResults.next()) {
                    int storyId = storyResults.getInt(1);

                    StoryDocument storyDocument =
                            extractStoryDocument(answerSetOnlyStmt, storyId, storyResults);

                    // Get additional story / document data.
                    if (storyDocument != null) {
                        Integer answerSetId = storyDocument.getAnswerSetId();
                        if (answerSetId != null) { // Extract the questionnaire data.
                            AnswerSet answerSet = answerSetPersister.get(answerSetId, conn);
                            if (answerSet != null) {
                                storyDocument.setQuestionnaireId(answerSet.getQuestionnaire());

                                List<Document> bodies = documentPersister.retrieveDocumentsByEntityAndRelation(
                                        new EntityAndRelationParams(answerSet.getQuestionnaire(), BODY, 0));
                                // At the moment, only ever one.
                                if (!bodies.isEmpty()) {
                                    storyDocument.setQuestionnaireTitle(bodies.get(0).getTitle());
                                }
                            }
                        }

                        // set owner name
                        ownerNameStmt.setInt(1, storyDocument.getOwnerId());
                        ownerNameStmt.setInt(2, storyDocument.getOwnerId());
                        ResultSet ownerNameResults = ownerNameStmt.executeQuery();
                        if (ownerNameResults.next() || ownerNameResults.getString(1) == null) {
                            storyDocument.setStoryOwner(ownerNameResults.getString(1));
                            ownerNameResults.close();
                        } else {
                            System.out.println(
                                    "No owner name found for owner ID: " + storyDocument.getOwnerId() + "; " +
                                            "story: " + storyDocument.getId());
                        }

                        // Load tags
                        tagsStmt.setInt(1, storyDocument.getId());
                        storyDocument.setTags(tagsExtractor.extractData(tagsStmt.executeQuery()));

                        // Load collections
                        collectionsStmt.setInt(1, storyDocument.getId());
                        storyDocument.setCollections(
                                collectionsExtractor.extractData(collectionsStmt.executeQuery()));
                        storyDocument.setCollectionsId(collectionsIdExtractor.extractData(collectionsStmt
                                .executeQuery()));

                        // Load Read authorization
                        storyDocument.setReadAuths(supportDataUtils.getStoryAuths(storyDocument.getId(),
                                AuthConstants.ROLE_READER));
                        storyDocument.setAdmins(supportDataUtils.getAdminNames(storyDocument.getId()));

                        // Load City and State
                        selectAddress.setInt(1, storyId);
                        Address address = addressesExtractor.extractData(selectAddress.executeQuery());
                        storyDocument.setAddress(address);

                        // load answer set text
                        String answerText = getAnswerText(answerSetStmt, storyDocument);
                        storyDocument.setAnswerText(answerText.trim());

                        List<String> emails = supportDataUtils.getEmailsFor(storyDocument.getAuthorId());
                        storyDocument.setAuthorPrimaryEmail(Iterables.getFirst(emails, null));
                        storyDocument.setAuthorEmails(emails);

                        List<String> phones = supportDataUtils.getPhonesFor(storyDocument.getAuthorId());
                        storyDocument.setAuthorPhones(phones);
                        storyDocument.setAuthorPrimaryPhone(Iterables.getFirst(phones, null));

                        // Extraction of Notes attached to Story
                        documentIdStmt.setInt(1, storyDocument.getId());
                        documentIdStmt.setString(2, SystemEntityRelation.NOTE.name());

                        List<String> storyNotes = documentsToNotes(documentIdStmt, storyTextStmt);
                        storyDocument.setStoryNotes(storyNotes);

                        //Extraction of Notes attached to Author
                        documentIdStmt.setInt(1, storyDocument.getOwnerId());
                        documentIdStmt.setString(2, SystemEntityRelation.NOTE.name());

                        List<String> authorNotes = documentsToNotes(documentIdStmt, storyTextStmt);
                        storyDocument.setAuthorNotes(authorNotes);

                        documentsSelect.setInt(1, storyDocument.getId());
                        Map<String, java.util.Collection<Integer>> documents =
                                documentsExtractor.extractData(documentsSelect.executeQuery());
                        storyDocument.setDocumentIds(documents);

                        stories.add(storyDocument);
                    } else {
                        System.out.println("No story for: " + storyResults.getInt(1));
                    }
                } // while loop

                System.err.println(BATCH_SIZE * (cycleCount + 1));
                storyResults.close();
                if (stories.size() > 0) {
                    storyIndexer.indexAsync(stories);
                    print(".");
                }
                cycleCount += 1;
            } // main 'while' loop
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        if (stories.size() > 0) {
            storyIndexer.index(stories);
        }
        print("Done.");
    }

    private StoryDocument extractStoryDocument(
            PreparedStatement answerSetOnlyStmt,
            int storyId,
            ResultSet storyResults) throws SQLException {
        StoryDocument storyDocument = null;
        storyResults.getString(5);
        if (!storyResults.wasNull()) {
            storyDocument = storyExtractor.extractData(storyResults);
        } else { // probably an answer set only
            answerSetOnlyStmt.setInt(1, storyId);
            ResultSet answerSetResults = answerSetOnlyStmt.executeQuery();
            if (answerSetResults.next()) {
                storyDocument = storyExtractor.extractData(answerSetResults);
                storyDocument.setDefaultContentId(null);
            }
            answerSetResults.close();
        }
        return storyDocument;
    }

    private String getAnswerText(PreparedStatement answerSetStmt, StoryDocument storyDocument)
            throws SQLException {
        answerSetStmt.setInt(1, storyDocument.getId());
        ResultSet aTextRs = answerSetStmt.executeQuery();
        StringBuilder aText = new StringBuilder();
        while (aTextRs.next()) {
            aText.append(" ").append(aTextRs.getString(1)).append(":")
                    .append(" ").append(aTextRs.getString(2));
        }
        aTextRs.close();
        return aText.toString();
    }

    private List<String> documentsToNotes(PreparedStatement documentIdStmt, final PreparedStatement storyTextStmt)
            throws SQLException {
        return FluentIterable.from(documentId.extractData(documentIdStmt.executeQuery()))
                .transform(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer id) {
                        try {
                            storyTextStmt.setInt(1, id);

                            return documentText.extractData(storyTextStmt.executeQuery());
                        } catch (SQLException e) {
                            throw new GeneralException(e);
                        }
                    }
                })
                .filter(Predicates.<String>notNull())
                .toList();
    }

    private void print(String s) {
        if (output != null) {
            try {
                output.getWriter().print(s);
                output.flushBuffer();
            } catch (Exception ignored) {
            }
        }
    }

    private void closeIfNotNull(PreparedStatement... statements) throws SQLException {
        for (PreparedStatement statement : statements) {
            if (statement != null) {
                statement.close();
            }
        }
    }

    private final ResultSetExtractor<StoryDocument> storyExtractor = new
            ResultSetExtractor<StoryDocument>() {
                @Override
                public StoryDocument extractData(ResultSet resultSet) throws SQLException {
                    StoryDocument story = new StoryDocument();
                    story.setId(resultSet.getInt(1));
                    story.setAuthorId(resultSet.getInt(2));
                    story.setPermalink(resultSet.getString(3));
                    story.setStoryBodyPrivacy(resultSet.getInt(4) != 0);
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

                    resultSet.close();

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
