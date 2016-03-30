package org.consumersunion.stories.dashboard.client.application.collection;

import org.consumersunion.stories.dashboard.client.application.collection.ui.AttachedCollectionCellFactory;
import org.consumersunion.stories.dashboard.client.application.collection.widget.WidgetModule;
import org.consumersunion.stories.dashboard.client.application.collection.widget.navbar.NavBarModule;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CollectionModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new NavBarModule());
        install(new WidgetModule());

        bindPresenter(CollectionPresenter.class, CollectionPresenter.MyView.class, CollectionView.class,
                CollectionPresenter.MyProxy.class);

        install(new GinFactoryModuleBuilder().build(AttachedCollectionCellFactory.class));

        bind(CollectionObserver.class).in(Singleton.class);
    }
}
