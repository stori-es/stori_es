package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbystory;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CollectionsByStoryModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(CollectionsByStoryPresenter.class, CollectionsByStoryPresenter.MyView.class,
                CollectionsByStoryView.class);
    }
}
