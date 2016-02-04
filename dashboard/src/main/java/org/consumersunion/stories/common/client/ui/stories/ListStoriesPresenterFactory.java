package org.consumersunion.stories.common.client.ui.stories;

public interface ListStoriesPresenterFactory {
    ListStoriesPresenter create(
            boolean trackUrl,
            StoryItemFactory storyItemFactory);
}
