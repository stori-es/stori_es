package org.consumersunion.stories.questionnaire.client.application;

import org.consumersunion.stories.questionnaire.client.application.questionnaire.QuestionnaireModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new QuestionnaireModule());

        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);
    }
}
