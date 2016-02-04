package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbyquestionnaire;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CollectionsByQuestionnaireModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(CollectionsByQuestionnairePresenter.class,
                CollectionsByQuestionnairePresenter.MyView.class, CollectionsByQuestionnaireView.class);
    }
}
