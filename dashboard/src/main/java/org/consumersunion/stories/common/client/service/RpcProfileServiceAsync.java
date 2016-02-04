package org.consumersunion.stories.common.client.service;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryAndStorytellerData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcProfileServiceAsync {
    void retrieveProfile(int id, AsyncCallback<DatumResponse<ProfileSummary>> callback);

    void update(Profile entity, AsyncCallback<DatumResponse<ProfileSummary>> callback);

    void getStorytellers(int start, int length, SortField sortField, boolean ascending, String searchText,
            int relation, AsyncCallback<PagedDataResponse<StoryAndStorytellerData>> callback);

    void getProfileSummaries(AsyncCallback<DataResponse<ProfileSummary>> callback);

    void getProfileSummaries(int userId, AsyncCallback<DataResponse<ProfileSummary>> callback);

    void setActiveProfile(int profileId, boolean removeAttribute,
            AsyncCallback<DatumResponse<ProfileSummary>> callback);
}
