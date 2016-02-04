package org.consumersunion.stories.common.client.service;

import java.util.Date;

import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.common.shared.model.metrics.ItemCountByDate;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/metrics")
public interface RpcMetricsService extends RemoteService {
    /**
     * Method retrieves report on story counts by day for the currently
     * effective OBO organization. Access requires OBO privileges.
     */
    DataResponse<ItemCountByDate> getStoryCountByCreationDate(Date start, Date end);

    /**
     * Method retrieves report on opt-in counts by day for the currently
     * effective OBO organization. Access requires OBO privileges.
     */
    DataResponse<ItemCountByDate> getUpdatesOptInCountByCreationDate(Date start, Date end);

    DatumResponse<StoriesCountByDayAndTime> getStoriesCountByDayAndTime(int collectionId, String timezone);

    DatumResponse<StoriesCountByDate> getStoriesCountByDate(int collectionId, String timezone);

    DatumResponse<StoriesCountByState> getStoriesCountByState(int collectionId);
}
