package org.consumersunion.stories.dashboard.client.application.collection.popup;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CollectionPopupModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(CollectionSelectPresenter.MyView.class).to(CollectionSelectView.class);
    }
}
