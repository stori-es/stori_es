package org.consumersunion.stories.dashboard.client.application.collection.widget;

import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;

import com.gwtplatform.mvp.client.UiHandlers;

public interface StoriesWidgetUiHandlers extends UiHandlers {
    StoriesListContainer getStoriesListContainer();

    void switchToList();

    void switchToMap();
}
