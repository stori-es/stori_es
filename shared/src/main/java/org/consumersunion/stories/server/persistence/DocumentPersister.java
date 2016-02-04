package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.common.shared.dto.AuthParam;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.Document.DocumentContributorRole;
import org.consumersunion.stories.common.shared.model.document.DocumentContributor;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.common.shared.service.datatransferobject.AnswerSetSummary;
import org.consumersunion.stories.common.shared.util.GUID;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.persistence.AnswerSetPersister.RetrieveAnswerSetFunc;
import org.consumersunion.stories.server.persistence.funcs.CreateFunc;
import org.consumersunion.stories.server.persistence.funcs.DeleteFunc;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.consumersunion.stories.server.persistence.funcs.UpdateFunc;
import org.consumersunion.stories.server.solr.SupportDataUtils;
import org.consumersunion.stories.server.solr.SupportDataUtilsFactory;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.model.Locale.ENGLISH;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ANSWER_SET;

/**
 * Class for {@link Document} CRUD operations.
 */
@Component
public class DocumentPersister implements Persister<Document> {
    public static final int INITIAL_VERSION = 1;

    private static final String BASE_DOCUMENT_SELECT = "SELECT se.id, d.version, d.systemEntity, " +
            "d.systemEntityRelation, d.primaryAuthor, d.permalink, se.public, se.created, se.lastModified, " +
            "p.givenName, p.surname, d.locale, d.title, d.summary " +
            "FROM document d " +
            "JOIN systemEntity se ON d.id=se.id AND d.version=se.version " +
            "LEFT JOIN profile p on d.primaryAuthor=p.id ";

    // The systemEntity version tracks the latest version of the document.
    @VisibleForTesting
    static final String DOCUMENT_SELECT = BASE_DOCUMENT_SELECT + "WHERE d.systemEntity=? AND systemEntityRelation=?";

    private static final String DOCUMENT_SELECT_BY_ID = BASE_DOCUMENT_SELECT + "WHERE se.id=?";
    private static final String DOCUMENT_SELECT_IN =
            BASE_DOCUMENT_SELECT + "WHERE d.systemEntity=? AND systemEntityRelation IN ";
    private static final String DOCUMENT_NOT_ANSWER_SET =
            BASE_DOCUMENT_SELECT + "WHERE d.systemEntity=? AND systemEntityRelation <> 'ANSWER_SET'";

    @VisibleForTesting
    static final String CONTRIBUTOR_SELECT = "SELECT user, role FROM documentContributor WHERE document=?";

    private final Provider<QuestionnaireI15dPersister> questionnaireI15dPersisterProvider;
    private final Provider<AnswerSetPersister> answerSetPersisterProvider;
    private final BlockPersistenceHelper blockPersistenceHelper;
    private final PersistenceService persistenceService;
    private final SupportDataUtilsFactory supportDataUtilsFactory;

    @Inject
    DocumentPersister(
            Provider<QuestionnaireI15dPersister> questionnaireI15dPersisterProvider,
            Provider<AnswerSetPersister> answerSetPersisterProvider,
            BlockPersistenceHelper blockPersistenceHelper,
            PersistenceService persistenceService,
            SupportDataUtilsFactory supportDataUtilsFactory) {
        this.questionnaireI15dPersisterProvider = questionnaireI15dPersisterProvider;
        this.answerSetPersisterProvider = answerSetPersisterProvider;
        this.blockPersistenceHelper = blockPersistenceHelper;
        this.persistenceService = persistenceService;
        this.supportDataUtilsFactory = supportDataUtilsFactory;
    }

    public Class<Document> getHandles() {
        return Document.class;
    }

    public Document create(Document document) {
        return persistenceService.process(createDocumentFunc(document));
    }

    public Document create(Document document, Connection conn) {
        return persistenceService.process(conn, createDocumentFunc(document));
    }

    public Document updateDocument(Document document) {
        return persistenceService.process(new UpdateDocumentFunc(document, this));
    }

    public Document updateDocument(Document document, Connection conn) {
        return persistenceService.process(conn, new UpdateDocumentFunc(document, this));
    }

    public Document retrieveOriginalByEntityAndRelation(EntityAndRelationParams params) {
        return persistenceService.process(new RetrieveOriginalByEntityAndRelationFunc(params, this));
    }

    public List<Document> retrieveDocumentsByEntityAndRelation(EntityAndRelationParams params) {
        return persistenceService.process(retrieveDocumentsByEntityAndRelationFunc(params));
    }

    public List<Document> retrieveDocumentsByEntityAndRelation(EntityAndRelationParams params, Connection conn) {
        return persistenceService.process(conn, retrieveDocumentsByEntityAndRelationFunc(params));
    }

    public List<Document> retrieveDocumentsByEntity(int id) {
        return persistenceService.process(new RetrieveDocumentsByEntity(id, this));
    }

    public List<Document> retrieveDocumentsByEntity(int id, Connection conn) {
        return persistenceService.process(conn, new RetrieveDocumentsByEntity(id, this));
    }

    public List<Document> retrieveStoryDocuments(Story story) {
        return persistenceService.process(new ProcessFunc<Story, List<Document>>(story) {
            @Override
            public List<Document> process() {
                return retrieveStoryDocuments(input, conn);
            }
        });
    }

    public List<Document> retrieveStoryDocuments(Story story, Connection conn) {
        return persistenceService.process(conn, new ProcessFunc<Story, List<Document>>(story) {
            @Override
            public List<Document> process() {
                List<Document> documents = new ArrayList<Document>();

                DocumentPersister documentPersister = DocumentPersister.this;
                List<Document> basicDocuments = documentPersister.retrieveDocumentsByEntity(input.getId(), conn);
                documents.addAll(basicDocuments);

                Integer answerSetId = input.getOnlyDocument(ANSWER_SET);
                if (answerSetId != null) {
                    AnswerSetPersister answerSetPersister = answerSetPersisterProvider.get();
                    AnswerSet answerSet = answerSetPersister.get(answerSetId, conn);
                    if (answerSet != null) {
                        QuestionnaireI15dPersister questionnaireI15dPersister =
                                questionnaireI15dPersisterProvider.get();

                        QuestionnaireI15d questionnaire =
                                questionnaireI15dPersister.getIncludeDeleted(answerSet.getQuestionnaire(), conn);

                        AnswerSetSummary answerSetSummary = new AnswerSetSummary(answerSet, questionnaire.getTitle());
                        documents.add(answerSetSummary);
                    }
                }

                return documents;
            }
        });
    }

    public void deleteDocument(Document document) {
        persistenceService.process(new DeleteDocumentFunc(document, this));
    }

    public List<Document> retrieveDocumentsByEntityAndRelations(
            int systemEntity,
            final List<SystemEntityRelation> systemEntityRelations,
            Connection conn) {
        return persistenceService.process(conn, new ProcessFunc<Integer, List<Document>>(systemEntity) {
            @Override
            public List<Document> process() {
                List<String> queryStrings = FluentIterable.from(systemEntityRelations)
                        .transform(new Function<SystemEntityRelation, String>() {
                            @Override
                            public String apply(SystemEntityRelation input) {
                                return "?";
                            }
                        }).toList();
                String queryString = Joiner.on(", ").join(queryStrings);
                try {
                    List<Document> result = new ArrayList<Document>();
                    PreparedStatement select = conn.prepareStatement(DOCUMENT_SELECT_IN + " (" + queryString + ")");
                    select.setInt(1, input);

                    for (int i = 0; i < systemEntityRelations.size(); i++) {
                        select.setString(i + 2, systemEntityRelations.get(i).name());
                    }

                    ResultSet rs = select.executeQuery();

                    SupportDataUtils dataUtils = supportDataUtilsFactory.create(conn);
                    while (rs.next()) {
                        DocumentPersister.this.loadDocument(conn, result, rs, dataUtils, new Document());
                    }

                    return result;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public List<Document> retrieveAllByEntityAndRelation(
            EntityAndRelationParams params,
            Connection connection) throws SQLException {
        return retrieveAllByEntityAndRelation(params, Document.class, connection, this);
    }

    private CreateDocumentFunc createDocumentFunc(Document document) {
        return new CreateDocumentFunc(document, this);
    }

    private RetrieveDocumentsByEntityAndRelationFunc retrieveDocumentsByEntityAndRelationFunc(
            EntityAndRelationParams params) {
        return new RetrieveDocumentsByEntityAndRelationFunc(params, this);
    }

    public static class UpdateDocumentFunc extends ProcessFunc<Document, Document> {
        private final DocumentPersister documentPersister;

        UpdateDocumentFunc(Document input, DocumentPersister documentPersister) {
            super(input);

            this.documentPersister = documentPersister;
        }

        @Override
        public Document process() {
            return PersistenceUtil.process(conn, new UpdateFunc<Document>(input) {
                @Override
                protected Document updateConcrete() throws SQLException {
                    PreparedStatement insert = conn
                            .prepareStatement(
                                    "INSERT INTO document (id, systemEntity, systemEntityRelation, primaryAuthor, " +
                                            "permalink, locale, title, version, summary) " +
                                            "VALUES (?,?,?,?,?,?,?,?,?)");
                    insert.setInt(1, input.getId());
                    insert.setInt(2, input.getSystemEntity());
                    insert.setString(3, input.getSystemEntityRelation().name());
                    insert.setInt(4, input.getPrimaryAuthor());
                    insert.setString(5, input.getPermalink());
                    insert.setString(6, input.getLocale().getCode());
                    insert.setString(7, input.getTitle());
                    insert.setInt(8, input.getVersion());
                    if (input.hasSummary()) {
                        insert.setString(9, input.getSummary());
                    } else {
                        insert.setNull(9, Types.VARCHAR);
                    }
                    insert.executeUpdate();

                    documentPersister.blockPersistenceHelper
                            .persistBlocks(input.getId(), input.getVersion(), input.getBlocks(), conn);

                    if (input instanceof AnswerSet) {
                        AnswerSetPersister answerSetPersister = documentPersister.answerSetPersisterProvider.get();
                        return PersistenceUtil.process(conn,
                                new RetrieveAnswerSetFunc(input.getId(), answerSetPersister));
                    } else {
                        return PersistenceUtil.process(conn,
                                new RetrieveDocumentFunc(input.getId(), documentPersister));
                    }
                }
            });
        }
    }

    private static class DeleteDocumentFunc extends DeleteFunc<Document> {
        private final DocumentPersister documentPersister;

        public DeleteDocumentFunc(Document document, DocumentPersister documentPersister) {
            super(document);

            this.documentPersister = documentPersister;
        }

        @Override
        public Document process() {
            try {
                documentPersister.blockPersistenceHelper.deleteBlocks(input.getId(), conn);

                PreparedStatement delete = conn.prepareStatement("DELETE FROM answerSet WHERE id=?");
                PreparedStatement deleteContrib =
                        conn.prepareStatement("DELETE FROM documentContributor WHERE document=?");
                PreparedStatement deleteD = conn.prepareStatement("DELETE FROM document WHERE id=?");
                PreparedStatement deleteSe = conn.prepareStatement("DELETE FROM systemEntity WHERE id=?");

                delete.setInt(1, input.getId());
                deleteContrib.setInt(1, input.getId());
                deleteD.setInt(1, input.getId());
                deleteSe.setInt(1, input.getId());

                delete.executeUpdate();
                deleteContrib.executeUpdate();
                deleteD.executeUpdate();
                int updateCount = deleteSe.executeUpdate();

                if (updateCount != 1) {
                    throw new GeneralException("Unexpected update count: " + updateCount);
                }

                return input;
            } catch (Exception e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class RetrieveDocumentKind extends RetrieveFunc<SystemEntityRelation> {
        public RetrieveDocumentKind(int documentId) {
            super(documentId);
        }

        @Override
        protected SystemEntityRelation retrieveConcrete() throws SQLException {
            PreparedStatement select = conn.prepareStatement(
                    "SELECT d.systemEntityRelation FROM document d WHERE d.id=?");
            select.setInt(1, input);

            ResultSet rs = select.executeQuery();

            if (!rs.next()) {
                throw new GeneralException("Document with id " + input + " not found.");
            }

            return SystemEntityRelation.valueOf(rs.getString(1));
        }
    }

    public static class RetrieveDocumentsByEntity extends ProcessFunc<Integer, List<Document>> {
        private final DocumentPersister documentPersister;

        RetrieveDocumentsByEntity(int entityId, DocumentPersister documentPersister) {
            super(entityId);

            this.documentPersister = documentPersister;
        }

        @Override
        public List<Document> process() {
            try {
                return retrieveLatestDocumentsByEntity(input, conn);
            } catch (SQLException e) {
                throw new GeneralException("Error retrieving documens for story " + input, e);
            }
        }

        private List<Document> retrieveLatestDocumentsByEntity(int entityId, Connection connection)
                throws SQLException {
            List<Document> result = Lists.newArrayList();

            PreparedStatement select = connection.prepareStatement(DOCUMENT_NOT_ANSWER_SET);
            select.setInt(1, entityId);

            ResultSet rs = select.executeQuery();
            SupportDataUtils dataUtils = documentPersister.supportDataUtilsFactory.create(connection);
            while (rs.next()) {
                documentPersister.loadDocument(connection, result, rs, dataUtils, new Document());
            }

            return result;
        }
    }

    public static class CreateDocumentFunc extends CreateFunc<Document> {
        private final DocumentPersister documentPersister;

        CreateDocumentFunc(Document input, DocumentPersister documentPersister) {
            super(input);

            this.documentPersister = documentPersister;
        }

        @Override
        protected Document createConcrete() {
            try {
                documentPersister.createConcrete(input, conn);

                return documentPersister.get(input.getId(), conn);
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    private Document createConcrete(Document document, Connection connection) throws SQLException {
        if (document.getPermalink() == null) {
            document.setPermalink(GUID.get());
        }

        if (Locale.UNKNOWN.equals(document.getLocale())) {
            document.setLocale(ENGLISH);
        }

        final PreparedStatement insert = connection
                .prepareStatement(
                        "INSERT INTO document (id, systemEntity, systemEntityRelation, primaryAuthor, permalink, " +
                                "locale, title, version, summary) " +
                                "VALUES (?,?,?,?,?,?,?,?,?)");
        insert.setInt(1, document.getId());
        insert.setInt(2, document.getSystemEntity());
        insert.setString(3, document.getSystemEntityRelation().name());
        insert.setInt(4, document.getPrimaryAuthor());
        insert.setString(5, document.getPermalink());
        insert.setString(6, document.getLocale().getCode());
        insert.setString(7, document.getTitle());
        insert.setInt(8, INITIAL_VERSION);
        if (document.hasSummary()) {
            insert.setString(9, document.getSummary());
        } else {
            insert.setNull(9, Types.VARCHAR);
        }

        final int insertCount = insert.executeUpdate();
        if (insertCount != 1) {
            throw new GeneralException("Unexpected insert count: " + insertCount);
        }

        blockPersistenceHelper.persistBlocks(document.getId(), document.getVersion(), document.getBlocks(), connection);
        persistContributors(document, connection);

        return document;
    }

    public static class RetrieveDocumentFunc extends RetrieveFunc<Document> {
        private final DocumentPersister documentPersister;

        RetrieveDocumentFunc(Integer id, DocumentPersister documentPersister) {
            super(id);
            this.documentPersister = documentPersister;
        }

        @Override
        protected Document retrieveConcrete() throws SQLException {
            PreparedStatement select = conn
                    .prepareStatement(DOCUMENT_SELECT_BY_ID);

            select.setInt(1, input);
            ResultSet rs = select.executeQuery();

            if (!rs.next()) {
                String message = LocaleFactory.get(CommonI18nErrorMessages.class).documentWithIdNotFound(input);
                throw new NotFoundException(message);
            }

            SupportDataUtils dataUtils = documentPersister.supportDataUtilsFactory.create(conn);

            return documentPersister.loadDocument(conn, rs, dataUtils, new Document());
        }
    }

    public static class RetrieveDocumentsByEntityAndRelationFunc
            extends ProcessFunc<EntityAndRelationParams, List<Document>> {
        private final DocumentPersister documentPersister;

        RetrieveDocumentsByEntityAndRelationFunc(EntityAndRelationParams params, DocumentPersister documentPersister) {
            super(params);

            this.documentPersister = documentPersister;
        }

        @Override
        public List<Document> process() {
            try {
                return retrieveLatestByEntityAndRelation(input, Document.class, conn);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }

        protected <T extends Document> List<T> retrieveLatestByEntityAndRelation(
                EntityAndRelationParams params,
                Class<T> documentClass,
                Connection connection) throws SQLException {
            /*
             * FIXME: Filtering by permissions and owner is not working Reenable
			 * once filtering by authorization is working for multiple entity tipes
			 *
			 * final PreparedStatement select =
			 * AuthorizationQueryUtil.prepareAuthorizationSelect(
			 * "SELECT se.id, se.version" +
			 * ", d.systemEntity, d.systemEntityRelation, d.primaryAuthor, d.permalink, d.public"
			 * + " FROM document d JOIN systemEntity se ON d.id=se.id"
			 * ," WHERE d.systemEntity=? AND systemEntityRelation=?" , new
			 * Object[]{params.getEntityId(), params.getRelation().name()} , params
			 * , connection);
			 */

            List<T> result = new ArrayList<T>();
            PreparedStatement select = connection.prepareStatement(DOCUMENT_SELECT);
            select.setInt(1, params.getEntityId());
            select.setString(2, params.getRelation().name());

            ResultSet rs = select.executeQuery();

            try {
                SupportDataUtils dataUtils = documentPersister.supportDataUtilsFactory.create(connection);
                while (rs.next()) {
                    T d = documentClass.newInstance();
                    documentPersister.loadDocument(connection, result, rs, dataUtils, d);
                }
            } catch (InstantiationException e) {
                throw new GeneralException("Unable to create : " + documentClass.getSimpleName());
            } catch (IllegalAccessException e) {
                throw new GeneralException("Unable to create : " + documentClass.getSimpleName());
            }

            return result;
        }
    }

    private <T extends Document> void loadDocument(
            Connection connection,
            List<T> result,
            ResultSet rs,
            SupportDataUtils dataUtils,
            T d) throws SQLException {
        loadDocument(connection, rs, dataUtils, d);
        result.add(d);
    }

    private <T extends Document> T loadDocument(
            Connection connection,
            ResultSet rs,
            SupportDataUtils dataUtils,
            T d)
            throws SQLException {
        fillDocument(d, rs);
        retrieveContributors(d, connection);
        d.setAuthorAddress(dataUtils.getPrimaryAddress(d.getPrimaryAuthor()));
        d.setBlocks(blockPersistenceHelper.getBlocks(d, connection));

        return d;
    }

    public static class RetrieveOriginalByEntityAndRelationFunc
            extends ProcessFunc<EntityAndRelationParams, Document> {
        private final DocumentPersister documentPersister;

        RetrieveOriginalByEntityAndRelationFunc(
                EntityAndRelationParams params,
                DocumentPersister documentPersister) {
            super(params);

            this.documentPersister = documentPersister;
        }

        @Override
        public Document process() {
            try {
                return retrieveAllByEntityAndRelation(input, Document.class, conn, documentPersister).get(0);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }
    }

    private static <T extends Document> List<T> retrieveAllByEntityAndRelation(
            EntityAndRelationParams params,
            Class<T> documentClass,
            Connection connection,
            DocumentPersister documentPersister) throws SQLException {
        List<T> result = new ArrayList<T>();

		/*
         * FIXME: Filtering by permissions and owner is not working Reenable
		 * once filtering by authorization is working for multiple entity tipes
		 */
        /*
         * final PreparedStatement select =
		 * AuthorizationQueryUtil.prepareAuthorizationSelect(
		 * "SELECT se.id, se.version" +
		 * ", d.systemEntity, d.systemEntityRelation, d.primaryAuthor, d.permalink, d.public"
		 * + " FROM document d JOIN systemEntity se ON d.id=se.id"
		 * ," WHERE d.systemEntity=? AND systemEntityRelation=?" , new
		 * Object[]{params.getEntityId(), params.getRelation().name()} , params
		 * , connection); /*
		 */

        PreparedStatement select = connection.prepareStatement(DOCUMENT_SELECT + " ORDER BY version DESC");
        select.setInt(1, params.getEntityId());
        select.setString(2, params.getRelation().name());

        ResultSet rs = select.executeQuery();

        try {
            SupportDataUtils dataUtils = documentPersister.supportDataUtilsFactory.create(connection);
            while (rs.next()) {
                documentPersister.loadDocument(connection, result, rs, dataUtils, documentClass.newInstance());
            }
        } catch (InstantiationException e) {
            throw new GeneralException("Unable to create : " + documentClass.getSimpleName());
        } catch (IllegalAccessException e) {
            throw new GeneralException("Unable to create : " + documentClass.getSimpleName());
        }

        return result;
    }

    public <T extends Document> T retrieveLatestDocumentByRelation(
            EntityAndRelationParams params,
            final Class<T> documentClass) {
        return persistenceService.process(new ProcessFunc<EntityAndRelationParams, T>(params) {
            @Override
            public T process() {
                try {
                    return retrieveLatestDocumentByRelation(input, documentClass, conn);
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public <T extends Document> T retrieveLatestDocumentByRelation(
            EntityAndRelationParams params,
            Class<T> documentClass,
            Connection connection) throws SQLException {
        PreparedStatement select = connection.prepareStatement(DOCUMENT_SELECT);
        select.setInt(1, params.getEntityId());
        select.setString(2, params.getRelation().name());

        ResultSet rs = select.executeQuery();

        try {
            if (rs.next()) {
                SupportDataUtils dataUtils = supportDataUtilsFactory.create(connection);
                return loadDocument(connection, rs, dataUtils, documentClass.newInstance());
            }
        } catch (InstantiationException e) {
            throw new GeneralException("Unable to create : " + documentClass.getSimpleName());
        } catch (IllegalAccessException e) {
            throw new GeneralException("Unable to create : " + documentClass.getSimpleName());
        }

        return null;
    }

    private static void fillDocument(Document doc, ResultSet rs) throws SQLException {
        doc.setId(rs.getInt(1));
        doc.setVersion(rs.getInt(2));
        doc.setEntity(rs.getInt(3));
        doc.setCreated(rs.getTimestamp(8) == null ? null : new Date(rs.getTimestamp(8).getTime()));
        doc.setUpdated(rs.getTimestamp(9) == null ? null : new Date(rs.getTimestamp(9).getTime()));

        SystemEntityRelation relation = SystemEntityRelation.valueOf(rs.getString(4));
        doc.setSystemEntityRelation(relation);

        doc.setPrimaryAuthor(rs.getInt(5));
        doc.setPrimaryAuthorFirstName(rs.getString(10));
        doc.setPrimaryAuthorLastName(rs.getString(11));
        doc.setLocale(Locale.fromCode(rs.getString(12)));
        doc.setPermalink(rs.getString(6));
        doc.setPublic(rs.getBoolean(7));
        doc.setTitle(Strings.nullToEmpty(rs.getString(13)));
        doc.setSummary(rs.getString(14));
    }

    private static void retrieveContributors(Document document, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(CONTRIBUTOR_SELECT);
        ps.setInt(1, document.getId());
        ResultSet rs = ps.executeQuery();

        List<DocumentContributor> contributors = new ArrayList<DocumentContributor>();
        for (; rs.next(); ) {
            DocumentContributor contrib = new DocumentContributor();
            contrib.setContributor(rs.getInt(1));
            String roleString = rs.getString(2);
            contrib.setRole(DocumentContributorRole.valueOf(roleString));
            contributors.add(contrib);
        }
        document.setContributors(contributors);
    }

    protected static void delete(Document document, Connection connection) throws SQLException {
        deleteContributors(document, connection);
        PreparedStatement delete = connection.prepareStatement("DELETE FROM document WHERE id=?");
        delete.setInt(1, document.getId());

        delete.executeUpdate();
    }

    private static void persistContributors(Document document, Connection connection) throws SQLException {
        List<DocumentContributor> contributors = document.getContributors();
        if (contributors == null) {
            return;
        }

        PreparedStatement ps = connection
                .prepareStatement("INSERT INTO documentContributor (document, user, role)" + " VALUES(?,?,?)");

        for (DocumentContributor contrib : contributors) {
            ps.setInt(1, document.getId());
            ps.setInt(2, contrib.getContributor());
            ps.setString(3, contrib.getRole().name());
            ps.addBatch();
        }

        ps.executeBatch();
    }

    private static void deleteContributors(Document document, Connection connection) throws SQLException {
        PreparedStatement delete = connection.prepareStatement(
                "DELETE FROM documentContributor WHERE document=?");
        delete.setInt(1, document.getId());
        delete.executeUpdate();
    }

    @Override
    public Document get(int id) {
        return persistenceService.process(new RetrieveDocumentFunc(id, this));
    }

    @Override
    public Document get(int id, Connection connection) {
        return persistenceService.process(connection, new RetrieveDocumentFunc(id, this));
    }

    public static class EntityAndRelationParams extends AuthParam<EntityAndRelationParams> {
        private final int entityId;
        private final SystemEntityRelation relation;

        public EntityAndRelationParams(
                int entityId,
                SystemEntityRelation relation,
                Integer effectiveId) {
            super(0, 0, null, false, ACCESS_MODE_EXPLICIT, effectiveId);

            this.entityId = entityId;
            this.relation = relation;
        }

        public int getEntityId() {
            return entityId;
        }

        public SystemEntityRelation getRelation() {
            return relation;
        }

        @Override
        public EntityAndRelationParams noLimit() {
            return this;
        }
    }
}
