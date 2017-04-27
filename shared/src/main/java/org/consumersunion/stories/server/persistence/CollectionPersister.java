package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.common.shared.dto.AuthParam;
import org.consumersunion.stories.common.shared.dto.RetrievePagedCollectionsParams;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.CollectionSortField;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.common.shared.service.QuestionnaireMask;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.collection.CollectionDocument;
import org.consumersunion.stories.server.index.elasticsearch.SortOrder;
import org.consumersunion.stories.server.index.elasticsearch.query.Ids;
import org.consumersunion.stories.server.index.elasticsearch.query.QueryBuilder;
import org.consumersunion.stories.server.index.elasticsearch.query.bool.Bool;
import org.consumersunion.stories.server.index.elasticsearch.query.bool.BoolBuilder;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchBuilder;
import org.consumersunion.stories.server.persistence.funcs.CreateFunc;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveListFunc;
import org.consumersunion.stories.server.persistence.funcs.UpdateFunc;
import org.consumersunion.stories.server.security.RelationalAuthorizationQueryUtil;
import org.consumersunion.stories.server.util.StringUtil;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ANY;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_OWN;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PRIVATE;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PRIVILEGED;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PUBLIC;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ROOT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.BODY;
import static org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister.SearchByCollectionPagedParams;

@Component
public class CollectionPersister implements Persister<Collection>, MineCallbackProvider {
    private static final String SELECT_CLAUSE = "SELECT e.id, e.version, e.created, e.lastModified, e.owner, " + //1-5
            "n.permalink, n.profile, e.public, c.deleted, EXISTS(SELECT 1 FROM questionnaire q WHERE q.id=c.id) AS " +
            "isQuestionnaire, " + // 6-10
            "c.theme, c.previewKey, c.published, c.publishedDate, d.title "; // 7-15
    private static final String FROM_CLAUSE = "FROM systemEntity e " +
            "JOIN collection c ON e.id=c.id " +
            "JOIN entity n ON e.id=n.id " +
            "LEFT OUTER JOIN document d ON d.systemEntity=c.id AND d.systemEntityRelation='BODY' " +
            // systemEntity.version tracks the latest version of the document.
            "LEFT OUTER JOIN systemEntity de ON d.id=de.id AND de.version=d.version ";

    private final Indexer<CollectionDocument> collectionIndexer;
    private final PersistenceService persistenceService;
    private final DocumentPersister documentPersister;
    private final TagsPersistenceHelper tagsPersistenceHelper;
    private final AvailablePermalinkExtractor availablePermalinkExtractor;
    private final Provider<QuestionnaireI15dPersister> questionnaireI15dPersisterProvider;
    private final AuthorizationPersistenceHelper authorizationPersistenceHelper;

    @Inject
    CollectionPersister(
            Indexer<CollectionDocument> collectionIndexer,
            PersistenceService persistenceService,
            DocumentPersister documentPersister,
            TagsPersistenceHelper tagsPersistenceHelper,
            AvailablePermalinkExtractor availablePermalinkExtractor,
            Provider<QuestionnaireI15dPersister> questionnaireI15dPersisterProvider,
            AuthorizationPersistenceHelper authorizationPersistenceHelper) {
        this.collectionIndexer = collectionIndexer;
        this.persistenceService = persistenceService;
        this.documentPersister = documentPersister;
        this.tagsPersistenceHelper = tagsPersistenceHelper;
        this.availablePermalinkExtractor = availablePermalinkExtractor;
        this.questionnaireI15dPersisterProvider = questionnaireI15dPersisterProvider;
        this.authorizationPersistenceHelper = authorizationPersistenceHelper;
    }

    private CollectionPersister() {
        this(null, null, null, null, null, null, null);
    }

    @Override
    public Class<Collection> getHandles() {
        return Collection.class;
    }

    @Override
    public Collection get(int entityId) {
        return persistenceService.process(new RetrieveCollectionFunc(entityId, this));
    }

    @Override
    public Collection get(int entityId, Connection connection) {
        return persistenceService.process(connection, new RetrieveCollectionFunc(entityId, this));
    }

    public List<Collection> get(List<Integer> ids) {
        return persistenceService.process(new RetrieveCollectionsFunc(ids, this));
    }

    public List<Collection> get(List<Integer> ids, Connection connection) {
        return persistenceService.process(connection, new RetrieveCollectionsFunc(ids, this));
    }

    public List<Collection> getPartial(ArrayList<Integer> ids, Connection connection) {
        return persistenceService.process(connection, new RetrieveCollectionsFunc(ids, this, false, true));
    }

    public Collection create(Collection collection) {
        return persistenceService.process(new CreateCollectionFunc<Collection>(collection, this));
    }

    public CollectionData getCollectionData(int id) {
        return persistenceService.process(new RetrieveCollectionData(id, this));
    }

    public Collection retrieveByPermalink(String permalink) {
        return persistenceService.process(new RetrieveByPermalink(permalink, this));
    }

    public Collection createCollection(Collection collection) {
        try {
            return persistenceService.process(new CreateCollectionFunc<Collection>(collection, this));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Collection updateCollection(Collection collection) {
        return persistenceService.process(new UpdateCollectionFunc<Collection>(collection, this));
    }

    public List<CollectionData> retrievePagedCollections(RetrievePagedCollectionsParams params) {
        return persistenceService.process(new RetrievePagedCollectionsFunc(params, this));
    }

    public List<CollectionData> retrievePagedCollectionsByStory(RetrievePagedCollectionsParams params) {
        return persistenceService.process(new CollectionPersister.RetrievePagedCollectionByStoryFunc(params, this));
    }

    public List<CollectionData> retrievePagedCollectionsByUserWithoutStoryAssociated(
            RetrievePagedCollectionsParams params) {
        return persistenceService.process(new RetrieveCollectionsByUserWithoutStoryAssocFunc(params, this));
    }

    public CountCollectionsResult countCollections(RetrievePagedCollectionsParams params) {
        return persistenceService.process(new CountCollections(params, this));
    }

    public String getAvailablePermalink(String slug) {
        return persistenceService.process(new ProcessFunc<String, String>("/collections/" + slug) {
            @Override
            public String process() {
                try {
                    PreparedStatement select = conn.prepareStatement(
                            "SELECT e.permalink FROM collection c " +
                                    "JOIN entity e ON c.id = e.id " +
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

    static class CreateCollectionFunc<T extends Collection> extends CreateFunc<T> {
        private final DocumentPersister documentPersister;
        private final AuthorizationPersistenceHelper authorizationPersistenceHelper;

        public CreateCollectionFunc(
                T input,
                CollectionPersister collectionPersister) {
            super(input);

            documentPersister = collectionPersister.documentPersister;
            authorizationPersistenceHelper = collectionPersister.authorizationPersistenceHelper;
        }

        @Override
        protected T createConcrete() throws SQLException {
            if (input.getPermalink() == null) {
                if (input.getTitle() != null) {
                    input.setPermalink(StringUtil.generateSlug(input.getTitle()));
                } else {
                    input.setPermalink(StringUtil.generateRandomPassword(StringUtil.ALL_LOWER, 12));
                }
            }

            setPreviewKey(input);

            PreparedStatement insertCollection = conn.prepareStatement(
                    "INSERT INTO collection (id, theme, previewKey) VALUES(?, ?, ?)");
            insertCollection.setInt(1, input.getId());
            insertCollection.setInt(2, input.getTheme());
            insertCollection.setString(3, input.getPreviewKey());
            int insertCount = insertCollection.executeUpdate();
            if (insertCount != 1) {
                throw new GeneralException("Unexpected insert count: " + insertCount);
            }

            PreparedStatement insertEntity = conn.prepareStatement("UPDATE entity SET permalink=? WHERE id=?");
            insertEntity.setString(1, StringUtil.cleanPermalink(input.getPermalink()));
            insertEntity.setInt(2, input.getId());
            insertCount = insertEntity.executeUpdate();
            if (insertCount != 1) {
                throw new GeneralException("Unexpected insert count: " + insertCount);
            }

            if (!input.isQuestionnaire()) {
                CollectionStoryLinkPersistenceHelper.updateStoryLinks(input, conn);
                updateCollectionSources(input, conn);
            }

            authorizationPersistenceHelper.grantToAllProfilesInOrganization(conn, ROLE_ADMIN, input.getOwner(),
                    input.getId());

            Document bodyDocument = input.getBodyDocument();
            bodyDocument.setLocale(input.getLocale());
            bodyDocument.setEntity(input.getId());
            bodyDocument.setSystemEntityRelation(SystemEntityRelation.BODY);
            bodyDocument.setPermalink(input.getPermalink().toUpperCase() + "-BODY");
            documentPersister.create(bodyDocument, conn);

            return input;
        }
    }

    static class RetrieveByPermalink extends ProcessFunc<String, Collection> {
        private final CollectionPersister collectionPersister;

        public RetrieveByPermalink(String input, CollectionPersister collectionPersister) {
            super(input);

            this.collectionPersister = collectionPersister;
        }

        @Override
        public Collection process() {
            PreparedStatement select;
            try {
                select = conn.prepareStatement(SELECT_CLAUSE + FROM_CLAUSE + "WHERE n.permalink=? GROUP BY de.id");
                select.setString(1, input);

                ResultSet results = select.executeQuery();
                if (results.next()) {
                    return collectionPersister.instantiateCollection(results, conn, false);
                } else {
                    return null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }
    }

    protected static void retrieveTargetCollections(Collection collection, Connection conn)
            throws SQLException {
        PreparedStatement targetSelect =
                conn.prepareStatement("SELECT targetCollection FROM collection_sources WHERE sourceQuestionnaire=?");

        targetSelect.setInt(1, collection.getId());
        ResultSet targetResults = targetSelect.executeQuery();
        while (targetResults.next()) {
            collection.getTargetCollections().add(targetResults.getInt(1));
        }
    }

    protected static void retrieveCollectionSources(Collection collection, Connection conn)
            throws SQLException {
        PreparedStatement sourceSelect =
                conn.prepareStatement("SELECT sourceQuestionnaire FROM collection_sources WHERE targetCollection=?");

        sourceSelect.setInt(1, collection.getId());
        ResultSet sourceResults = sourceSelect.executeQuery();
        while (sourceResults.next()) {
            collection.getCollectionSources().add(sourceResults.getInt(1));
        }
    }

    private static void updateCollectionSources(Collection input, Connection conn)
            throws SQLException {
        PreparedStatement sourceDelete =
                conn.prepareStatement("DELETE FROM collection_sources WHERE targetCollection=?");
        sourceDelete.setInt(1, input.getId());
        sourceDelete.executeUpdate();

        if (input.getCollectionSources() != null && input.getCollectionSources().size() > 0) {
            PreparedStatement sourceInsert =
                    conn.prepareStatement(
                            "INSERT INTO collection_sources (sourceQuestionnaire, targetCollection) VALUES (?,?)");
            for (Integer sourceQuestionnaire : input.getCollectionSources()) {
                sourceInsert.setInt(1, sourceQuestionnaire);
                sourceInsert.setInt(2, input.getId());
                sourceInsert.addBatch();
            }
            sourceInsert.executeBatch();
        }
    }

    public static class RetrieveCollectionFunc extends RetrieveFunc<Collection> {
        private final boolean includeDeleted;
        private final CollectionPersister collectionPersister;

        public RetrieveCollectionFunc(Integer id, CollectionPersister collectionPersister, boolean includeDeleted) {
            super(id);

            this.collectionPersister = collectionPersister;
            this.includeDeleted = includeDeleted;
        }

        public RetrieveCollectionFunc(Integer id, CollectionPersister collectionPersister) {
            this(id, collectionPersister, true);
        }

        @Override
        protected Collection retrieveConcrete() throws SQLException {
            PreparedStatement select = conn.prepareStatement(SELECT_CLAUSE
                    + FROM_CLAUSE +
                    " WHERE e.id=?" + (includeDeleted ? "" : " AND c.deleted=0") + " GROUP BY de.id");
            select.setInt(1, input);

            ResultSet results = select.executeQuery();
            if (!results.next()) {
                String message = LocaleFactory.get(CommonI18nErrorMessages.class).collectionWithIdNotFound(
                        input.toString());
                throw new NotFoundException(message);
            }

            return collectionPersister.instantiateCollection(results, conn, false);
        }
    }

    static class RetrieveCollectionsFunc extends RetrieveListFunc<Collection> {
        private final boolean includeDeleted;
        private final boolean partial;
        private final CollectionPersister collectionPersister;

        public RetrieveCollectionsFunc(
                List<Integer> ids,
                CollectionPersister collectionPersister,
                boolean includeDeleted,
                boolean partial) {
            super(ids);

            this.collectionPersister = collectionPersister;
            this.includeDeleted = includeDeleted;
            this.partial = partial;
        }

        public RetrieveCollectionsFunc(List<Integer> ids, CollectionPersister collectionPersister) {
            this(ids, collectionPersister, true, false);
        }

        @Override
        protected List<Collection> retrieveConcrete() throws SQLException {
            List<String> queryStrings = FluentIterable.from(input)
                    .transform(new Function<Integer, String>() {
                        @Override
                        public String apply(Integer input) {
                            return "?";
                        }
                    }).toList();
            String queryString = Joiner.on(", ").join(queryStrings);

            PreparedStatement select = conn.prepareStatement(SELECT_CLAUSE
                    + FROM_CLAUSE +
                    " WHERE e.id IN (" + queryString + ") AND de.version IS NOT NULL" +
                    (includeDeleted ? "" : " AND c.deleted=0") + " GROUP BY de.id");

            for (int i = 0; i < input.size(); i++) {
                select.setInt(i + 1, input.get(i));
            }

            ResultSet results = select.executeQuery();

            List<Collection> collections = new ArrayList<Collection>();
            while (results.next()) {
                Collection collection = collectionPersister.instantiateCollection(results, conn, partial);
                collections.add(collection);
            }

            return collections;
        }
    }

    static class UpdateCollectionFunc<T extends Collection> extends UpdateFunc<T> {
        private final CollectionPersister collectionPersister;

        public UpdateCollectionFunc(T input, CollectionPersister collectionPersister) {
            super(input);

            this.collectionPersister = collectionPersister;
        }

        @Override
        protected T updateConcrete() throws SQLException {
            input.setBodyDocument(updateDocument(input.getBodyDocument()));

            collectionPersister.updatePublishState(input, conn);
            PreparedStatement update =
                    conn.prepareStatement("UPDATE collection c JOIN entity e ON c.id=e.id "
                            + "SET c.deleted=?, e.permalink=?, c.theme=?, c.published=?, c.publishedDate=?, c" +
                            ".previewKey=? "
                            + "WHERE c.id=?");

            update.setBoolean(1, input.getDeleted());

            if (input.getDeleted()) {
                update.setString(2, input.getId() + "-" + StringUtil.cleanPermalink(input.getPermalink()));
            } else {
                update.setString(2, StringUtil.cleanPermalink(input.getPermalink()));
            }
            update.setInt(3, input.getTheme());
            update.setBoolean(4, input.isPublished());

            Date publishedDate = input.getPublishedDate();
            if (publishedDate == null) {
                update.setNull(5, Types.TIMESTAMP);
                update.setString(6, input.getPreviewKey());
            } else {
                update.setTimestamp(5, new Timestamp(publishedDate.getTime()));
                update.setNull(6, Types.VARCHAR);
            }

            update.setInt(7, input.getId());

            int updateCount = update.executeUpdate();
            if (updateCount != 2) {
                throw new GeneralException("Unexpected update count: " + updateCount);
            }

            if (!input.isQuestionnaire() && !input.getDeleted()) {
                updateCollectionSources(input, conn);
                CollectionStoryLinkPersistenceHelper.updateStoryLinks(input, conn);
            }

            return input;
        }

        protected Document updateDocument(Document document) {
            if (document.isNew()) {
                return collectionPersister.documentPersister.create(document, conn);
            } else {
                return collectionPersister.documentPersister.updateDocument(document, conn);
            }
        }
    }

    private <T extends Collection> void updatePublishState(T collection, Connection conn) {
        Collection dbCollection =
                persistenceService.process(conn, new RetrieveCollectionFunc(collection.getId(), this));
        if (collection.isPublished()) {
            if (!dbCollection.isPublished()) {
                Date publishedDate = new Date();
                collection.setPublishedDate(publishedDate);
                collection.setPreviewKey(null);
            }
        } else {
            if (dbCollection.isPublished()) {
                collection.setPublishedDate(null);
                setPreviewKey(collection);
            }
        }
    }

    private static <T extends Collection> void setPreviewKey(T collection) {
        long hashValue = generatePreviewKey(collection);
        collection.setPreviewKey(Long.toHexString(hashValue));
    }

    private static <T extends Collection> long generatePreviewKey(T collection) {
        Checksum crc = new CRC32();
        Date lastModified = collection.getUpdated() == null ? new Date() : collection.getUpdated();
        byte[] bytes = (collection.getPermalink() + lastModified.getTime()).getBytes();
        crc.update(bytes, 0, bytes.length);

        return crc.getValue();
    }

    public RetrieveCollectionByPermalinkFunc retrieveByPermalinkFunc(final CollectionRetrieveKeysByURL params) {
        return new RetrieveCollectionByPermalinkFunc(params, this);
    }

    public static class RetrieveCollectionIdsFunc extends RetrieveFunc<List<Integer>> {
        public RetrieveCollectionIdsFunc(Integer organization) {
            super(organization);
        }

        @Override
        protected List<Integer> retrieveConcrete() {
            List<Integer> collections = Lists.newArrayList();
            try {
                PreparedStatement select = conn.prepareStatement(
                        "SELECT se.id FROM systemEntity se JOIN collection c ON c.id=se.id " +
                                "WHERE se.owner=?");
                select.setInt(1, input);

                ResultSet results = select.executeQuery();
                while (results.next()) {
                    collections.add(results.getInt(1));
                }
            } catch (SQLException e) {
                throw new GeneralException(e);
            }

            return collections;
        }
    }

    class RetrieveCollectionByPermalinkFunc extends ProcessFunc<CollectionRetrieveKeysByURL, Collection> {
        private final CollectionPersister collectionPersister;

        RetrieveCollectionByPermalinkFunc(
                CollectionRetrieveKeysByURL input,
                CollectionPersister collectionPersister) {
            super(input);

            this.collectionPersister = collectionPersister;
        }

        @Override
        public Collection process() {
            try {
                String authBit = input.getUser() == null ? "e.public=TRUE" : "(e.public=TRUE OR (class.class = '"
                        + Collection.class.getCanonicalName() + "' AND acl.sid="
                        + input.getUser().getId() + " AND acl.mask IN (" + BasePermission.READ.getMask() + ","
                        + BasePermission.ADMINISTRATION.getMask() + ")))";

                PreparedStatement select = conn
                        .prepareStatement(SELECT_CLAUSE
                                + FROM_CLAUSE
                                + (input.getUser() != null ? "LEFT OUTER JOIN acl_object_identity o ON o" +
                                ".object_id_identity=e.id LEFT OUTER JOIN acl_class class ON o.object_id_class = " +
                                "class.id LEFT OUTER JOIN acl_entry acl ON o.id = acl.acl_object_identity "
                                : "") + "WHERE (LOWER(n.permalink)=? OR LOWER(n.permalink)=?) AND " + authBit + " AND" +
                                " de.id IS NOT NULL " +
                                "GROUP BY de.id");

                select.setString(1, "/collections/" + input.getPermalink());
                select.setString(2, "/questionnaires/" + input.getPermalink());

                ResultSet results = select.executeQuery();
                if (!results.next()) {
                    String message = LocaleFactory.get(CommonI18nErrorMessages.class)
                            .collectionWithQuestionnaireAndPermalinkNotFound(input.getQuestionnaireTitle(),
                                    input.getPermalink());
                    throw new GeneralException(message);
                }

                return instantiateCollection(results, conn, false);
            } catch (final SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class RetrievePagedCollectionByStoryFunc
            extends ProcessFunc<RetrievePagedCollectionsParams, List<CollectionData>> {
        private final CollectionPersister collectionPersister;

        public RetrievePagedCollectionByStoryFunc(
                RetrievePagedCollectionsParams params,
                CollectionPersister collectionPersister) {
            super(params);

            this.collectionPersister = collectionPersister;
        }

        @Override
        public List<CollectionData> process() {
            try {
                RelationalAuthorizationQueryUtil util =
                        new RelationalAuthorizationQueryUtil(new CollectionPersister());
                PreparedStatement select =
                        util.prepareAuthorizationSelect(SELECT_CLAUSE,
                                FROM_CLAUSE + "JOIN collection_story cs ON c.id = cs.collection ",
                                "cs.story = ? AND de.version IS NOT NULL / GROUP BY de.id ",
                                new Object[]{input.getStoryId()}, input, conn, true,
                                BasePermission.READ.getMask());
                ResultSet results = select.executeQuery();

                List<CollectionData> collections = new ArrayList<CollectionData>();
                while (results.next()) {
                    Collection collection = collectionPersister.instantiateCollection(results, conn, false);
                    // TODO: see TASK-129
                    if (!collection.getDeleted() && (input.getQuestionnaireMask() == QuestionnaireMask
                            .QUESTIONNAIRE_MASK_ALL ||
                            (input.getQuestionnaireMask() == QuestionnaireMask.QUESTIONNAIRE_MASK_QUESTIONNAIRES &&
                                    collection.isQuestionnaire()) ||
                            input.getQuestionnaireMask() == QuestionnaireMask.QUESTIONNAIRE_MASK_NON_QUESTIONNAIRES)) {
                        Set<String> tags = collectionPersister.tagsPersistenceHelper.getTags(collection, conn);
                        collections.add(new CollectionData(collection, tags, null));
                    }
                }

                return collections;
            } catch (Exception e) {
                throw new GeneralException(e);
            }
        }
    }

    static class RetrievePagedCollectionsFunc
            extends ProcessFunc<RetrievePagedCollectionsParams, List<CollectionData>> {
        private final CollectionPersister collectionPersister;

        RetrievePagedCollectionsFunc(
                RetrievePagedCollectionsParams params,
                CollectionPersister collectionPersister) {
            super(params);

            this.collectionPersister = collectionPersister;
        }

        @Override
        public List<CollectionData> process() {
            try {
                BoolBuilder boolBuilder = processSearchQuery(input);

                SearchBuilder searchBuilder = SearchBuilder.newBuilder()
                        .withQuery(QueryBuilder.ofBool(boolBuilder.build()))
                        .withFrom(input.getStart())
                        .withSize(input.getLength());
                setSolrSort(searchBuilder, input);

                List<CollectionDocument> collectionDocuments =
                        collectionPersister.collectionIndexer.search(searchBuilder.build());
                List<CollectionData> collections = new ArrayList<CollectionData>();

                for (CollectionDocument doc : collectionDocuments) {
                    CollectionData cd = collectionPersister.instantiateCollectionFromSolr(doc, conn,
                            collectionPersister.tagsPersistenceHelper);
                    // TODO: see TASK-129
                    Collection collection = cd.getCollection();
                    if (!collection.getDeleted()) {
                        if (input.getQuestionnaireMask() == QuestionnaireMask.QUESTIONNAIRE_MASK_ALL ||
                                (input.getQuestionnaireMask() == QuestionnaireMask.QUESTIONNAIRE_MASK_QUESTIONNAIRES
                                        && collection.isQuestionnaire()) ||
                                (input.getQuestionnaireMask() == QuestionnaireMask
                                        .QUESTIONNAIRE_MASK_NON_QUESTIONNAIRES
                                        && !collection.isQuestionnaire())) {
                            collections.add(cd);
                        }
                    }

                    cd.setStoriesCount(collection.getStories().size());

                    if (input.isIncludeLinkedCollections()) {
                        if (collection.isQuestionnaire()) {
                            List<CollectionSummary> targetCollections = Lists.newArrayList();
                            for (Integer targetCollectionId : collection.getTargetCollections()) {
                                Collection targetCollection = PersistenceUtil.process(conn,
                                        new RetrieveCollectionFunc(targetCollectionId, collectionPersister));
                                targetCollections.add(new CollectionSummary(targetCollection));
                            }

                            cd.setTargetCollections(targetCollections);
                        } else {
                            SearchByCollectionPagedParams params = new SearchByCollectionPagedParams(collection
                                    .getId(),
                                    0, 20, ACCESS_MODE_EXPLICIT, input.getEffectiveId());
                            params.setRetrievePartials();

                            QuestionnaireI15dPersister questionnaireI15dPersister =
                                    collectionPersister.questionnaireI15dPersisterProvider.get();
                            List<QuestionnaireI15d> questionnaires =
                                    questionnaireI15dPersister.searchByCollectionsPaged(params, conn);
                            cd.setSourceQuestionnaires(questionnaires);
                        }
                    }
                }

                return collections;
            } catch (final Exception e) {
                throw new GeneralException(e);
            }
        }
    }

    static class RetrieveCollectionData extends ProcessFunc<Integer, CollectionData> {
        private final CollectionPersister collectionPersister;

        public RetrieveCollectionData(Integer id, CollectionPersister collectionPersister) {
            super(id);

            this.collectionPersister = collectionPersister;
        }

        @Override
        public CollectionData process() {
            try {
                Bool bool = BoolBuilder.newBuilder()
                        .must().withIds(Ids.fromInt(input))
                        .filter().addTerm("deleted", false)
                        .build();

                List<CollectionDocument> collectionDocuments =
                        collectionPersister.collectionIndexer.search(SearchBuilder.ofQuery(QueryBuilder.ofBool(bool)));

                CollectionData collectionData;
                CollectionDocument doc = Iterables.getFirst(collectionDocuments, null);
                if (doc != null) {
                    collectionData = collectionPersister.instantiateCollectionFromSolr(doc, conn,
                            collectionPersister.tagsPersistenceHelper);

                    Collection collection = collectionData.getCollection();
                    collectionData.setStoriesCount(collection.getStories().size());

                    if (collection.isQuestionnaire()) {
                        List<CollectionSummary> targetCollections = Lists.newArrayList();
                        for (Integer targetCollectionId : collection.getTargetCollections()) {
                            Collection targetCollection = PersistenceUtil.process(conn,
                                    new RetrieveCollectionFunc(targetCollectionId, collectionPersister));
                            targetCollections.add(new CollectionSummary(targetCollection));
                        }

                        collectionData.setTargetCollections(targetCollections);
                    } else {
                        SearchByCollectionPagedParams params = new SearchByCollectionPagedParams(collection.getId(),
                                0, 20, ACCESS_MODE_EXPLICIT, 0);
                        params.setRetrievePartials();

                        QuestionnaireI15dPersister questionnaireI15dPersister =
                                collectionPersister.questionnaireI15dPersisterProvider.get();
                        List<QuestionnaireI15d> questionnaires =
                                questionnaireI15dPersister.searchByCollectionsPaged(params, conn);
                        collectionData.setSourceQuestionnaires(questionnaires);
                    }
                } else {
                    throw new NotFoundException("Did not find index record for Collection " + input);
                }

                return collectionData;
            } catch (NotFoundException e) {
                throw e;
            } catch (final Exception e) {
                throw new GeneralException(e);
            }
        }
    }

    /**
     * Retrieves non-{@link org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire}, non-deleted
     * {@link Collection Collections} writable by the user.
     */
    static class RetrieveCollectionsByUserWithoutStoryAssocFunc
            extends ProcessFunc<RetrievePagedCollectionsParams, List<CollectionData>> {
        private final CollectionPersister collectionPersister;

        public RetrieveCollectionsByUserWithoutStoryAssocFunc(RetrievePagedCollectionsParams params,
                CollectionPersister collectionPersister) {
            super(params);

            this.collectionPersister = collectionPersister;
        }

        @Override
        public List<CollectionData> process() {
            try {
                RelationalAuthorizationQueryUtil util =
                        new RelationalAuthorizationQueryUtil(new CollectionPersister());
                PreparedStatement select = util
                        .prepareAuthorizationSelect(
                                SELECT_CLAUSE,
                                FROM_CLAUSE,
                                "de.version IS NOT NULL AND c.id NOT IN (select cs.collection from " +
                                        "collection_story " +

                                        "cs where story=?) / GROUP BY de.id ",
                                new Object[]{input.getStoryId()}, input, conn, true,
                                BasePermission.WRITE.getMask());

                ResultSet results = select.executeQuery();

                List<CollectionData> collections = new ArrayList<CollectionData>();
                while (results.next()) {
                    Collection collection = collectionPersister.instantiateCollection(results, conn, false);
                    // TODO: see TASK-129
                    if (input.getQuestionnaireMask() == QuestionnaireMask.QUESTIONNAIRE_MASK_ALL ||
                            (input.getQuestionnaireMask() == QuestionnaireMask.QUESTIONNAIRE_MASK_QUESTIONNAIRES &&
                                    collection.isQuestionnaire()) ||
                            input.getQuestionnaireMask() == QuestionnaireMask
                                    .QUESTIONNAIRE_MASK_NON_QUESTIONNAIRES) {
                        Set<String> tags = collectionPersister.tagsPersistenceHelper.getTags(collection, conn);
                        // TODO: see TASK-131
                        if (!collection.getDeleted() && !collection.isQuestionnaire()) {
                            collections.add(new CollectionData(collection, tags, null));
                        }
                    }
                }

                return collections;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class CountCollectionsResult {
        private final int nbCollections;
        private final int nbQuestionnaires;
        private final int totalCount;

        public CountCollectionsResult(int totalCount, int nbQuestionnaires) {
            this.nbCollections = totalCount - nbQuestionnaires;
            this.nbQuestionnaires = nbQuestionnaires;
            this.totalCount = totalCount;
        }

        public int getNbCollections() {
            return nbCollections;
        }

        public int getNbQuestionnaires() {
            return nbQuestionnaires;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }

    public static class CountCollectionsByStory
            extends ProcessFunc<RetrievePagedCollectionsParams, CountCollectionsResult> {
        public CountCollectionsByStory(final RetrievePagedCollectionsParams input) {
            super(input);
        }

        @Override
        public CountCollectionsResult process() {
            try {
                RelationalAuthorizationQueryUtil util =
                        new RelationalAuthorizationQueryUtil(new CollectionPersister());
                PreparedStatement select = util
                        .prepareAuthorizationSelect(
                                "SELECT COUNT(DISTINCT(id)) ",
                                "SELECT e.id ",
                                "FROM systemEntity e JOIN collection c ON e.id=c.id JOIN entity n ON e.id=n.id " +
                                        "JOIN " +
                                        "collection_story cs ON c.id = cs.collection ",
                                "cs.story = ? and c.deleted = 0", new Object[]{input.getStoryId()}, input.noLimit(),
                                conn, false,
                                BasePermission.READ.getMask());
                ResultSet rs = select.executeQuery();
                rs.next();
                return new CountCollectionsResult(rs.getInt(1), 0);
            } catch (final SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class CountCollections
            extends ProcessFunc<RetrievePagedCollectionsParams, CountCollectionsResult> {
        private final CollectionPersister collectionPersister;

        public CountCollections(
                RetrievePagedCollectionsParams input,
                CollectionPersister collectionPersister) {
            super(input);
            this.collectionPersister = collectionPersister;
        }

        @Override
        public CountCollectionsResult process() {
            try {

                BoolBuilder boolBuilder = processSearchQuery(input).filter().addTerm("isQuestionnaire", false).and();

                long collectionCount = collectionPersister.collectionIndexer.count(
                        SearchBuilder.ofQuery(QueryBuilder.ofBool(boolBuilder.build())));
                long questionnaireCount = collectionPersister.collectionIndexer.count(SearchBuilder.ofQuery(
                        QueryBuilder.ofBool(boolBuilder.filter().addTerm("isQuestionnaire", true).build())));
                return new CountCollectionsResult((int) collectionCount, (int) questionnaireCount);
            } catch (final Exception e) {
                throw new GeneralException(e);
            }
        }
    }

    public ProcessFunc<List<Collection>, Boolean> linkStoryAndCollectionFunc(
            final int storyId,
            List<Collection> collections) {
        return new ProcessFunc<List<Collection>, Boolean>(collections) {
            @Override
            public Boolean process() {
                try {
                    PreparedStatement insert = conn
                            .prepareStatement(
                                    "INSERT INTO collection_story (collection, story, clearedForPublicInclusion) " +
                                            "VALUES (?,?,?)");

                    for (Collection collection : input) {
                        insert.setInt(1, collection.getId());
                        insert.setInt(2, storyId);
                        insert.setBoolean(3, true);
                        insert.addBatch();
                    }

                    insert.executeBatch();

                    return true;
                } catch (Exception e) {
                    throw new GeneralException(e);
                }
            }
        };
    }

    protected Collection instantiateCollection(ResultSet results, Connection conn, boolean partial)
            throws SQLException {
        int id = results.getInt(1);
        int version = results.getInt(2);
        Collection collection = new Collection(id, version);

        Timestamp cts = results.getTimestamp(3);
        if (cts != null) {
            collection.setCreated(new Date(cts.getTime()));
        }

        Timestamp uts = results.getTimestamp(4);
        if (uts != null) {
            collection.setUpdated(new Date(uts.getTime()));
        }

        collection.setOwner(results.getInt(5));
        collection.setPermalink(results.getString(6));
        collection.setProfile(results.getInt(7));
        if (results.wasNull()) {
            collection.setProfile(null);
        }
        collection.setPublic(results.getBoolean(8));
        collection.setDeleted(results.getBoolean(9));
        collection.setQuestionnaire(results.getBoolean(10));
        collection.setTheme(results.getInt(11));

        collection.setPreviewKey(results.getString(12));
        collection.setPublished(results.getBoolean(13));

        Timestamp publishedDateTimestamp = results.getTimestamp(14);
        collection.setPublishedDate(
                publishedDateTimestamp == null ? null : new Date(publishedDateTimestamp.getTime()));

        if (!partial) {
            CollectionStoryLinkPersistenceHelper.loadAllStoryLinks(collection, conn);
            retrieveCollectionSources(collection, conn);
            retrieveTargetCollections(collection, conn);
        }

        List<Document> bodyDocuments = documentPersister.retrieveDocumentsByEntityAndRelation(
                new DocumentPersister.EntityAndRelationParams(collection.getId(), BODY, 0), conn);
        if (bodyDocuments.size() > 1) {
            throw new GeneralException("Unexpectedly found multiple 'BODY' documents.");
        } else if (bodyDocuments.size() == 1) {
            collection.setBodyDocument(bodyDocuments.get(0));
        }

        return collection;
    }

    protected CollectionData instantiateCollectionFromSolr(
            org.consumersunion.stories.server.index.Document doc,
            Connection conn,
            TagsPersistenceHelper tagsPersistenceHelper) {
        int id = doc.getId();

        try {
            PreparedStatement retrieve = conn
                    .prepareStatement(
                            "SELECT e.id, e.version, e.created, e.lastModified, e.owner, n.permalink, n.profile, " +
                                    "e" +
                                    ".public, " +
                                    "c.deleted, EXISTS(SELECT 1 FROM questionnaire q WHERE q.id=c.id) AS " +
                                    "isQuestionnaire, o.name AS ownerName, p.givenName AS ownerGivenName, p" +
                                    ".surname " +
                                    "AS ownerSurname, c.previewKey, c.published, c.publishedDate, c.theme " +
                                    "FROM systemEntity e JOIN collection c ON e.id=c.id JOIN entity n ON e.id=n" +
                                    ".id " +
                                    "LEFT OUTER JOIN organization o ON o.id=e.owner LEFT OUTER JOIN profile p ON " +
                                    "p.id" +
                                    " = e.owner " +
                                    "WHERE c.id=? AND c.deleted=0");
            retrieve.setInt(1, id);
            ResultSet results = retrieve.executeQuery();
            if (results.next()) {
                boolean isQuestionnaire = results.getBoolean(10);

                Collection collection;
                if (!isQuestionnaire) {
                    int version = results.getInt(2);
                    collection = new Collection(id, version);

                    Timestamp cts = results.getTimestamp(3);
                    if (cts != null) {
                        collection.setCreated(new Date(cts.getTime()));
                    }

                    Timestamp uts = results.getTimestamp(4);
                    if (uts != null) {
                        collection.setUpdated(new Date(uts.getTime()));
                    }

                    collection.setOwner(results.getInt(5));

                    List<Document> bodyDocuments = documentPersister.retrieveDocumentsByEntityAndRelation(
                            new DocumentPersister.EntityAndRelationParams(collection.getId(), BODY, 0), conn);
                    if (bodyDocuments.size() > 1) {
                        throw new GeneralException("Unexpectedly found multiple 'BODY' documents.");
                    }
                    collection.setBodyDocument(bodyDocuments.size() == 0 ? null : bodyDocuments.get(0));

                    collection.setPermalink(results.getString(6));
                    collection.setProfile(results.getInt(7));
                    if (results.wasNull()) {
                        collection.setProfile(null);
                    }
                    collection.setPublic(results.getBoolean(8));
                    collection.setDeleted(results.getBoolean(9));
                    collection.setQuestionnaire(false);
                    collection.setPreviewKey(results.getString(14));
                    collection.setPublished(results.getBoolean(15));
                    collection.setPublishedDate(results.getTimestamp(16));
                    collection.setTheme(results.getInt(17));
                    retrieveCollectionSources(collection, conn);
                    retrieveTargetCollections(collection, conn);
                } else {
                    QuestionnaireI15dPersister questionnaireI15dPersister = questionnaireI15dPersisterProvider
                            .get();
                    collection = questionnaireI15dPersister.get(id, conn);
                }

                String owner = results.getString("ownerName");
                if (Strings.isNullOrEmpty(owner)) {
                    owner = String.format("%s %s", results.getString("ownerGivenName"),
                            results.getString("ownerSurname"));
                }

                CollectionStoryLinkPersistenceHelper.loadAllStoryLinks(collection, conn);

                Set<String> tags = tagsPersistenceHelper.getTags(collection, conn);

                return new CollectionData(collection, tags, owner);
            }
            return null;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public String getMineWhereClause(final AuthParam<?> authParam) {
        if (authParam.getEffectiveId() != null) {
            return "e.owner = " + authParam.getEffectiveId();
        } else {
            return null;
        }
    }

    @Override
    public String getMineJoinClause(final AuthParam<?> authParam) {
        return MineCallbackProvider.DEFAULT_JOIN_CLAUSE;
    }

    @Override
    public boolean hasMineClause() {
        return true;
    }

    @Override
    public String getSortClause(final SortField sortField, final boolean ascending) {
        final StringBuilder sb = new StringBuilder(" ORDER BY ");
        sb.append("e.id");

        if (ascending) {
            sb.append(" ASC");
        } else {
            sb.append(" DESC");
        }

        return sb.toString();
    }

    public static final class CollectionRetrieveKeysByURL {
        private final String permalink;
        private final String questionnaireTitle;
        private final User user;

        public CollectionRetrieveKeysByURL(String permalink, String questionnaireTitle, User user) {
            this.permalink = permalink;
            this.questionnaireTitle = questionnaireTitle;
            this.user = user;
        }

        public String getPermalink() {
            return permalink;
        }

        public String getQuestionnaireTitle() {
            return questionnaireTitle;
        }

        public User getUser() {
            return this.user;
        }
    }

    public static class StoryCollectionParams extends AuthParam<StoryCollectionParams> {
        private final int collectionId;
        private final int storyId;

        public StoryCollectionParams(
                int start,
                int length,
                SortField sortField,
                boolean ascending,
                int collectionId,
                int relation,
                Integer effectiveId,
                int storyId) {
            super(start, length, sortField, ascending, relation, effectiveId);

            this.collectionId = collectionId;
            this.storyId = storyId;
        }

        public int getCollectionId() {
            return collectionId;
        }

        public int getStoryId() {
            return storyId;
        }

        @Override
        public StoryCollectionParams noLimit() {
            return new StoryCollectionParams(0, 0, getSortField(), isAscending(), getCollectionId(),
                    getAuthRelation(),
                    getEffectiveId(), getStoryId());
        }
    }

    public static class RemoveStoryFromCollectionFunc extends ProcessFunc<StoryCollectionParams, Integer> {
        public RemoveStoryFromCollectionFunc(StoryCollectionParams input) {
            super(input);
        }

        @Override
        public Integer process() {
            try {
                RelationalAuthorizationQueryUtil util = new RelationalAuthorizationQueryUtil(
                        new CollectionPersister());
                PreparedStatement select = util
                        .prepareAuthorizationSelect("SELECT SUM(count)",
                                "Select COUNT(*) AS count ",
                                "FROM systemEntity e JOIN collection c ON e.id=c.id JOIN collection_story cs ON " +
                                        "cs" +
                                        ".collection=c.id",
                                "cs.collection=? AND cs.story=? ",
                                new Object[]{input.getCollectionId(), input.getStoryId()}, input.noLimit(), conn,
                                false, BasePermission.READ.getMask());

                ResultSet rs = select.executeQuery();
                int count = 0;
                if (rs.next()) {
                    count = rs.getInt(1);
                }

                if (count > 0) {
                    CollectionStoryLinkPersistenceHelper.deleteStoryLink(input.getCollectionId(),
                            input.getStoryId(),
                            conn);
                }

                return count;
            } catch (final SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    private static String getAccessModeParams(RetrievePagedCollectionsParams input) {
        if (input.getAuthRelation() == ACCESS_MODE_ROOT) {
            return "*";
        } else {
            String solrField;
            if (input.getPermissionMask() == ROLE_CURATOR) {
                solrField = "writeAuths";
            } else if (input.getPermissionMask() == ROLE_ADMIN) {
                solrField = "adminAuths";
            } else if (input.getPermissionMask() == ROLE_READER) {
                solrField = "readAuths";
            } else {
                throw new GeneralException("Unknown permission mask: " + input.getPermissionMask());
            }

            if ((input.getEffectiveId() == null && (input.getAuthRelation() == ACCESS_MODE_ANY) ||
                    input.getAuthRelation() == ACCESS_MODE_PUBLIC)) {
                return "public:1";
            } else if (input.getAuthRelation() == ACCESS_MODE_ANY) {
                if (input.getPermissionMask() == BasePermission.READ.getMask()) {
                    return "public:1 OR readAuths:" + input.getEffectiveId() + " OR ownerId:" + input
                            .getEffectiveId();
                } else {
                    return solrField + ":" + input.getEffectiveId() + " OR ownerId:" + input.getEffectiveId();
                }
            } else if (input.getAuthRelation() == ACCESS_MODE_EXPLICIT) {
                return solrField + ":" + input.getEffectiveId() + " OR ownerId:" + input.getEffectiveId();
            } else if (input.getAuthRelation() == ACCESS_MODE_OWN) {
                return "ownerId:" + input.getEffectiveId();
            } else if (input.getAuthRelation() == ACCESS_MODE_PRIVILEGED) {
                return solrField + ":" + input.getEffectiveId();
            } else if (input.getAuthRelation() == ACCESS_MODE_PRIVATE) {
                return solrField + ":" + input.getEffectiveId() + " AND ownerId:" + input.getEffectiveId();
            } else {
                return "";
            }
        }
    }

    private static BoolBuilder processSearchQuery(RetrievePagedCollectionsParams input) {
        String searchText = TextHelper.processSearchText(input.getSearchText());
        String accessModeParams = " (" + getAccessModeParams(input) + ") AND deleted:false";

        return BoolBuilder.newBuilder().must().withQueryString(searchText + accessModeParams).and();
    }

    private static void setSolrSort(SearchBuilder searchBuilder, RetrievePagedCollectionsParams input) {
        SortField sortField = input.getSortField();

        if (sortField == CollectionSortField.CREATED_NEW) {
            searchBuilder.withSort("created", SortOrder.DESC);
        } else if (sortField == CollectionSortField.CREATED_OLD) {
            searchBuilder.withSort("created", SortOrder.ASC);
        } else if (sortField == CollectionSortField.MODIFIED_NEW) {
            searchBuilder.withSort("lastModified", SortOrder.DESC);
        } else if (sortField == CollectionSortField.MODIFIED_OLD) {
            searchBuilder.withSort("lastModified", SortOrder.ASC);
        } else if (sortField == CollectionSortField.TITLE_A_Z) {
            searchBuilder.withSort("title.keyword", SortOrder.ASC);
        } else if (sortField == CollectionSortField.TITLE_Z_A) {
            searchBuilder.withSort("title.keyword", SortOrder.DESC);
        } else {
            searchBuilder.withSort("created", SortOrder.ASC);
        }
    }
}
