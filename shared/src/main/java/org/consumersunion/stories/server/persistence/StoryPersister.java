package org.consumersunion.stories.server.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
import org.consumersunion.stories.server.persistence.DocumentPersister.EntityAndRelationParams;
import org.consumersunion.stories.server.persistence.funcs.CreateFunc;
import org.consumersunion.stories.server.persistence.funcs.DeleteFunc;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.consumersunion.stories.server.persistence.funcs.UpdateFunc;
import org.consumersunion.stories.server.solr.story.documents.UpdateStoryAuthorDocument;
import org.consumersunion.stories.server.util.StringUtil;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
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

@Component
public class StoryPersister implements Persister<Story> {
    private static final int STORY_POSITIONS_PAGE = 1000;

    private final SolrServer storySolrServer;
    private final PersistenceService persistenceService;
    private final ContactPersister contactPersister;
    private final DocumentPersister documentPersister;
    private final CollectionPersister collectionPersister;

    @Inject
    StoryPersister(
            @Named("solrStoryServer") SolrServer storySolrServer,
            PersistenceService persistenceService,
            ContactPersister contactPersister,
            DocumentPersister documentPersister,
            CollectionPersister collectionPersister) {
        this.storySolrServer = storySolrServer;
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

    public void updateAuthor(final ProfileSummary profileSummary) {
        SolrQuery sQuery = new SolrQuery();

        sQuery.setQuery("*:*");
        sQuery.addFilterQuery("authorId:" + profileSummary.getProfile().getId());
        sQuery.setRequestHandler("/search");

        try {
            QueryResponse response = storySolrServer.query(sQuery);

            List<SolrInputDocument> documents =
                    FluentIterable.from(response.getResults())
                            .transformAndConcat(new Function<SolrDocument, List<SolrInputDocument>>() {
                                @Override
                                public List<SolrInputDocument> apply(SolrDocument input) {
                                    int id = Integer.parseInt((String) input.getFieldValue("id"));

                                    UpdateStoryAuthorDocument update =
                                            new UpdateStoryAuthorDocument(id, profileSummary);

                                    return update.toDocuments();
                                }
                            }).toList();

            if (!documents.isEmpty()) {
                storySolrServer.add(documents);
                storySolrServer.commit();
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        private final SolrServer solrServer;

        DeleteStoryFunc(Story story, StoryPersister storyPersister) {
            super(story);

            solrServer = storyPersister.storySolrServer;
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
                    solrServer.deleteById(String.valueOf(input.getId()));
                    solrServer.commit();
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

        public String getBBoxSolr() {
            StringBuilder builder = new StringBuilder();
            builder.append("authorLocation:[");
            builder.append(southWest);
            builder.append(" TO ");
            builder.append(northEast);
            builder.append("]");

            return builder.toString();
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
        private final SolrServer solrServer;

        public RetrieveStorySummary(Integer input, boolean includeFullText, StoryPersister storyPersister) {
            super(input);

            this.includeFullText = includeFullText;
            this.storyPersister = storyPersister;
            solrServer = storyPersister.storySolrServer;
        }

        @Override
        public StorySummary process() {

            try {
                SolrQuery sQuery = processSearchQuery(input);

                QueryResponse result = solrServer.query(sQuery);

                List<StorySummary> storySummaries = storyPersister.processSolrResults(result, includeFullText, true,
                        conn);

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
        private final SolrServer solrServer;

        RetrieveStoriesPaged(StoryPagedRetrieveParams params,
                boolean includeFullText,
                StoryPersister storyPersister) {
            super(params);

            this.includeFullText = includeFullText;
            this.storyPersister = storyPersister;
            solrServer = storyPersister.storySolrServer;
        }

        @Override
        public List<StorySummary> process() {
            try {
                SolrQuery sQuery = processSearchQuery(input);
                setSolrSort(sQuery, input);

                sQuery.setStart(input.getStart());
                sQuery.setRows(input.getLength());

                QueryResponse result = solrServer.query(sQuery);

                List<StorySummary> storySummaries =
                        storyPersister.processSolrResults(result, includeFullText, input.isIncludeCollections(), conn);

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
                        SolrQuery sQuery = processSearchQuery(input);
                        sQuery.addFilterQuery("collectionsId:" + input.getCollectionId());
                        sQuery.setStart(i);
                        sQuery.setRows(STORY_POSITIONS_PAGE);

                        QueryResponse result = storySolrServer.query(sQuery);

                        List<StoryPosition> batch = processSolrResults(result);
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
        private final SolrServer solrServer;

        CountStories(
                StoryPagedRetrieveParams params,
                StoryPersister storyPersister) {
            super(params);

            solrServer = storyPersister.storySolrServer;
        }

        @Override
        public Integer process() {
            try {
                input.noLimit();
                SolrQuery sQuery = processSearchQuery(input);

                QueryResponse result = solrServer.query(sQuery);
                Long numFound = result.getResults().getNumFound();
                return numFound.intValue();
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
                    SolrQuery sQuery = processSearchQuery(input);
                    sQuery.addFilterQuery("collectionsId:" + input.getCollectionId());

                    if (input.containBBox()) {
                        sQuery.addFilterQuery(input.getBBoxSolr());
                    }

                    if (input.containGeoFilter()) {
                        sQuery.addFilterQuery("{!bbox}");
                        sQuery.set("pt", input.getLocation());
                        sQuery.set("d", input.getDistance());
                        sQuery.set("sfield", "authorLocation");
                    }

                    QueryResponse result = storySolrServer.query(sQuery);
                    Long numFound = result.getResults().getNumFound();
                    return numFound.intValue();
                } catch (Exception e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private List<StorySummary> processSolrResults(
            QueryResponse result,
            boolean includeFullText,
            boolean includeCollections,
            Connection conn) {
        List<StorySummary> stories = new ArrayList<StorySummary>();
        Iterator<SolrDocument> iterator = result.getResults().iterator();

        while (iterator.hasNext()) {
            SolrDocument doc = iterator.next();
            Story story = instantiateStoryFromSolr(doc);

            Address address = new Address();
            address.setCity((String) doc.getFieldValue("authorCity"));
            address.setState((String) doc.getFieldValue("authorState"));
            address.setAddress1((String) doc.getFieldValue("authorAddress1"));
            address.setPostalCode((String) doc.getFieldValue("authorPostalCode"));

            String summaryText = (String) doc.getFieldValue("primaryText");
            if (summaryText != null) {
                summaryText = StringUtil.truncateString(summaryText, StorySummary.SUMMARY_SIZE);
            }

            List<String> notes = new ArrayList<String>();
            if (null != doc.getFieldValue("storyNotes")) {
                notes.addAll((java.util.Collection<String>) doc.getFieldValue("storyNotes"));
            }

            Object defaultContentId = doc.getFieldValue("defaultContentId");
            if (defaultContentId != null) {
                story.setDefaultContent((Integer) defaultContentId);
            }

            StorySummary storySummary = new StorySummary(story,
                    (String) doc.getFieldValue("storyTitle"),
                    summaryText,
                    (String) doc.getFieldValue("authorHandle"),
                    (String) doc.getFieldValue("authorGivenName"),
                    (String) doc.getFieldValue("authorSurname"),
                    (String) doc.getFieldValue("authorPrimaryEmail"),
                    (String) doc.getFieldValue("authorPrimaryPhone"),
                    notes,
                    address,
                    new DocumentsContainer());

            if (includeFullText) {
                storySummary.setFullText((String) doc.getFieldValue("primaryText"));
            }

            Set<String> tags = new LinkedHashSet<String>();
            if (null != doc.getFieldValue("storyTags")) {
                tags.addAll((java.util.Collection<String>) doc.getFieldValue("storyTags"));
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
            return "storyBodyPrivacy:true OR readAuths:"
                    + input.getEffectiveId()
                    + " OR ownerId:" + input.getEffectiveId();
        } else if (input.getAuthRelation() == ACCESS_MODE_EXPLICIT) {
            return ("readAuths:" + input.getEffectiveId() + " OR ownerId:" + input.getEffectiveId());
        } else if (input.getAuthRelation() == ACCESS_MODE_OWN) {
            return ("ownerId:" + input.getEffectiveId());
        } else if (input.getAuthRelation() == ACCESS_MODE_PRIVILEGED) {
            return ("readAuths:" + input.getEffectiveId());
        } else if (input.getAuthRelation() == ACCESS_MODE_PRIVATE) {
            return ("readAuths:" + input.getEffectiveId() + " AND ownerId:" + input.getEffectiveId());
        } else {
            throw new GeneralException("Unknown access mode: " + input.getAuthRelation());
        }
    }

    private List<StoryPosition> processSolrResults(QueryResponse result) {
        List<StoryPosition> storyPositions = new ArrayList<StoryPosition>();
        Iterator<SolrDocument> iterator = result.getResults().iterator();

        while (iterator.hasNext()) {
            SolrDocument doc = iterator.next();
            String storyLocation = (String) doc.getFieldValue("authorLocation");

            if (!Strings.isNullOrEmpty(storyLocation)) {
                Integer id = Integer.parseInt((String) doc.getFieldValue("id"));
                storyPositions.add(new StoryPosition(id, storyLocation));
            }
        }

        return storyPositions;
    }

    private static Story instantiateStoryFromSolr(SolrDocument doc) {
        Integer id = Integer.parseInt((String) doc.getFieldValue("id"));
        Integer version = (Integer) doc.getFieldValue("storyVersion");
        Story s = new Story(id, version);
        s.setUpdated((Date) doc.getFieldValue("lastModified"));
        s.setOwner((Integer) doc.getFieldValue("ownerId"));
        s.setPermalink((String) doc.getFieldValue("permalink"));
        s.setUpdated((Date) doc.getFieldValue("lastModified"));
        s.setCreated((Date) doc.getFieldValue("created"));
        final String fullName = getStoryteller((String) doc.getFieldValue("authorGivenName"),
                (String) doc.getFieldValue("authorSurname"));
        s.setStoryTeller(fullName);
        // TODO: need byline
        s.setByLine(fullName);
        // TODO: need 'published'
        s.setPublished(true);
        s.setDefaultContent((Integer) doc.getFieldValue("documentId"));
        s.setDocuments(instantiateDocumentIds(doc));
        s.setPublic((Boolean) doc.getFieldValue("storyBodyPrivacy"));

        return s;
    }

    private static Map<String, Set<Integer>> instantiateDocumentIds(SolrDocument doc) {
        String documentIdsFromDoc = (String) doc.getFieldValue("documentIds");

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

    private List<CollectionSummary> instantiateAttachedCollections(SolrDocument doc, Connection conn) {
        Set<Integer> collectionIds;
        java.util.Collection<Object> fieldValues = doc.getFieldValues("collectionsId");
        List<CollectionSummary> collectionSummaries = new ArrayList<CollectionSummary>();

        if (fieldValues != null) {
            collectionIds = FluentIterable.from(fieldValues)
                    .transform(new Function<Object, Integer>() {
                        @Override
                        public Integer apply(Object input) {
                            return (Integer) input;
                        }
                    }).toSet();

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

    private static SolrQuery processSearchQuery(StoryPagedRetrieveParams input) {
        SolrQuery sQuery = new SolrQuery();

        if (!Strings.isNullOrEmpty(input.getSearchText())) {
            sQuery.setQuery(TextHelper.processSearchText(Strings.nullToEmpty(input.getSearchText())));
        } else {
            sQuery.setQuery("*:*");
        }
        sQuery.addFilterQuery(StoryPersister.getAccessModeParams(input));
        sQuery.setRequestHandler("/search");

        if (input.getCollectionId() != null) {
            sQuery.addFilterQuery("collectionsId:" + input.getCollectionId());
        }

        if (input.getQuestionnaireId() != null) {
            sQuery.addFilterQuery("questionnaireId:" + input.getQuestionnaireId()
                    + " OR collectionsId:" + input.getQuestionnaireId());
        }

        if (input.getAuthorId() != null) {
            sQuery.addFilterQuery("authorId:" + input.getAuthorId());
        }

        if (input.containBBox()) {
            sQuery.addFilterQuery(input.getBBoxSolr());
        }

        return sQuery;
    }

    private static SolrQuery processSearchQuery(Integer id) {
        SolrQuery sQuery = new SolrQuery();

        sQuery.setRequestHandler("/search");

        if (id != null) {
            sQuery.addFilterQuery("id:" + id);
        }

        return sQuery;
    }

    private static void setSolrSort(SolrQuery sQuery, AuthParam<?> input) {
        SortField sortField = input.getSortField();
        ORDER order = input.isAscending() ? ORDER.asc : ORDER.desc;

        if (sortField == StorySortField.CITY) {
            sQuery.setSort("authorCity_sort", order);
        } else if (sortField == StorySortField.CREATED_NEW || sortField == StorySortField.CREATED_OLD) {
            sQuery.setSort("created", getOrder(sortField == StorySortField.CREATED_OLD));
        } else if (sortField == StorySortField.ID) {
            sQuery.setSort("storyId", order);
        } else if (sortField == StorySortField.MODIFIED_NEW || sortField == StorySortField.MODIFIED_OLD) {
            sQuery.setSort("lastModified", getOrder(sortField == StorySortField.MODIFIED_OLD));
        } else if (sortField == StorySortField.STATE) {
            sQuery.setSort("authorState_sort", order);
        } else if (sortField == StorySortField.STORYTELLER) {
            sQuery.setSort("authorSurname_sort", order);
        } else if (sortField == StorySortField.TITLE_A_Z || sortField == StorySortField.TITLE_Z_A) {
            sQuery.setSort("storyTitle_sort", getOrder(sortField == StorySortField.TITLE_A_Z));
        } else {
            sQuery.setSort("created", order);
        }
    }

    private static ORDER getOrder(boolean ascending) {
        return ascending ? ORDER.asc : ORDER.desc;
    }
}
