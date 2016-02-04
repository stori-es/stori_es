package org.consumersunion.stories.server.api.gwt_rpc;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcMetricsService;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.common.shared.model.metrics.ItemCountByDate;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.business_logic.MetricsService;
import org.consumersunion.stories.server.persistence.MetricsPersister;
import org.springframework.stereotype.Service;

import static org.consumersunion.stories.server.persistence.ProfilePersister.RetrieveProfileSummaryFunc;

@Service("metricsService")
public class RpcMetricsServiceImpl extends RpcBaseServiceImpl implements RpcMetricsService {
    @Inject
    MetricsPersister metricsPersister;
    @Inject
    MetricsService metricsService;

    @Override
    public DataResponse<ItemCountByDate> getStoryCountByCreationDate(Date start, Date end) {
        DataResponse<ItemCountByDate> response = new DataResponse<ItemCountByDate>();

        try {
            ProfileSummary profileSummary = persistenceService.process(
                    new RetrieveProfileSummaryFunc(userService.getActiveProfileId()));
            if (profileSummary != null && profileSummary.getProfile() != null) {
                Integer orgId = profileSummary.getProfile().getOrganizationId();
                List<ItemCountByDate> metrics =
                        persistenceService.process(metricsPersister.getStoryCountByCreationDate(orgId));
                response.setData(metrics);
            }
        } catch (Exception e) {
            response.addGlobalErrorMessage(e.getLocalizedMessage());
            throw new GeneralException(e);
        }

        return response;
    }

    @Override
    public DataResponse<ItemCountByDate> getUpdatesOptInCountByCreationDate(Date start, Date end) {
        DataResponse<ItemCountByDate> response = new DataResponse<ItemCountByDate>();

        try {
            ProfileSummary profileSummary =
                    persistenceService.process(new RetrieveProfileSummaryFunc(userService.getActiveProfileId()));
            if (profileSummary != null && profileSummary.getProfile() != null) {
                int orgId = profileSummary.getProfile().getOrganizationId();
                List<ItemCountByDate> metrics = persistenceService.process(
                        metricsPersister.getUpdatesOptInCountByCreationDate(orgId));
                response.setData(metrics);
            }
        } catch (Exception e) {
            response.addGlobalErrorMessage(e.getLocalizedMessage());
            throw new GeneralException(e);
        }

        return response;
    }

    @Override
    public DatumResponse<StoriesCountByDayAndTime> getStoriesCountByDayAndTime(int collectionId, String timezone) {
        DatumResponse<StoriesCountByDayAndTime> response = new DatumResponse<StoriesCountByDayAndTime>();

        response.setDatum(metricsService.getStoriesCountByDayAndTime(collectionId, timezone));

        return response;
    }

    @Override
    public DatumResponse<StoriesCountByDate> getStoriesCountByDate(int collectionId, String timezone) {
        DatumResponse<StoriesCountByDate> response = new DatumResponse<StoriesCountByDate>();

        response.setDatum(metricsService.getStoriesCountByDate(collectionId, timezone));

        return response;
    }

    @Override
    public DatumResponse<StoriesCountByState> getStoriesCountByState(int collectionId) {
        DatumResponse<StoriesCountByState> response = new DatumResponse<StoriesCountByState>();

        response.setDatum(metricsService.getStoriesCountByState(collectionId));

        return response;
    }
}
