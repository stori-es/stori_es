package org.consumersunion.stories.common.client.service;

import java.util.Date;

import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.common.shared.model.metrics.ItemCountByDate;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcMetricsServiceAsync {
    void getStoryCountByCreationDate(Date start, Date end, AsyncCallback<DataResponse<ItemCountByDate>> callback);

    void getUpdatesOptInCountByCreationDate(Date start, Date end,
            AsyncCallback<DataResponse<ItemCountByDate>> callback);

    void getStoriesCountByDayAndTime(int collectionId, String timezone,
            AsyncCallback<DatumResponse<StoriesCountByDayAndTime>> callback);

    void getStoriesCountByDate(int collectionId, String timezone, AsyncCallback<DatumResponse<StoriesCountByDate>>
            callback);

    void getStoriesCountByState(int collectionDataId, AsyncCallback<DatumResponse<StoriesCountByState>> callback);
}
