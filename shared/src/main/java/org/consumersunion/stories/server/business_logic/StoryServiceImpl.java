package org.consumersunion.stories.server.business_logic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.service.datatransferobject.PagedData;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.exception.BadRequestException;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.export.StoryCsv;
import org.consumersunion.stories.server.export.StoryExport;
import org.consumersunion.stories.server.index.story.NewStoryIndexer;
import org.consumersunion.stories.server.index.story.UpdatedStoryDocumentIndexer;
import org.consumersunion.stories.server.index.story.UpdatedStoryIndexer;
import org.consumersunion.stories.server.persistence.AnswerSetPersister;
import org.consumersunion.stories.server.persistence.PersistenceService;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.consumersunion.stories.server.persistence.StoryPersister;
import org.consumersunion.stories.server.persistence.StoryPersister.StoryPagedRetrieveParams;
import org.consumersunion.stories.server.persistence.StoryTellersParams;
import org.consumersunion.stories.server.util.StringUtil;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ANY;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ROOT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

@Service
public class StoryServiceImpl implements StoryService {
    private static final String NOT_AUTHORIZED = "Not authorized to access the requested resource";
    private static final int PERMALINK_LENGTH = 20;
    private static final int MODE = 0;

    private final DocumentService documentService;
    private final AuthorizationService authService;
    private final PersistenceService persistenceService;
    private final NewStoryIndexer newStoryIndexer;
    private final UpdatedStoryIndexer updatedStoryIndexer;
    private final UpdatedStoryDocumentIndexer updatedStoryDocumentIndexer;
    private final StoryPersister storyPersister;
    private final AnswerSetPersister answerSetPersister;
    private final ProfilePersister profilePersister;
    private final UserService userService;

    @Inject
    StoryServiceImpl(
            PersistenceService persistenceService,
            NewStoryIndexer newStoryIndexer,
            UpdatedStoryIndexer updatedStoryIndexer,
            UpdatedStoryDocumentIndexer updatedStoryDocumentIndexer,
            DocumentService documentService,
            AuthorizationService authService,
            StoryPersister storyPersister,
            AnswerSetPersister answerSetPersister,
            ProfilePersister profilePersister,
            UserService userService) {
        this.persistenceService = persistenceService;
        this.newStoryIndexer = newStoryIndexer;
        this.updatedStoryIndexer = updatedStoryIndexer;
        this.updatedStoryDocumentIndexer = updatedStoryDocumentIndexer;
        this.storyPersister = storyPersister;
        this.documentService = documentService;
        this.authService = authService;
        this.answerSetPersister = answerSetPersister;
        this.profilePersister = profilePersister;
        this.userService = userService;
    }

    @Override
    public Story getStory(int id) {
        Story story = storyPersister.get(id);

        if (!authService.canRead(story)) {
            throw new NotAuthorizedException(NOT_AUTHORIZED);
        }

        return story;
    }

    @Override
    public StorySummary getStorySummary(int id) {
        return getStorySummary(id, true);
    }

    @Override
    public StorySummary getStorySummary(int id, boolean includeFullText) {
        try {
            StorySummary storySummary = storyPersister.getStorySummary(id, includeFullText);

            if (!authService.canRead(storySummary.getStory())) {
                throw new NotAuthorizedException(NOT_AUTHORIZED);
            }

            List<Document> documents = documentService.getStoryDocuments(storySummary.getStory());
            storySummary.addDocuments(documents);

            return storySummary;
        } catch (NotLoggedInException e) {
            throw new NotAuthorizedException(NOT_AUTHORIZED);
        }
    }

    @Override
    public Story createStory(Story story) {
        Connection connection = persistenceService.getConnection();
        try {
            return createStory(story, connection);
        } finally {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public Story createStory(Story story, Connection connection) {
        if (Strings.isNullOrEmpty(story.getPermalink())) {
            story.setPermalink(StringUtil.generateRandomPassword(MODE, PERMALINK_LENGTH));
        }

        story.setPublished(false);
        story.setByLine("No Byline");

        story = storyPersister.create(story, connection);
        story = storyPersister.get(story.getId(), connection);

        ProfileSummary profileSummary = profilePersister.getProfileSummary(story.getOwner(), connection);

        newStoryIndexer.index(story, profileSummary);

        return story;
    }

    @Override
    public Story updateStory(Story story) {
        Story dbStory = storyPersister.get(story.getId());

        if (authService.canWrite(dbStory)) {
            if (story.getId() > 0) {
                story = storyPersister.updateStory(story);

                updatedStoryIndexer.index(story);

                Document documentText = null;
                if (story.getDefaultContent() != null) {
                    documentText = documentService.getEntityDocument(story.getId(), story.getDefaultContent());
                }

                updatedStoryDocumentIndexer.index(story.getId(), documentText);

                return story;
            } else {
                throw new BadRequestException();
            }
        } else {
            throw new NotAuthorizedException();
        }
    }

    @Override
    public void deleteStory(int storyId) {
        Story story = storyPersister.get(storyId);

        if (authService.isUserAuthorized(ROLE_CURATOR, story)) {
            storyPersister.deleteStory(storyId);
        } else {
            throw new NotAuthorizedException();
        }
    }

    @Override
    public PagedData<StorySummary> getStories(StorySearchParameters storySearchParameters) {
        User user = userService.getLoggedInUser();
        Integer accessMode = updateAccessMode(storySearchParameters, user);

        if (user != null || accessMode == ACCESS_MODE_ANY) {
            Integer effectiveSubject = userService.getEffectiveSubject(user);
            return getStories(storySearchParameters, effectiveSubject);
        } else {
            return new PagedData<StorySummary>();
        }
    }

    @Override
    public PagedData<StorySummary> getStories(StorySearchParameters storySearchParameters, Integer effectiveSubject) {
        StoryPagedRetrieveParams searchParams =
                StoryPagedRetrieveParams.fromSearch(storySearchParameters, effectiveSubject);

        List<StorySummary> stories =
                storyPersister.getStoriesPaged(searchParams, storySearchParameters.isIncludeFullText());

        int count = getStoriesCount(searchParams);

        PagedData<StorySummary> pagedStories = new PagedData<StorySummary>();
        pagedStories.setData(stories).setTotalCount(count);

        return pagedStories;
    }

    @Override
    public int getStoriesCount(StorySearchParameters storySearchParameters, int profileId) {
        StoryPagedRetrieveParams searchParams =
                StoryPagedRetrieveParams.fromSearch(storySearchParameters, profileId);

        return getStoriesCount(searchParams);
    }

    @Override
    public StoryExport<StoryCsv> exportStories(int profileId, StorySearchParameters searchParameters) {
        // see SYSFOUR-518
        try {
            User user = userService.getUserForProfile(profileId);

            Integer accessMode = ACCESS_MODE_EXPLICIT;
            if (authService.isSuperUser(user)) {
                accessMode = ACCESS_MODE_ROOT;
            }

            int start = searchParameters.getStart();
            List<StoryCsv> storiesCsv = new ArrayList<StoryCsv>();

            StoryPagedRetrieveParams pagedParams = new StoryPagedRetrieveParams(start,
                    searchParameters.getLength(),
                    searchParameters.getSortField() == null ? StorySortField.CREATED_NEW : searchParameters
                            .getSortField(),
                    false, // ascending
                    searchParameters.getSearchToken(),
                    searchParameters.getCollectionId(),
                    searchParameters.getQuestionnaireId(),
                    searchParameters.getAuthorId(),
                    searchParameters.getNorthEast(),
                    searchParameters.getSouthWest(),
                    null, // location
                    searchParameters.getDistance(),
                    accessMode,
                    profileId);
            pagedParams.setIncludeCollections(false);

            int storiesCount = getStoriesCount(pagedParams);
            List<StorySummary> stories = persistenceService.process(
                    storyPersister.getStoriesPagedFunc(pagedParams, true));

            for (StorySummary item : stories) {
                String sanitizedText = Jsoup.parse(Strings.nullToEmpty(item.getFullText())).text();
                AnswerSet answerSet = getAnswerSet(item);
                storiesCsv.add(new StoryCsv(item, sanitizedText, answerSet));
            }

            return new StoryExport<StoryCsv>(storiesCsv, storiesCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new StoryExport<StoryCsv>(new ArrayList<StoryCsv>(), 0);
    }

    @Override
    public int getStorytellerCount(StoryTellersParams params) {
        return persistenceService.process(profilePersister.countStorytellersFunc(params));
    }

    @Override
    public void updateAuthor(ProfileSummary profileSummary) {
        storyPersister.updateAuthor(profileSummary);
    }

    @Override
    public List<Integer> getStoriesIds(int profileId) {
        if (!authService.isUserAuthorized(ROLE_READER, profileId)) {
            throw new NotAuthorizedException();
        }

        return storyPersister.getStoriesId(profileId);
    }

    private Integer updateAccessMode(StorySearchParameters storySearchParameters, User user) {
        Integer accessMode = storySearchParameters.getAccessMode();
        if (authService.isSuperUser(user)) {
            storySearchParameters.setAccessMode(ACCESS_MODE_ROOT);
        }

        return accessMode;
    }

    private int getStoriesCount(StoryPagedRetrieveParams searchParams) {
        return storyPersister.countStories(searchParams.noLimit());
    }

    private AnswerSet getAnswerSet(StorySummary item) {
        Integer answerSetId = item.getStory().getOnlyDocument(SystemEntityRelation.ANSWER_SET);

        if (answerSetId != null) {
            return answerSetPersister.get(answerSetId);
        }

        return null;
    }
}
