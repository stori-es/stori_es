package org.consumersunion.stories.common.client.ui.stories;

import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;

import com.google.gwt.view.client.RowCountChangeEvent;

public interface StoriesListHandler extends RowCountChangeEvent.Handler {
    StorySearchParameters getStorySearchParameters(int start, int length);
}
