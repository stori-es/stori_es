package org.consumersunion.stories.dashboard.client.application.stories;

import org.consumersunion.stories.dashboard.client.application.stories.widget.StoriesWidgetModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class StoriesModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(StoriesPresenter.class, StoriesPresenter.MyView.class, StoriesView.class,
                StoriesPresenter.MyProxy.class);

        install(new StoriesWidgetModule());
    }
}
