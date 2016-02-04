package org.consumersunion.stories.dashboard.client.application.widget.search;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SearchModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(SearchPresenter.MyView.class).to(SearchView.class);

        bind(CollectionSearchProvider.class).in(Singleton.class);
        bind(StorySearchProvider.class).in(Singleton.class);

        install(new GinFactoryModuleBuilder().build(SearchPresenterFactory.class));
    }
}
