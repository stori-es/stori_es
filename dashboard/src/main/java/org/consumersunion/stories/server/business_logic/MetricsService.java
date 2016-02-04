package org.consumersunion.stories.server.business_logic;

import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;

public interface MetricsService {
    StoriesCountByDayAndTime getStoriesCountByDayAndTime(int collectionId, String timezone);

    StoriesCountByDate getStoriesCountByDate(int collectionId, String timezone);

    StoriesCountByState getStoriesCountByState(int collectionId);
}
