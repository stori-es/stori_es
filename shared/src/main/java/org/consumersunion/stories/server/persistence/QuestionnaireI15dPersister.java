package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.AuthParam;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock.NextDocument;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.i18n.CommonI18nMessages;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.persistence.OrganizationPersister.RetrieveOrganizationFunc;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import net.lightoze.gwt.i18n.server.LocaleFactory;
import net.lightoze.gwt.i18n.server.LocaleProxy;

import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ATTACHMENT;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.BODY;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.CONTENT;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.SURVEY;

@Component
public class QuestionnaireI15dPersister implements Persister<QuestionnaireI15d> {
    static {
        if ("true".equals(System.getProperty("org.consumersunion.testMode"))) {
            LocaleProxy.initialize();
        }
    }

    private static final String selectClause = "SELECT e.id, e.version, e.created, e.lastModified, "
            + "(SELECT COUNT(*) FROM answerSet a WHERE a.questionnaire = q.id) as responseCount, "
            + "c.theme, e.public, entity.permalink, c.published, c.publishedDate, c.previewKey ";

    private static final String fromClause = " FROM questionnaire q " +
            "JOIN systemEntity e ON e.id=q.id JOIN entity ON entity.id=q.id JOIN collection c ON c.id=q.id";

    private final PersistenceService persistenceService;
    private final DocumentPersister documentPersister;
    private final CollectionPersister collectionPersister;
    private final AvailablePermalinkExtractor availablePermalinkExtractor;

    @Inject
    QuestionnaireI15dPersister(
            PersistenceService persistenceService,
            DocumentPersister documentPersister,
            CollectionPersister collectionPersister,
            AvailablePermalinkExtractor availablePermalinkExtractor) {
        this.persistenceService = persistenceService;
        this.documentPersister = documentPersister;
        this.collectionPersister = collectionPersister;
        this.availablePermalinkExtractor = availablePermalinkExtractor;
    }

    @Override
    public Class<QuestionnaireI15d> getHandles() {
        return QuestionnaireI15d.class;
    }

    @Override
    public QuestionnaireI15d get(int id) throws NotFoundException {
        return persistenceService.process(new RetrieveQuestionnaireFunc(id, documentPersister));
    }

    @Override
    public QuestionnaireI15d get(int id, Connection connection) {
        return persistenceService.process(connection, new RetrieveQuestionnaireFunc(id, documentPersister));
    }

    public QuestionnaireI15d getIncludeDeleted(int id) throws NotFoundException {
        return persistenceService.process(new RetrieveQuestionnaireFunc(id, documentPersister, true));
    }

    public QuestionnaireI15d getIncludeDeleted(int id, Connection connection) {
        return persistenceService.process(connection, new RetrieveQuestionnaireFunc(id, documentPersister, true));
    }

    public QuestionnaireI15d getByPermalink(final String permalink) throws NotFoundException {
        return persistenceService.process(new ProcessFunc<String, QuestionnaireI15d>(permalink) {
            @Override
            public QuestionnaireI15d process() {
                try {
                    PreparedStatement select = conn.prepareStatement(selectClause + fromClause +
                            " WHERE entity.permalink=?");

                    select.setString(1, permalink);

                    ResultSet results = select.executeQuery();
                    if (results.next()) {
                        return instantiateBasicQuestionnaire(documentPersister, results, conn, true);
                    } else {
                        throw new NotFoundException();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new GeneralException(e);
                }
            }
        });
    }

    public QuestionnaireI15d createQuestionnaire(QuestionnaireI15d questionnaire) {
        return persistenceService.process(
                new CreateQuestionnaireFunc(questionnaire, collectionPersister, documentPersister));
    }

    public QuestionnaireI15d createQuestionnaire(QuestionnaireI15d questionnaire, Connection conn) {
        return persistenceService.process(conn,
                new CreateQuestionnaireFunc(questionnaire, collectionPersister, documentPersister));
    }

    public UpdateQuestionnaireFunc updateQuestionnaireFunc(QuestionnaireI15d questionnaire) {
        return new UpdateQuestionnaireFunc(questionnaire, collectionPersister);
    }

    public List<QuestionnaireI15d> searchAndExcludeLinkedToCollectionFunc(SearchByCollectionPagedParams params) {
        return persistenceService.process(new SearchAndExcludeLinkedToCollectionFunc(params, documentPersister));
    }

    public List<QuestionnaireI15d> searchByCollectionsPaged(SearchByCollectionPagedParams params) {
        return persistenceService.process(new SearchByCollectionPagedFunc(params, documentPersister));
    }

    public List<QuestionnaireI15d> searchByCollectionsPaged(SearchByCollectionPagedParams params, Connection conn) {
        return persistenceService.process(conn, new SearchByCollectionPagedFunc(params, documentPersister));
    }

    public QuestionnaireI15d updateQuestionnaire(QuestionnaireI15d questionnaire) {
        return persistenceService.process(updateQuestionnaireFunc(questionnaire));
    }

    public String getAvailablePermalink(String slug) {
        return persistenceService.process(new ProcessFunc<String, String>("/questionnaires/" + slug) {
            @Override
            public String process() {
                try {
                    PreparedStatement select = conn.prepareStatement(
                            "SELECT e.permalink FROM questionnaire q " +
                                    "JOIN entity e ON q.id = e.id " +
                                    "WHERE e.permalink LIKE ?");

                    select.setString(1, input + "%");

                    ResultSet rs = select.executeQuery();

                    return availablePermalinkExtractor.extractPermalink(rs, input);
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    static class CreateQuestionnaireFunc extends CollectionPersister.CreateCollectionFunc<QuestionnaireI15d> {
        private final DocumentPersister documentPersister;

        CreateQuestionnaireFunc(
                QuestionnaireI15d input,
                CollectionPersister collectionPersister,
                DocumentPersister documentPersister) {
            super(input, collectionPersister);

            this.documentPersister = documentPersister;
        }

        @Override
        protected QuestionnaireI15d createConcrete() throws SQLException {
            Document confirmation = createConfirmation(documentPersister);
            Document redirect = createRedirect(documentPersister);

            SubmitBlock submitBlock = input.getSubmitBlock();
            NextDocument currentNextDocument = submitBlock.getNextDocument();

            List<NextDocument> nextDocuments = NextDocument.fromDocuments(confirmation, redirect);
            if (currentNextDocument != null) {
                for (NextDocument nextDocument : nextDocuments) {
                    if (currentNextDocument.matches(nextDocument)) {
                        currentNextDocument.setDocumentId(nextDocument.getDocumentId());
                        break;
                    }
                }
            }

            submitBlock.setNextDocuments(nextDocuments);

            super.createConcrete();

            PreparedStatement insertQuestionnaire = conn
                    .prepareStatement("INSERT INTO questionnaire (id) VALUES (?)");
            insertQuestionnaire.setInt(1, input.getId());

            int insertCount = insertQuestionnaire.executeUpdate();
            if (insertCount != 1) {
                throw new GeneralException("Unexpected insert count: " + insertCount);
            }

            addCollectionQuestionnaireLinks();

            Document surveyDoc = input.getSurvey();
            surveyDoc.setSystemEntityRelation(SURVEY);
            surveyDoc.setEntity(input.getId());
            surveyDoc.setVersion(input.getVersion());
            surveyDoc.setPrimaryAuthor(input.getBodyDocument().getPrimaryAuthor());
            surveyDoc = documentPersister.create(surveyDoc, conn);
            input.setSurvey(surveyDoc);

            return input;
        }

        private Document createRedirect(DocumentPersister documentPersister) {
            Organization organization = PersistenceUtil.process(conn, new RetrieveOrganizationFunc(input.getOwner()));

            Document redirect = new Document();

            CommonI18nMessages messages = LocaleFactory.get(CommonI18nMessages.class);
            redirect.setTitle(messages.redirect());
            redirect.setPermalink(organization.getPermalink());
            redirect.setEntity(input.getId());
            redirect.setSystemEntityRelation(ATTACHMENT);
            redirect.setPrimaryAuthor(input.getPrimaryAuthor());
            redirect.addBlock(new TextImageBlock(messages.redirect()));

            return documentPersister.create(redirect, conn);
        }

        private Document createConfirmation(DocumentPersister documentPersister) {
            Document confirmation = input.getConfirmationDocument();

            String title = confirmation.getTitle();
            if (Strings.isNullOrEmpty(title) && confirmation.getFirstContent() == null) {
                CommonI18nMessages messages = LocaleFactory.get(CommonI18nMessages.class);
                title = messages.confirmationTitle();
                confirmation.addBlock(new TextImageBlock(messages.confirmationText()));
            }

            confirmation.setTitle(title);
            confirmation.setEntity(input.getId());
            confirmation.setSystemEntityRelation(CONTENT);
            confirmation.setPermalink("");

            return documentPersister.create(confirmation, conn);
        }

        private void addCollectionQuestionnaireLinks() throws SQLException {
            String sql =
                    "INSERT INTO collection_sources " +
                            "(sourceQuestionnaire, targetCollection) " +
                            "VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (Integer collectionId : input.getTargetCollections()) {
                stmt.setInt(1, input.getId());
                stmt.setInt(2, collectionId);
                stmt.addBatch();
            }

            int[] insertCounts = stmt.executeBatch();

            for (int insertCount : insertCounts) {
                if (insertCount != 1) {
                    throw new GeneralException("Unexpected insert count: " + insertCount);
                }
            }
        }
    }

    public static class RetrieveQuestionnaireFunc extends RetrieveFunc<QuestionnaireI15d> {
        private final DocumentPersister documentPersister;
        private final boolean includeDeleted;

        public RetrieveQuestionnaireFunc(Integer id, DocumentPersister documentPersister) {
            this(id, documentPersister, false);
        }

        public RetrieveQuestionnaireFunc(int id, DocumentPersister documentPersister, boolean includeDeleted) {
            super(id);

            this.documentPersister = documentPersister;
            this.includeDeleted = includeDeleted;
        }

        @Override
        protected QuestionnaireI15d retrieveConcrete() throws SQLException {
            return getBasicQuestionnaireFromId(documentPersister, input, conn, includeDeleted);
        }
    }

    /**
     * Retrieves all Questionnaires, non-linked to {Collection Collection} by the user.
     */
    public static class SearchAndExcludeLinkedToCollectionFunc extends
            ProcessFunc<SearchByCollectionPagedParams, List<QuestionnaireI15d>> {
        private final DocumentPersister documentPersister;

        public SearchAndExcludeLinkedToCollectionFunc(
                SearchByCollectionPagedParams params,
                DocumentPersister documentPersister) {
            super(params);

            this.documentPersister = documentPersister;
        }

        @Override
        public List<QuestionnaireI15d> process() {
            try {
                List<QuestionnaireI15d> result = new ArrayList<QuestionnaireI15d>();

                PreparedStatement qps = conn.prepareStatement(selectClause + fromClause
                        + " WHERE q.id NOT IN (select cs.sourceQuestionnaire from collection_sources cs"
                        + " where cs.targetCollection=?) AND e.owner=?"
                        + " AND c.deleted=0 "
                        + " ORDER BY e.id DESC" + " LIMIT ?, ?");
                qps.setInt(1, input.getCollectionId());
                qps.setInt(2, input.getEffectiveId());
                qps.setInt(3, input.getStart());
                qps.setInt(4, input.getLength());

                final ResultSet qrs = qps.executeQuery();

                for (; qrs.next(); ) {
                    QuestionnaireI15d q =
                            instantiateBasicQuestionnaire(documentPersister, qrs, conn, input.isRetrieveComplete());
                    result.add(q);
                }

                return result;
            } catch (final SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class SearchByCollectionPagedFunc
            extends ProcessFunc<SearchByCollectionPagedParams, List<QuestionnaireI15d>> {
        private final DocumentPersister documentPersister;

        SearchByCollectionPagedFunc(
                SearchByCollectionPagedParams params,
                DocumentPersister documentPersister) {
            super(params);

            this.documentPersister = documentPersister;
        }

        @Override
        public List<QuestionnaireI15d> process() {
            try {
                List<QuestionnaireI15d> result = new ArrayList<QuestionnaireI15d>();

                boolean hasLimit = input.getStart() != 0 && input.getLength() != 0;
                PreparedStatement qps = conn.prepareStatement(selectClause + fromClause +
                        " JOIN collection_sources cSource ON cSource.sourceQuestionnaire=q.id "
                        + "WHERE cSource.targetCollection=? "
                        + "AND c.deleted=0 "
                        + "ORDER BY e.id DESC" + (hasLimit ? " LIMIT ?, ?" : ""));
                qps.setInt(1, input.getCollectionId());
                if (hasLimit) {
                    qps.setInt(2, input.getStart());
                    qps.setInt(3, input.getLength());
                }

                ResultSet qrs = qps.executeQuery();

                for (; qrs.next(); ) {
                    QuestionnaireI15d q =
                            instantiateBasicQuestionnaire(documentPersister, qrs, conn, input.isRetrieveComplete());
                    result.add(q);
                }

                return result;
            } catch (final SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class SearchByCollectionPagedParams extends AuthParam<SearchByCollectionPagedParams> {
        final int start;
        final int length;
        final int collectionId;
        private boolean retrieveComplete = true;

        public SearchByCollectionPagedParams(
                int collectionId,
                int start,
                int length,
                int relation,
                Integer effectiveId) {
            super(start, length, null, true, relation, effectiveId);

            this.collectionId = collectionId;
            this.start = start;
            this.length = length;
        }

        public void setRetrievePartials() {
            retrieveComplete = false;
        }

        public void setRetrieveComplete() {
            retrieveComplete = true;
        }

        public boolean isRetrieveComplete() {
            return retrieveComplete;
        }

        public int getStart() {
            return start;
        }

        public int getLength() {
            return length;
        }

        @Override
        public SearchByCollectionPagedParams noLimit() {
            return new SearchByCollectionPagedParams(getCollectionId(), 0, 0, getAuthRelation(), getEffectiveId());
        }

        public int getCollectionId() {
            return collectionId;
        }
    }

    public static class CountQuestionnairesFunc extends ProcessFunc<Integer, Integer> {
        public CountQuestionnairesFunc(Integer collectionId) {
            super(collectionId);
        }

        @Override
        public Integer process() {
            try {
                PreparedStatement cps = conn
                        .prepareStatement("SELECT COUNT(*) FROM collection_sources WHERE targetCollection=?");
                cps.setInt(1, input);

                ResultSet crs = cps.executeQuery();
                crs.next();

                return crs.getInt(1);
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    static class UpdateQuestionnaireFunc extends CollectionPersister.UpdateCollectionFunc<QuestionnaireI15d> {
        public UpdateQuestionnaireFunc(QuestionnaireI15d input, CollectionPersister collectionPersister) {
            super(input, collectionPersister);
        }

        @Override
        protected QuestionnaireI15d updateConcrete() throws SQLException {
            input.setSurvey(updateDocument(input.getSurvey()));

            return super.updateConcrete();
        }
    }

    private static QuestionnaireI15d getBasicQuestionnaireFromId(
            DocumentPersister documentPersister,
            int id,
            Connection conn,
            boolean includeDeleted) throws SQLException {
        PreparedStatement retrieveQ = conn.prepareStatement(
                selectClause + fromClause + " WHERE e.id=? " + (includeDeleted ? "" : "AND c.deleted=0"));
        retrieveQ.setInt(1, id);

        ResultSet resultsQ = retrieveQ.executeQuery();
        if (!resultsQ.next()) {
            throw new NotFoundException("No Questionnaire with ID " + id + " found.");
        }

        return instantiateBasicQuestionnaire(documentPersister, resultsQ, conn, true);
    }

    private static QuestionnaireI15d instantiateBasicQuestionnaire(
            DocumentPersister documentPersister,
            ResultSet questionnaireRS,
            Connection conn,
            boolean fullRetrieve) throws SQLException {
        int id = questionnaireRS.getInt(1);
        int version = questionnaireRS.getInt(2);
        Date created = questionnaireRS.getTimestamp(3) == null ? null :
                new Date(questionnaireRS.getTimestamp(3).getTime());
        int responseCount = questionnaireRS.getInt(5);

        Integer theme = questionnaireRS.getInt(6);
        if (questionnaireRS.wasNull()) {
            theme = null;
        }

        String permalink = questionnaireRS.getString(8);
        boolean published = questionnaireRS.getBoolean(9);
        Timestamp publishedDateTimestamp = questionnaireRS.getTimestamp(10);
        Date publishedDate = publishedDateTimestamp == null ? null : new Date(publishedDateTimestamp.getTime());
        String previewKey = questionnaireRS.getString(11);

        QuestionnaireI15d questionnaire = new QuestionnaireI15d(id, version);
        questionnaire.setPublic(questionnaireRS.getBoolean(7));
        questionnaire.setPublished(published);
        questionnaire.setPublishedDate(publishedDate);
        questionnaire.setPreviewKey(previewKey);
        questionnaire.setCreated(created);
        questionnaire.setNumberOfReponses(responseCount);
        questionnaire.setTheme(theme);
        questionnaire.setPermalink(permalink);

        // TODO : Support i18n of body document
        DocumentPersister.EntityAndRelationParams bodyParams =
                new DocumentPersister.EntityAndRelationParams(questionnaire.getId(), BODY, 0);
        Document bodyDocument = documentPersister.retrieveLatestDocumentByRelation(bodyParams, Document.class, conn);
        questionnaire.setBodyDocument(bodyDocument);

        // TODO : Support i18n of survey document
        DocumentPersister.EntityAndRelationParams surveyParams =
                new DocumentPersister.EntityAndRelationParams(questionnaire.getId(), SURVEY, 0);
        Document surveyDocument = documentPersister.retrieveLatestDocumentByRelation(surveyParams, Document.class,
                conn);
        questionnaire.setSurvey(surveyDocument);

        if (fullRetrieve) {
            CollectionStoryLinkPersistenceHelper.loadAllStoryLinks(questionnaire, conn);

            NextDocument nextDocument = questionnaire.getSubmitBlock().getNextDocument();
            if (nextDocument != null) {
                Document confirmation = documentPersister.get(nextDocument.getDocumentId(), conn);
                questionnaire.setNextDocument(confirmation);
            }
        }

        CollectionPersister.retrieveTargetCollections(questionnaire, conn);
        CollectionPersister.retrieveCollectionSources(questionnaire, conn);

        return questionnaire;
    }
}
