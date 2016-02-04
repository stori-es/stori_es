package org.consumersunion.stories.server.business_logic;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.server.persistence.MetricsPersister;
import org.springframework.stereotype.Service;

@Service
public class MetricsServiceImpl implements MetricsService {
    private final MetricsPersister metricsPersister;

    @Inject
    MetricsServiceImpl(MetricsPersister metricsPersister) {
        this.metricsPersister = metricsPersister;
    }

    @Override
    public StoriesCountByDayAndTime getStoriesCountByDayAndTime(int collectionId, String timezone) {
        return metricsPersister.getStoryCountByDayAndTime(collectionId, timezone);
    }

    @Override
    public StoriesCountByDate getStoriesCountByDate(int collectionId, String timezone) {
        return metricsPersister.getStoriesCountByDate(collectionId, timezone);
    }

    @Override
    public StoriesCountByState getStoriesCountByState(int collectionId) {
        return metricsPersister.getStoriesCountByState(collectionId);
    }
}
