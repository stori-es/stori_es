package org.consumersunion.stories.server.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.dto.AuthParam;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.common.shared.service.datatransferobject.DocumentsContainer;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryPosition;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.Script;
import org.consumersunion.stories.server.index.ScriptFactory;
import org.consumersunion.stories.server.index.elasticsearch.SortOrder;
import org.consumersunion.stories.server.index.elasticsearch.UpdateByQuery;
import org.consumersunion.stories.server.index.elasticsearch.query.Query;
import org.consumersunion.stories.server.index.elasticsearch.query.QueryBuilder;
import org.consumersunion.stories.server.index.elasticsearch.query.bool.Bool;
import org.consumersunion.stories.server.index.elasticsearch.query.bool.BoolBuilder;
import org.consumersunion.stories.server.index.elasticsearch.search.Search;
import org.consumersunion.stories.server.index.elasticsearch.search.SearchBuilder;
import org.consumersunion.stories.server.index.story.StoryDocument;
import org.consumersunion.stories.server.persistence.DocumentPersister.EntityAndRelationParams;
import org.consumersunion.stories.server.persistence.funcs.CreateFunc;
import org.consumersunion.stories.server.persistence.funcs.DeleteFunc;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.consumersunion.stories.server.persistence.funcs.UpdateFunc;
import org.consumersunion.stories.server.util.StringUtil;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ANY;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_OWN;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PRIVATE;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PRIVILEGED;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PUBLIC;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ROOT;

import static com.google.common.base.Strings.nullToEmpty;

@Component
public class StoryPersister implements Persister<Story> {
    private static final int STORY_POSITIONS_PAGE = 1000;

    private final Indexer<StoryDocument> storyIndexer;
    private final ScriptFactory scriptFactory;
    private final PersistenceService persistenceService;
    private final ContactPersister contactPersister;
    private final DocumentPersister documentPersister;
    private final CollectionPersister collectionPersister;

    @Inject
    StoryPersister(
            Indexer<StoryDocument> storyIndexer,
            ScriptFactory scriptFactory,
            PersistenceService persistenceService,
            ContactPersister contactPersister,
            DocumentPersister documentPersister,
            CollectionPersister collectionPersister) {
        this.storyIndexer = storyIndexer;
        this.scriptFactory = scriptFactory;
        this.persistenceService = persistenceService;
        this.contactPersister = contactPersister;
        this.documentPersister = documentPersister;
        this.collectionPersister = collectionPersister;
    }

    @Override
    public Class<Story> getHandles() {
        return Story.class;
    }

    @Override
    public Story get(int id) {
        return persistenceService.process(new RetrieveStoryFunc(id));
    }

    @Override
    public Story get(int id, Connection connection) {
        return persistenceService.process(connection, new RetrieveStoryFunc(id));
    }

    public void deleteStory(int id) {
        Story story = get(id);

        persistenceService.process(new DeleteStoryFunc(story, this));
    }

    public Story create(Story story) {
        return persistenceService.process(new CreateStoryFunc(story));
    }

    public Story create(Story story, Connection conn) {
        return persistenceService.process(conn, new CreateStoryFunc(story));
    }

    public Story updateStory(Story story) {
        return persistenceService.process(new UpdateStoryFunc(story));
    }

    public StorySummary getStorySummary(int id, boolean includeFullText) {
        return persistenceService.process(new RetrieveStorySummary(id, includeFullText, this));
    }

    public List<StorySummary> getStoriesPaged(StoryPagedRetrieveParams params, boolean includeFullText) {
        return persistenceService.process(getStoriesPagedFunc(params, includeFullText));
    }

    public int countStories(StoryPagedRetrieveParams params) {
        return persistenceService.process(new CountStories(params, this));
    }

    public void updateAuthor(ProfileSummary profileSummary) {
        Query query = QueryBuilder.newBuilder().withTerm("authorId", profileSummary.getProfile().getId()).build();
        Script script = scriptFactory.createUpdateAuthorScript(profileSummary);
        storyIndexer.updateFromQuery(new UpdateByQuery(query, script));
    }

    public List<Integer> getStoriesId(int profileId) {
        return persistenceService.process(new RetrieveFunc<List<Integer>>(profileId) {
            @Override
            protected List<Integer> retrieveConcrete() throws SQLException {
                PreparedStatement select = conn.prepareStatement(
                        "SELECT id FROM story WHERE owner=? ORDER BY id DESC");
                select.setInt(1, input);

                ResultSet result = select.executeQuery();

                List<Integer> ids = new ArrayList<Integer>();
                while (result.next()) {
                    ids.add(result.getInt(1));
                }

                return ids;
            }
        });
    }

    public static class CreateStoryFunc extends CreateFunc<Story> {
        public CreateStoryFunc(Story input) {
            super(input);
        }

        @Override
        protected Story createConcrete() throws SQLException {
            PreparedStatement insert = conn
                    .prepareStatement(
                            "INSERT INTO story (id, owner, permalink, defaultContent, published, firstPublished, " +
                                    "byline)"
                                    + " VALUES (?,?,?,?,?,?,?)");
            insert.setInt(1, input.getId());
            if (input.getOwner() != null) {
                insert.setInt(2, input.getOwner());
            } else {
                insert.setNull(2, Types.INTEGER);
            }
            insert.setString(3, input.getPermalink());
            if (input.getDefaultContent() != null) {
                insert.setInt(4, input.getDefaultContent());
            } else {
                insert.setNull(4, Types.INTEGER);
            }
            insert.setBoolean(5, input.getPublished());

            Date publishedDate = input.getFirstPublished();
            if (publishedDate != null) {
                insert.setTimestamp(6, new Timestamp(publishedDate.getTime()));
            } else {
                insert.setNull(6, Types.DATE);
            }

            insert.setString(7, input.getByLine());

            int insertCount = insert.executeUpdate();
            if (insertCount != 1) {
                throw new GeneralException("Unexpected insert count: " + insertCount);
            }

            return input;
        }
    }

    public static class DeleteStoryFunc extends DeleteFunc<Story> {
        private final DocumentPersister documentPersister;
        private final Indexer<StoryDocument> indexer;

        DeleteStoryFunc(Story story, StoryPersister storyPersister) {
            super(story);

            this.indexer = storyPersister.storyIndexer;
            this.documentPersister = storyPersister.documentPersister;
        }

        @Override
        public Story process() {
            checkVersion();
            try {
                PreparedStatement deleteExtra = conn.prepareStatement("DELETE FROM collection_story WHERE story=?");
                deleteExtra.setInt(1, input.getId());
                deleteExtra.executeUpdate();

                deleteExtra = conn.prepareStatement("DELETE FROM tag WHERE systemEntity=?");
                deleteExtra.setInt(1, input.getId());
                deleteExtra.executeUpdate();

                deleteExtra = conn.prepareStatement("DELETE FROM address WHERE entity=?");
                deleteExtra.setInt(1, input.getId());
                deleteExtra.executeUpdate();

                deleteExtra = conn.prepareStatement("DELETE a FROM answer a " +
                        "JOIN answerSet ans ON a.answerSet=ans.id JOIN document d ON d.id=ans.id WHERE d" +
                        ".systemEntity=?");
                deleteExtra.setInt(1, input.getId());
                deleteExtra.executeUpdate();

                deleteExtra = conn.prepareStatement("DELETE ans FROM answerSet ans " +
                        "JOIN document d ON d.id=ans.id WHERE d.systemEntity=?");
                deleteExtra.setInt(1, input.getId());
                deleteExtra.executeUpdate();

                PreparedStatement update = conn.prepareStatement("UPDATE story SET defaultContent=? WHERE id=?");
                update.setNull(1, Types.INTEGER);
                update.setInt(2, input.getId());
                update.executeUpdate();

                for (SystemEntityRelation relation : new SystemEntityRelation[]{
                        SystemEntityRelation.BODY,
                        SystemEntityRelation.NOTE,
                        SystemEntityRelation.ANSWER_SET,
                        SystemEntityRelation.ATTACHMENT}) {
                    List<Document> docs = documentPersister.retrieveAllByEntityAndRelation(
                            new EntityAndRelationParams(input.getId(), relation, 0), conn);
                    for (Document doc : docs) {
                        DocumentPersister.delete(doc, conn);
                    }
                }

                deleteExtra = conn.prepareStatement("DELETE FROM document WHERE systemEntity=?");
                deleteExtra.setInt(1, input.getId());
                deleteExtra.executeUpdate();

                PreparedStatement delete = conn.prepareStatement("DELETE FROM story WHERE id=?");
                delete.setInt(1, input.getId());

                int updateCount = delete.executeUpdate();
                if (updateCount != 1) {
                    throw new GeneralException("Unexpected delete count: " + updateCount);
                }

                try {
                    indexer.delete(input.getId());
                } catch (Exception e) {
                    throw new GeneralException(e);
                }

                deleteEntityRecordAndUpdateInput();

                return input;
            } catch (final SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class RetrieveStoryFunc extends RetrieveFunc<Story> {
        public RetrieveStoryFunc(Integer id) {
            super(id);
        }

        @Override
        protected Story retrieveConcrete() {
            try {
                return retrieveStory(input, conn);
            } catch (final SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class RetrieveByPermalink extends ProcessFunc<String, Story> {
        public RetrieveByPermalink(String input) {
            super(input);
        }

        @Override
        public Story process() {
            try {
                PreparedStatement ps = conn
                        .prepareStatement("SELECT id FROM story WHERE permalink=?");
                ps.setString(1, input);

                ResultSet results = ps.executeQuery();
                if (!results.next()) {
                    throw new GeneralException("No Story with Permalink " + input + " found.");
                }

                return retrieveStory(results.getInt(1), conn);
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    private static Story retrieveStory(int storyId, Connection conn) throws SQLException {
        PreparedStatement select = conn
                .prepareStatement("SELECT se.id, se.version, se.lastModified, se.public"
                        + ", s.owner, s.permalink, s.published, s.firstPublished, s.defaultContent, dAns.id, s" +
                        ".byline, se.created"
                        + " FROM story s " + "JOIN systemEntity se ON s.id=se.id "
                        + "LEFT JOIN document dAns ON dAns.systemEntity=s.id AND dAns.systemEntityRelation='"
                        + SystemEntityRelation.ANSWER_SET.name() + "' " + "WHERE s.id=?");
        select.setInt(1, storyId);

        ResultSet rs = select.executeQuery();
        if (!rs.next()) {
            String message = LocaleFactory.get(CommonI18nErrorMessages.class).storyWithIdNotFound(storyId);
            throw new NotFoundException(message);
        }

        PreparedStatement documentsSelect = conn.prepareStatement(
                "SELECT d.id, d.primaryAuthor, d.permalink, d.systemEntity, d.systemEntityRelation, bc.textType, bc" +
                        ".content, d.title " +
                        // It's the se.version that locks us into the current version.
                        "FROM document d JOIN systemEntity sd ON d.id=sd.id AND d.version=sd.version " +
                        "LEFT JOIN block_content bc ON bc.document=d.id AND bc.version=d.version " +
                        "WHERE d.systemEntity=?");
        documentsSelect.setInt(1, storyId);

        ResultSet documentsRs = documentsSelect.executeQuery();

        return instantiateStory(rs, documentsRs);
    }

    public static class UpdateStoryFunc extends UpdateFunc<Story> {
        public UpdateStoryFunc(Story input) {
            super(input);
        }

        @Override
        protected Story updateConcrete() throws SQLException {
            PreparedStatement update = conn.prepareStatement("UPDATE story SET"
                    + " owner=?, permalink=?, defaultContent=?, published=?, firstPublished=?"
                    + " WHERE id=?");

            if (input.getOwner() == null) {
                update.setNull(1, Types.INTEGER);
            } else {
                update.setInt(1, input.getOwner());
            }

            update.setString(2, input.getPermalink());
            if (input.getDefaultContent() != null) {
                update.setInt(3, input.getDefaultContent());
            } else {
                update.setNull(3, Types.INTEGER);
            }
            update.setBoolean(4, input.getPublished());
            Date publishedDate = input.getFirstPublished();
            if (publishedDate != null) {
                update.setTimestamp(5, new Timestamp(publishedDate.getTime()));
            } else {
                update.setNull(5, Types.DATE);
            }

            update.setInt(6, input.getId());

            int updateCount = update.executeUpdate();
            if (updateCount != 1) {
                throw new GeneralException("Unexpected update count: " + updateCount);
            }

            return input;
        }
    }

    protected String limitCharacters(String input, int cutoff) {
        String pruned = removeOpenCloseTags(input);
        pruned = removeSingleTags(pruned);
        pruned = pruned.trim();

        final StringBuilder result = new StringBuilder(pruned);

        for (int i = cutoff - 1; i < result.length(); i++) {
            String c = result.substring(i, i + 1);
            if (!c.matches("[a-zA-Z]")) {
                result.delete(i, result.length());
            }
        }

        if (result.length() < pruned.length()) {
            result.append("...");
        }

        return result.toString();
    }

    private String removeOpenCloseTags(String input) {
        Pattern openCloseTag = Pattern.compile("<[^/]*?>(.*?)</[^/]*?>");

        String result = input;
        Matcher matcher = openCloseTag.matcher(result);

        for (; matcher.find(); ) {
            result = matcher.replaceAll("$1");
            matcher = openCloseTag.matcher(result);
        }

        return result;
    }

    private String removeSingleTags(String input) {
        Pattern openCloseTag = Pattern.compile("<[^/]*?/>");

        String result = input;
        Matcher matcher = openCloseTag.matcher(result);

        for (; matcher.find(); ) {
            result = matcher.replaceAll("");
            matcher = openCloseTag.matcher(result);
        }

        return result;
    }

    public static class StoryPagedRetrieveParams extends AuthParam<StoryPagedRetrieveParams> {
        public static StoryPagedRetrieveParams fromSearch(
                StorySearchParameters storySearchParameters,
                Integer effectiveSubject) {
            if (TextHelper.isGeoSearchToken(storySearchParameters.getSearchToken())) {
                String searchToken = storySearchParameters.getSearchToken();
                storySearchParameters.setLocalisation(TextHelper.extractLocalisation(searchToken));
                storySearchParameters.setDistance(TextHelper.extractDistance(searchToken));
            }

            StoryPagedRetrieveParams params = new StoryPagedRetrieveParams(
                    storySearchParameters.getStart(),
                    storySearchParameters.getLength(),
                    storySearchParameters.getSortField(),
                    storySearchParameters.isAscending(),
                    storySearchParameters.getSearchToken(),
                    storySearchParameters.getCollectionId(),
                    storySearchParameters.getQuestionnaireId(),
                    storySearchParameters.getAuthorId(),
                    storySearchParameters.getNorthEast(),
                    storySearchParameters.getSouthWest(),
                    storySearchParameters.getLocalisation(),
                    storySearchParameters.getDistance(),
                    storySearchParameters.getAccessMode(),
                    effectiveSubject);

            params.setIncludeData(storySearchParameters.isIncludeData());

            return params;
        }

        private String searchText;
        private Integer collectionId;
        private Integer questionnaireId;
        private Integer authorId;
        private String northEast;
        private String southWest;
        private String location;
        private String distance;
        private String accessModeParams;
        private boolean includeCollections = true;
        private boolean includeData = true;

        public StoryPagedRetrieveParams(
                int start,
                int length,
                SortField sortField,
                boolean ascending,
                String searchText,
                Integer collectionId,
                Integer questionnaireId,
                Integer authorId,
                String northEast,
                String southWest,
                String location,
                String distance,
                int relation,
                Integer effectiveId) {
            super(start, length, sortField, ascending, relation, effectiveId);

            this.searchText = searchText;
            this.collectionId = collectionId;
            this.questionnaireId = questionnaireId;
            this.authorId = authorId;
            this.northEast = northEast;
            this.southWest = southWest;
            this.location = location;
            this.distance = distance;
        }

        // For serialization
        public StoryPagedRetrieveParams() {
        }

        public String getSearchText() {
            return searchText;
        }

        public String getAccessModeParams() {
            this.accessModeParams = StoryPersister.getAccessModeParams(this);
            return accessModeParams;
        }

        public Integer getCollectionId() {
            return collectionId;
        }

        public Integer getQuestionnaireId() {
            return questionnaireId;
        }

        public Integer getAuthorId() {
            return authorId;
        }

        public String getNorthEast() {
            return northEast;
        }

        public String getSouthWest() {
            return southWest;
        }

        public String getLocation() {
            return location;
        }

        public String getDistance() {
            return distance;
        }

        public Boolean containBBox() {
            return !Strings.isNullOrEmpty(northEast)
                    && !Strings.isNullOrEmpty(southWest);
        }

        public Boolean containGeoFilter() {
            return !Strings.isNullOrEmpty(location)
                    && !Strings.isNullOrEmpty(distance);
        }

        @Override
        public StoryPagedRetrieveParams noLimit() {
            return new StoryPagedRetrieveParams(0, 0, getSortField(), isAscending(), getSearchText(), getCollectionId(),
                    getQuestionnaireId(), getAuthorId(), getNorthEast(), getSouthWest(), getLocation(), getDistance(),
                    getAuthRelation(), getEffectiveId());
        }

        public StoryPagedRetrieveParams setIncludeCollections(boolean includeCollections) {
            this.includeCollections = includeCollections;
            return this;
        }

        public boolean isIncludeCollections() {
            return includeCollections;
        }

        public void setIncludeData(boolean includeData) {
            this.includeData = includeData;
        }

        public boolean isIncludeData() {
            return includeData;
        }
    }

    public static class RetrieveStoryIds extends RetrieveFunc<List<Integer>> {
        public RetrieveStoryIds(int organization) {
            super(organization);
        }

        @Override
        protected List<Integer> retrieveConcrete() throws SQLException {
            PreparedStatement ps = conn
                    .prepareStatement("SELECT DISTINCT s.id FROM story s JOIN collection_story cs ON cs.story=s.id " +
                            "JOIN systemEntity se ON cs.collection=se.id WHERE se.owner=?");
            ps.setInt(1, input);

            List<Integer> storyIds = Lists.newArrayList();
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                storyIds.add(results.getInt(1));
            }

            return storyIds;
        }
    }

    static class RetrieveStorySummary extends ProcessFunc<Integer, StorySummary> {
        private final boolean includeFullText;
        private final StoryPersister storyPersister;

        public RetrieveStorySummary(Integer input, boolean includeFullText, StoryPersister storyPersister) {
            super(input);

            this.includeFullText = includeFullText;
            this.storyPersister = storyPersister;
        }

        @Override
        public StorySummary process() {
            try {
                StoryDocument storyDocument = storyPersister.storyIndexer.get(input);

                List<StorySummary> storySummaries = storyPersister.procesIndexResults(
                        Collections.singletonList(storyDocument), includeFullText, true, conn);

                if (!storySummaries.isEmpty()) {
                    return storySummaries.get(0);
                }
            } catch (Exception e) {
                throw new GeneralException(e);
            }

            throw new NotFoundException();
        }
    }

    public RetrieveStoriesPaged getStoriesPagedFunc(final StoryPagedRetrieveParams params, boolean includeFullText) {
        return new RetrieveStoriesPaged(params, includeFullText, this);
    }

    static class RetrieveStoriesPaged extends ProcessFunc<StoryPagedRetrieveParams, List<StorySummary>> {
        private final boolean includeFullText;
        private final StoryPersister storyPersister;

        RetrieveStoriesPaged(StoryPagedRetrieveParams params,
                boolean includeFullText,
                StoryPersister storyPersister) {
            super(params);

            this.includeFullText = includeFullText;
            this.storyPersister = storyPersister;
        }

        @Override
        public List<StorySummary> process() {
            try {
                SearchBuilder searchBuilder = SearchBuilder.newBuilder();
                setSolrSort(searchBuilder, input);

                Bool bool = processSearchQuery(input);
                Query query = QueryBuilder.ofBool(bool);

                Search search = searchBuilder
                        .withQuery(query)
                        .withFrom(input.getStart())
                        .withSize(input.getLength())
                        .build();

                List<StoryDocument> storyDocuments = storyPersister.storyIndexer.search(search);
                List<StorySummary> storySummaries = storyPersister.procesIndexResults(storyDocuments, includeFullText,
                        input.isIncludeCollections(), conn);

                if (input.isIncludeData()) {
                    for (StorySummary storySummary : storySummaries) {
                        setDocuments(storySummary);
                        processAddress(storySummary);
                    }
                }

                return storySummaries;
            } catch (Exception e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }

        private void setDocuments(StorySummary storySummary) {
            List<Document> storyDocuments = storyPersister.documentPersister
                    .retrieveStoryDocuments(storySummary.getStory(), conn);
            storySummary.addDocuments(storyDocuments);
        }

        private void processAddress(StorySummary story) {
            Integer ownerId = story.getStory().getOwner();
            List<Address> addresses = storyPersister.contactPersister.retrieveAddress(ownerId, conn);
            if (addresses != null) {
                for (Address address : addresses) {
                    String status = address.getGeoCodeStatus();

                    if (!Strings.isNullOrEmpty(status)
                            && Address.GeoCodeStatus.valueOf(status) == Address.GeoCodeStatus.SUCCESS) {
                        story.setAddress(address);
                    }
                }
            }
        }
    }

    public static String getStoryteller(String userName, String lastName) {
        String fullName;
        if (userName != null && !userName.isEmpty()) {
            fullName = userName + " ";
        } else {
            fullName = "? ";
        }

        if (lastName != null && !lastName.isEmpty()) {
            fullName = fullName + lastName;
        } else {
            fullName = fullName + "?";
        }

        if (fullName.contains("? ?")) {
            fullName = "anonymous";
        }
        return fullName;
    }

    public List<StoryPosition> getStoriesPosition(StoryPagedRetrieveParams params) {
        return persistenceService.process(new ProcessFunc<StoryPagedRetrieveParams, List<StoryPosition>>(params) {
            @Override
            public List<StoryPosition> process() {
                try {
                    List<StoryPosition> storyPositions = new ArrayList<StoryPosition>();
                    for (int i = 0; i < input.getLength(); i += STORY_POSITIONS_PAGE) {
                        Bool bool = processSearchQuery(input);

                        Search search = SearchBuilder.newBuilder()
                                .withQuery(QueryBuilder.ofBool(bool))
                                .withFrom(i)
                                .withSize(STORY_POSITIONS_PAGE)
                                .build();

                        List<StoryDocument> storyDocuments = storyIndexer.search(search);

                        List<StoryPosition> batch = procesIndexResults(storyDocuments);
                        storyPositions.addAll(batch);
                    }

                    return storyPositions;
                } catch (Exception e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    static class CountStories extends ProcessFunc<StoryPagedRetrieveParams, Integer> {
        private final StoryPersister storyPersister;

        CountStories(
                StoryPagedRetrieveParams params,
                StoryPersister storyPersister) {
            super(params);
            this.storyPersister = storyPersister;
        }

        @Override
        public Integer process() {
            try {
                input.noLimit();
                Bool bool = processSearchQuery(input);

                Query query = QueryBuilder.ofBool(bool);

                return (int) storyPersister.storyIndexer.count(SearchBuilder.ofQuery(query));
            } catch (Exception e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }
    }

    public int countStoriesByCollection(StoryPagedRetrieveParams params) {
        return persistenceService.process(new ProcessFunc<StoryPagedRetrieveParams, Integer>(params) {
            @Override
            public Integer process() {
                try {
                    Bool bool = processSearchQuery(input);
                    Query query = QueryBuilder.ofBool(bool);

                    return (int) storyIndexer.count(SearchBuilder.ofQuery(query));
                } catch (Exception e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private List<StorySummary> procesIndexResults(
            List<StoryDocument> documents,
            boolean includeFullText,
            boolean includeCollections,
            Connection conn) {
        List<StorySummary> stories = new ArrayList<StorySummary>();

        for (StoryDocument doc : documents) {
            Story story = instantiateStoryFromDocument(doc);

            Address address = new Address();
            address.setCity(doc.getAuthorCity());
            address.setState(doc.getAuthorState());
            address.setAddress1(doc.getAuthorAddress1());
            address.setPostalCode(doc.getAuthorPostalCode());

            String summaryText = doc.getPrimaryText();
            if (summaryText != null) {
                summaryText = StringUtil.truncateString(summaryText, StorySummary.SUMMARY_SIZE);
            }

            List<String> notes = new ArrayList<String>();
            if (doc.getStoryNotes() != null) {
                notes.addAll(doc.getStoryNotes());
            }

            if (doc.getDefaultContentId() != null) {
                story.setDefaultContent(doc.getDefaultContentId());
            }

            StorySummary storySummary = new StorySummary(story,
                    doc.getTitle(),
                    summaryText,
                    null,
                    doc.getAuthorGivenName(),
                    doc.getAuthorSurname(),
                    doc.getAuthorPrimaryEmail(),
                    doc.getAuthorPrimaryPhone(),
                    notes,
                    address,
                    new DocumentsContainer());

            if (includeFullText) {
                storySummary.setFullText(doc.getPrimaryText());
            }

            Set<String> tags = new LinkedHashSet<String>();
            if (doc.getTags() != null) {
                tags.addAll(doc.getTags());
            }

            storySummary.setTags(tags);
            try {
                if (includeCollections) {
                    storySummary.setCollections(instantiateAttachedCollections(doc, conn));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }

            stories.add(storySummary);
        }

        return stories;
    }

    private static String getAccessModeParams(AuthParam<?> input) {
        // NOTE: it's confusingly named, but 'storyBodyPrivacy:true' => Story is public.
        if (input.getAuthRelation() == ACCESS_MODE_ROOT) {
            return ("*:*");
        } else if ((input.getEffectiveId() == null && (input.getAuthRelation() == ACCESS_MODE_ANY) || input
                .getAuthRelation() == ACCESS_MODE_PUBLIC)) {
            return "storyBodyPrivacy:true";
        } else if (input.getAuthRelation() == ACCESS_MODE_ANY) {
            return "(storyBodyPrivacy:true OR readAuths:" + input.getEffectiveId()
                    + " OR ownerId:" + input.getEffectiveId() + ")";
        } else if (input.getAuthRelation() == ACCESS_MODE_EXPLICIT) {
            return "(readAuths:" + input.getEffectiveId() + " OR ownerId:" + input.getEffectiveId() + ")";
        } else if (input.getAuthRelation() == ACCESS_MODE_OWN) {
            return "ownerId:" + input.getEffectiveId();
        } else if (input.getAuthRelation() == ACCESS_MODE_PRIVILEGED) {
            return "readAuths:" + input.getEffectiveId();
        } else if (input.getAuthRelation() == ACCESS_MODE_PRIVATE) {
            return "(readAuths:" + input.getEffectiveId() + " AND ownerId:" + input.getEffectiveId() + ")";
        } else {
            throw new GeneralException("Unknown access mode: " + input.getAuthRelation());
        }
    }

    private List<StoryPosition> procesIndexResults(List<StoryDocument> storyDocuments) {
        List<StoryPosition> storyPositions = new ArrayList<StoryPosition>();

        for (StoryDocument doc : storyDocuments) {
            String storyLocation = doc.getAuthorLocation();

            if (!Strings.isNullOrEmpty(storyLocation)) {
                storyPositions.add(new StoryPosition(doc.getId(), storyLocation));
            }
        }

        return storyPositions;
    }

    private static Story instantiateStoryFromDocument(StoryDocument doc) {
        int id = doc.getId();
        Integer version = doc.getStoryVersion();
        Story s = new Story(id, version);
        s.setOwner(doc.getOwnerId());
        s.setPermalink(doc.getPermalink());
        s.setUpdated(doc.getLastModified());
        s.setCreated(doc.getCreated());
        String fullName = getStoryteller(doc.getAuthorGivenName(), doc.getAuthorSurname());
        s.setStoryTeller(fullName);
        // TODO: need byline
        s.setByLine(fullName);
        // TODO: need 'published'
        s.setPublished(true);
        s.setDefaultContent(doc.getDefaultContentId());
        s.setDocuments(instantiateDocumentIds(doc));
        s.setPublic(doc.getStoryBodyPrivacy());

        return s;
    }

    private static Map<String, Set<Integer>> instantiateDocumentIds(StoryDocument doc) {
        String documentIdsFromDoc = doc.getDocumentIds();

        if (documentIdsFromDoc != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(documentIdsFromDoc, new TypeReference<Map<String, Set<Integer>>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Maps.newHashMap();
    }

    private List<CollectionSummary> instantiateAttachedCollections(StoryDocument doc, Connection conn) {
        List<CollectionSummary> collectionSummaries = new ArrayList<CollectionSummary>();

        Set<Integer> collectionIds = doc.getCollectionsId();

        if (collectionIds != null && !collectionIds.isEmpty()) {
            List<Collection> collections = collectionPersister.getPartial(Lists.newArrayList(collectionIds), conn);
            for (Collection collection : collections) {
                if (!collection.getDeleted()) {
                    collectionSummaries.add(new CollectionSummary(collection));
                }
            }
        }

        return collectionSummaries;
    }

    private static Story instantiateStory(ResultSet rs, ResultSet documentsRs) throws SQLException {
        Story s = new Story(rs.getInt(1), rs.getInt(2));

        Timestamp uts = rs.getTimestamp(3);
        if (uts != null) {
            s.setUpdated(new Date(uts.getTime()));
        }

        s.setPublic(rs.getBoolean(4));
        s.setOwner(rs.getInt(5));
        if (rs.wasNull()) {
            s.setOwner(null);
        }

        s.setPermalink(rs.getString(6));
        s.setPublished(rs.getBoolean(7));

        Timestamp pts = rs.getTimestamp(8);
        if (pts != null) {
            s.setFirstPublished(new Date(pts.getTime()));
        }

        Integer defaultId = rs.getInt(9);
        if (!rs.wasNull()) {
            s.setDefaultContent(defaultId);
        } else {
            s.setDefaultContent(null);
        }

        s.setByLine(rs.getString(11));

        Timestamp tscreated = rs.getTimestamp(12);
        if (tscreated != null) {
            s.setCreated(new Date(tscreated.getTime()));
        }

        while (documentsRs.next()) {
            int id = documentsRs.getInt(1);
            String relation = documentsRs.getString(5);

            s.addDocument(SystemEntityRelation.valueOf(relation), id);
        }

        return s;
    }

    private static Bool processSearchQuery(StoryPagedRetrieveParams input) {
        StringBuilder queryStringBuilder = new StringBuilder();

        BoolBuilder boolBuilder = BoolBuilder.newBuilder();

        if (!Strings.isNullOrEmpty(input.getSearchText())) {
            queryStringBuilder.append(TextHelper.processSearchText(nullToEmpty(input.getSearchText()))).append(" AND ");
        }

        queryStringBuilder.append(StoryPersister.getAccessModeParams(input));

        if (input.getCollectionId() != null) {
            appendQuery(queryStringBuilder, "collectionsId:", "AND").append(input.getCollectionId());
        }

        if (input.getQuestionnaireId() != null) {
            StringBuilder subBuilder = new StringBuilder();
            appendQuery(queryStringBuilder, subBuilder.append("(questionnaireId:").append(input.getQuestionnaireId())
                    .append(" OR collectionsId:").append(input.getQuestionnaireId()).append(")").toString(), "AND");
        }

        if (input.getAuthorId() != null) {
            appendQuery(queryStringBuilder, "authorId:", "AND").append(input.getAuthorId());
        }

        if (input.containBBox()) {
            boolBuilder.filter()
                    .withGeoBoundingBox("authorLocation", input.getNorthEast(), input.getSouthWest());
        }

        if (input.containGeoFilter()) {
            boolBuilder.filter()
                    .withGeoDistance("authorLocation", input.getDistance(), input.getLocation());
        }

        if (queryStringBuilder.length() > 0) {
            boolBuilder.must().withQueryString(queryStringBuilder.toString());
        }

        return boolBuilder.build();
    }

    private static StringBuilder appendQuery(StringBuilder query, String textToAppend, String operator) {
        if (query.length() > 0) {
            query.append(" ").append(operator).append(" ").append(textToAppend);
        }
        return query;
    }

    private static void setSolrSort(SearchBuilder searchBuilder, AuthParam<?> input) {
        SortField sortField = input.getSortField();
        SortOrder order = getOrder(input.isAscending());

        if (sortField == StorySortField.CITY) {
            searchBuilder.withSort("authorCity.keyword", order);
        } else if (sortField == StorySortField.CREATED_NEW || sortField == StorySortField.CREATED_OLD) {
            searchBuilder.withSort("created", getOrder(sortField == StorySortField.CREATED_OLD));
        } else if (sortField == StorySortField.ID) {
            searchBuilder.withSort("_id", order);
        } else if (sortField == StorySortField.MODIFIED_NEW || sortField == StorySortField.MODIFIED_OLD) {
            searchBuilder.withSort("lastModified", getOrder(sortField == StorySortField.MODIFIED_OLD));
        } else if (sortField == StorySortField.STATE) {
            searchBuilder.withSort("authorState.keyword", order);
        } else if (sortField == StorySortField.STORYTELLER) {
            searchBuilder.withSort("authorSurname.keyword", order);
        } else if (sortField == StorySortField.TITLE_A_Z || sortField == StorySortField.TITLE_Z_A) {
            searchBuilder.withSort("title.keyword", getOrder(sortField == StorySortField.TITLE_A_Z));
        } else {
            searchBuilder.withSort("created", order);
        }
    }

    private static SortOrder getOrder(boolean ascending) {
        return ascending ? SortOrder.ASC : SortOrder.DESC;
    }
}
