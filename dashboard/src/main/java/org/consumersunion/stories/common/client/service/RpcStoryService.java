package org.consumersunion.stories.common.client.service;

import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryPosition;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.business_logic.StoryService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/story")
public interface RpcStoryService extends RemoteService {
    /**
     * Retrieves the most recent public stories associated to a collection. This
     * is primarily for showing recent stories in a public site.
     */
    DataResponse<StorySummary> getRecentStories(final int collectionId, final int storyCount);

    /**
     * Retrieves stories according to the param.ters. Will only return public,
     * owned, or stories to which the effective has READ privileges according to
     * the indicated access mode.
     */
    PagedDataResponse<StorySummary> getStories(StorySearchParameters storySearchParameters);

    /**
     * Retrieves stories according to the param.ters. Will only return public,
     * owned, or stories to which the effective has READ privileges according to
     * the indicated access mode.
     */
    DataResponse<StoryPosition> getStoriesPositionByCollection(final Integer collectionId, final String searchText,
            final int relation);

    /**
     * Retrieves total count stories attached to a collection, and contain a part
     * of the given searchToken
     */
    DatumResponse<Integer> getStoriesCount(StorySearchParameters storySearchParameters);

    /**
     * @see StoryService#getStory(int)
     */
    DatumResponse<Story> getStory(final int id);

    /**
     * Effective role must have read access to the story.
     */
    DataResponse<String> getTags(final int storyId);

    /**
     * Effective role must have read access to the story.
     */
    DatumResponse<Document> getBodyDocument(final int storyId);

    /**
     * Effective role must have read access to the story. Load initial story
     * content text
     */
    DatumResponse<Document> getOriginalBodyDocument(final int storyId);

    /**
     * @see StoryService#updateStory(Story)
     */
    DatumResponse<Story> updateStory(Story story);

    /**
     * @see StoryService#getStorySummary(int)
     */
    DatumResponse<StorySummary> getStorySummary(int id);
    
    /**
     * @see StoryService#getStorySummary(int, boolean)
     */
    DatumResponse<StorySummary> getStorySummary(int id, boolean includeText);
}
