package org.consumersunion.stories.dashboard.client.application.widget.addto;

import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuHandler;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * Gin factory to create instances of {@link AddStoriesToCollectionsPresenter} and {@link AddTagsToStoriesPresenter}
 * through
 * AssitedInjection
 */
public interface AddToPresenterFactory extends UiHandlers {
    AddStoriesToCollectionsPresenter createAddStoriesToCollections(AddToMenuHandler addToMenuHandler,
            StoriesListContainer storiesListContainer);

    AddTagsToStoriesPresenter createAddTagsToStories(AddToMenuHandler addToMenuHandler,
            StoriesListContainer storiesListContainer);

    AddNoteToStoriesPresenter createAddNoteToStories(AddToMenuHandler addToMenuHandler,
            StoriesListContainer storiesListContainer);
}
