package org.consumersunion.stories.dashboard.client.application.collections;

import org.consumersunion.stories.dashboard.client.application.collections.widget.CollectionsWidgetModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CollectionsModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new CollectionsWidgetModule());

        bindPresenter(CollectionsPresenter.class, CollectionsPresenter.MyView.class, CollectionsView.class,
                CollectionsPresenter.MyProxy.class);
    }
}
