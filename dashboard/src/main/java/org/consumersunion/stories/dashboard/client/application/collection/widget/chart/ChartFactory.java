package org.consumersunion.stories.dashboard.client.application.collection.widget.chart;

import java.util.Date;

import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.google.inject.assistedinject.Assisted;

public interface ChartFactory {
    TimeDistributionPresenter createTimeDistribution(CollectionData data);

    TimeDistributionPresenter createTimeDistribution(StoriesCountByDayAndTime countByDayAndTime);

    TimelinePresenter createTimeline(CollectionData data);

    TimelinePresenter createTimeline(
            @Assisted("lastModified") Date lastModified,
            @Assisted("published") Date published,
            StoriesCountByDate countByDate);

    MapPresenter createMap(CollectionData data);

    MapPresenter createMap(StoriesCountByState countByState);
}
