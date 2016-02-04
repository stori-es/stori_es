package org.consumersunion.stories.dashboard.client.application.collections.widget;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CollectionsWidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(CollectionItemPresenter.MyView.class).to(CollectionItemView.class);

        install(new GinFactoryModuleBuilder().build(CollectionItemPresenterFactory.class));
        install(new GinFactoryModuleBuilder().build(NewCollectionPresenterFactory.class));
    }
}
