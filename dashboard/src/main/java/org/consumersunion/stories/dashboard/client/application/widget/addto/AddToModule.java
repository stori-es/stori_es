package org.consumersunion.stories.dashboard.client.application.widget.addto;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AddToModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(AddStoriesToCollectionsPresenter.MyView.class).to(AddStoriesToCollectionsView.class);
        bind(AddTagsToStoriesPresenter.MyView.class).to(AddTagsToStoriesView.class);
        bind(AddNoteToStoriesPresenter.MyView.class).to(AddNoteToStoriesView.class);
        bind(AddToWidgetPresenter.MyView.class).to(AddToWidgetPresenterView.class);

        install(new GinFactoryModuleBuilder().build(AddToPresenterFactory.class));
        install(new GinFactoryModuleBuilder().build(AddToWidgetPresenterFactory.class));
    }
}
