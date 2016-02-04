package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import javax.inject.Singleton;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class QuestionnaireWidgetTabModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindSingletonPresenterWidget(ContentTabPresenter.class, ContentTabPresenter.MyView.class, ContentTabView.class);
        bindSingletonPresenterWidget(OnSubmitTabPresenter.class, OnSubmitTabPresenter.MyView.class,
                OnSubmitTabView.class);
        bindSingletonPresenterWidget(PublicationTabPresenter.class, PublicationTabPresenter.MyView.class,
                PublicationTabView.class);
        bind(PermalinkUtil.class).to(PermalinkUtilImpl.class).in(Singleton.class);
    }
}
