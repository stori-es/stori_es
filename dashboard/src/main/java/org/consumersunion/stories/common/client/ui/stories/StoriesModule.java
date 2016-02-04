package org.consumersunion.stories.common.client.ui.stories;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Provides;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class StoriesModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(ListStoriesPresenter.MyView.class).to(ListStoriesView.class);
        bind(PublishedStoryCardPresenter.MyView.class).to(PublishedStoryCardView.class);

        install(new GinFactoryModuleBuilder().build(ListStoriesPresenterFactory.class));

        install(new GinFactoryModuleBuilder()
                .implement(StoryCard.class, PublishedStoryCardPresenter.class)
                .build(StoryItemFactory.class));
    }

    @Provides
    ListStoriesPresenter getListStories(
            ListStoriesPresenterFactory factory,
            StoryItemFactory storyItemFactory) {
        return factory.create(false, storyItemFactory);
    }
}
