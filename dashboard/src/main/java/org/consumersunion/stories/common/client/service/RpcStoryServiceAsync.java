package org.consumersunion.stories.common.client.service;

import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryPosition;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * {@link RpcStoryService}.
 */
public interface RpcStoryServiceAsync {
    void getRecentStories(final int collectionId, final int storyCount,
            final AsyncCallback<DataResponse<StorySummary>> callback);

    Request getStories(StorySearchParameters storySearchParameters,
            AsyncCallback<PagedDataResponse<StorySummary>> callback);

    void getStoriesCount(StorySearchParameters storySearchParameters, AsyncCallback<DatumResponse<Integer>> callback);

    void getStoriesPositionByCollection(final Integer collectionId, final String searchText, final int relation,
            AsyncCallback<DataResponse<StoryPosition>> callback);

    void getStory(final int id, AsyncCallback<DatumResponse<Story>> callback);

    void getStorySummary(int id, AsyncCallback<DatumResponse<StorySummary>> callback);
    
    void getStorySummary(int id, boolean includeText, AsyncCallback<DatumResponse<StorySummary>> callback);

    void getTags(int storyId, AsyncCallback<DataResponse<String>> callback);

    void getBodyDocument(int storyId, AsyncCallback<DatumResponse<Document>> callback);

    void getOriginalBodyDocument(int storyId, AsyncCallback<DatumResponse<Document>> callback);

    void updateStory(Story story, AsyncCallback<DatumResponse<Story>> callback);
}
