package org.consumersunion.stories.dashboard.client.application.widget.addto;

import org.consumersunion.stories.common.client.service.datatransferobject.AssignableStory;

import com.google.common.base.Function;

public class TransformAssignableStory implements Function<AssignableStory, Integer> {
    @Override
    public Integer apply(AssignableStory assignableStory) {
        return assignableStory.getStoryId();
    }
}
