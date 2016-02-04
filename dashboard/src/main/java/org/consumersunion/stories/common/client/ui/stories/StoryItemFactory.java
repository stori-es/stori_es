package org.consumersunion.stories.common.client.ui.stories;

import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.gwtplatform.mvp.client.PresenterWidget;

public interface StoryItemFactory {
    StoryCard create(PresenterWidget storyPresenter, StorySummary storySummary);
}
