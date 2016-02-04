package org.consumersunion.stories.dashboard.client.application.stories.widget;

import org.consumersunion.stories.common.client.ui.stories.ListStoriesPresenter;
import org.consumersunion.stories.common.client.ui.stories.ListStoriesPresenterFactory;
import org.consumersunion.stories.common.client.ui.stories.StoryItemFactory;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class StoriesWidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(StoryItemPresenter.MyView.class).to(StoryItemView.class);

        install(new GinFactoryModuleBuilder()
                .implement(org.consumersunion.stories.common.client.ui.stories.StoryCard.class,
                        StoryItemPresenter.class)
                .build(Key.get(StoryItemFactory.class, StoryCard.class)));
    }

    @Provides
    @StoryCard
    ListStoriesPresenter getListStories(
            ListStoriesPresenterFactory factory,
            @StoryCard StoryItemFactory storyItemFactory) {
        return factory.create(true, storyItemFactory);
    }
}
