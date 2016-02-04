package org.consumersunion.stories.survey.client.application.stories;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class StoriesModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(StoriesPresenter.class, StoriesPresenter.MyView.class, StoriesView.class,
                StoriesPresenter.MyProxy.class);
    }
}
