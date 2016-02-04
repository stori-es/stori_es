package org.consumersunion.stories.server.business_logic;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.server.persistence.MetricsPersister;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(JukitoRunner.class)
public class MetricsServiceImplTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(MetricsPersister.class);
        }
    }

    @Inject
    private MetricsServiceImpl metricsService;
    @Inject
    private MetricsPersister metricsPersister;

    @Test
    public void getStoriesCountByDayAndTime_delegatesToService() throws Exception {
        int collectionId = 9;
        String timezone = "TZ";
        StoriesCountByDayAndTime expected = mock(StoriesCountByDayAndTime.class);
        given(metricsPersister.getStoryCountByDayAndTime(collectionId, timezone)).willReturn(expected);

        StoriesCountByDayAndTime result = metricsService.getStoriesCountByDayAndTime(collectionId, timezone);

        assertThat(result).isSameAs(expected);
    }

    @Test
    public void getStoriesCountByDate_delegatesToService() throws Exception {
        int collectionId = 9;
        String timezone = "TZ";
        StoriesCountByDate expected = mock(StoriesCountByDate.class);
        given(metricsPersister.getStoriesCountByDate(collectionId, timezone)).willReturn(expected);

        StoriesCountByDate result = metricsService.getStoriesCountByDate(collectionId, timezone);

        assertThat(result).isSameAs(expected);
    }

    @Test
    public void getStoriesCountByState_delegatesToService() throws Exception {
        int collectionId = 9;
        StoriesCountByState expected = mock(StoriesCountByState.class);
        given(metricsPersister.getStoriesCountByState(collectionId)).willReturn(expected);

        StoriesCountByState result = metricsService.getStoriesCountByState(collectionId);

        assertThat(result).isSameAs(expected);
    }
}
