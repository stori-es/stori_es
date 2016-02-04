package org.consumersunion.stories.dashboard.client.application.story.widget;

import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.gwtplatform.mvp.client.UiHandlers;

public interface StoriesByAuthorUiHandlers extends UiHandlers {
    void storyDetails(StorySummary story);

    void loadStories(int start, int length);
}
