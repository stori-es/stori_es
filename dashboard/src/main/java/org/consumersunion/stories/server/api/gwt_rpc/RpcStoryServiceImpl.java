package org.consumersunion.stories.server.api.gwt_rpc;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcStoryService;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.common.shared.service.datatransferobject.PagedData;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryPosition;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.business_logic.DocumentService;
import org.consumersunion.stories.server.business_logic.StoryService;
import org.consumersunion.stories.server.business_logic.TagsService;
import org.consumersunion.stories.server.exception.BadRequestException;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.persistence.StoryPersister;
import org.consumersunion.stories.server.persistence.StoryPersister.StoryPagedRetrieveParams;
import org.consumersunion.stories.server.persistence.TextHelper;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_PUBLIC;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ROOT;
import static org.consumersunion.stories.common.shared.model.StorySortField.CREATED_NEW;

@Service("rpcStoryService")
public class RpcStoryServiceImpl extends RpcBaseServiceImpl implements RpcStoryService {
    private static final int MAX_STORY_COUNT = 50;

    @Inject
    private StoryService storyService;
    @Inject
    private DocumentService documentService;
    @Inject
    private StoryPersister storyPersister;
    @Inject
    private TagsService tagsService;

    @Override
    public DataResponse<StorySummary> getRecentStories(int collectionId, int storyCount) {
        if (storyCount > MAX_STORY_COUNT) {
            storyCount = MAX_STORY_COUNT;
        }

        return getStories(
                new StorySearchParameters(0, storyCount, CREATED_NEW, false, null, collectionId, ACCESS_MODE_PUBLIC));
    }

    @Override
    public DatumResponse<Story> getStory(int id) {
        DatumResponse<Story> response = new DatumResponse<Story>();

        try {
            Story story = storyService.getStory(id);
            response.setDatum(story);
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (Exception e) {
            response.addGlobalErrorMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public PagedDataResponse<StorySummary> getStories(StorySearchParameters storySearchParameters) {
        // see SYSFOUR-518
        PagedDataResponse<StorySummary> response = new PagedDataResponse<StorySummary>();
        try {
            PagedData<StorySummary> stories = storyService.getStories(storySearchParameters);
            response.setData(stories.getData());

            if (!stories.isEmpty()) {
                response.setStart(storySearchParameters.getStart());
                response.setTotalCount(stories.getTotalCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }
        return response;
    }

    @Override
    public DataResponse<StoryPosition> getStoriesPositionByCollection(
            Integer collectionId,
            String searchText,
            int relation) {
        DataResponse<StoryPosition> response = new DataResponse<StoryPosition>();
        try {
            User user = userService.getLoggedInUser(true);
            Integer accessMode = relation;
            if (authService.isSuperUser(user)) {
                accessMode = ACCESS_MODE_ROOT;
            }

            StoryPagedRetrieveParams countParams = new StoryPagedRetrieveParams(0, 10,
                    StorySortField.TITLE_A_Z,
                    true, searchText,
                    collectionId,
                    null, // questionnaire Id
                    null, // author ID
                    null, // north east
                    null, // south west
                    null, // location
                    null, // distance
                    accessMode, getEffectiveSubject(user));

            int countStories = storyPersister.countStoriesByCollection(countParams.noLimit());

            // Loading stories position
            StoryPagedRetrieveParams searchParams = new StoryPagedRetrieveParams(0, countStories,
                    StorySortField.TITLE_A_Z,
                    true, searchText,
                    collectionId,
                    null, // questionnaire Id
                    null, // author ID
                    null, // north east
                    null, // south west
                    null, // location
                    null, // distance
                    accessMode, getEffectiveSubject(user));
            List<StoryPosition> positions = storyPersister.getStoriesPosition(searchParams);
            response.setData(positions);
        } catch (Exception e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }

        return response;
    }

    public DatumResponse<Integer> getStoriesCount(StorySearchParameters storySearchParameters) {
        DatumResponse<Integer> response = new DatumResponse<Integer>();
        try {
            User user = userService.getLoggedInUser(true);
            if (authService.isSuperUser(user)) {
                storySearchParameters.setAccessMode(ACCESS_MODE_ROOT);
            }

            if (TextHelper.isGeoSearchToken(storySearchParameters.getSearchToken())) {
                String searchToken = storySearchParameters.getSearchToken();
                storySearchParameters.setLocalisation(TextHelper.extractLocalisation(searchToken));
                storySearchParameters.setDistance(TextHelper.extractDistance(searchToken));
                storySearchParameters.setSearchToken(null);
            }
            StoryPagedRetrieveParams searchParams = new StoryPagedRetrieveParams(0, 10, StorySortField.TITLE_A_Z, true,
                    storySearchParameters.getSearchToken(), storySearchParameters.getCollectionId(),
                    storySearchParameters.getQuestionnaireId(), storySearchParameters.getAuthorId(),
                    storySearchParameters.getNorthEast(), storySearchParameters.getSouthWest(),
                    storySearchParameters.getLocalisation(), storySearchParameters.getDistance(),
                    storySearchParameters.getAccessMode(), getEffectiveSubject(user));

            int count = storyPersister.countStories(searchParams);

            response.setDatum(count);
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public DataResponse<String> getTags(int storyId) {
        DatumResponse<Story> storyResponse = getStory(storyId);
        if (storyResponse.isError()) {
            return new DataResponse<String>(storyResponse);
        } else {
            DataResponse<String> response = new DataResponse<String>();
            try {
                Set<String> tags = tagsService.getTags(storyResponse.getDatum());
                response.setData(Lists.newArrayList(tags));
            } catch (Exception e) {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
            return response;
        }
    }

    @Override
    public DatumResponse<Document> getBodyDocument(int storyId) {
        DatumResponse<Story> storyResponse = getStory(storyId);
        if (storyResponse.isError()) {
            return new DatumResponse<Document>(storyResponse);
        } else {
            DatumResponse<Document> response = new DatumResponse<Document>();

            try {
                Document document = documentService.getBodyDocument(storyId);

                response.setDatum(document);
            } catch (Exception e) {
                response.setDatum(null);
            }
            return response;
        }
    }

    @Override
    public DatumResponse<Document> getOriginalBodyDocument(int storyId) {
        DatumResponse<Story> storyResponse = getStory(storyId);
        if (storyResponse.isError()) {
            return new DatumResponse<Document>(storyResponse);
        } else {
            DatumResponse<Document> response = new DatumResponse<Document>();
            try {
                Document document = documentService.getOriginalBodyDocument(storyId);

                response.setDatum(document);
            } catch (Exception e) {
                response.setDatum(null);
            }
            return response;
        }
    }

    @Override
    public DatumResponse<Story> updateStory(Story story) {
        DatumResponse<Story> response = new DatumResponse<Story>();
        try {
            response.setDatum(storyService.updateStory(story));
        } catch (BadRequestException e) {
            response.addGlobalErrorMessage("Unexpected error cannot save the story.");
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        } catch (Exception e) {
            response.addGlobalErrorMessage("Unexpected error: " + e.getMessage());
            throw new GeneralException("StoryServiceImpl.updateStory()", e);
        }

        return response;
    }

    @Override
    public DatumResponse<StorySummary> getStorySummary(int id) {
    	return getStorySummary(id, false);
    }
    
    @Override
    public DatumResponse<StorySummary> getStorySummary(int id, boolean includeText) {
        DatumResponse<StorySummary> response = new DatumResponse<StorySummary>();

        try {
            StorySummary storySummary = storyService.getStorySummary(id, includeText);
            response.setDatum(storySummary);
        } catch (Exception e) {
            response.addGlobalErrorMessage(e.getMessage());
        }

        return response;
    }
}
