package org.consumersunion.stories.questionnaire.client.application.questionnaire;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class QuestionnaireModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(QuestionnaireUiHandlers.class).to(QuestionnairePresenter.class);

        bindPresenter(QuestionnairePresenter.class, QuestionnairePresenter.MyView.class, QuestionnaireView.class,
                QuestionnairePresenter.MyProxy.class);
        bindPresenter(EndQuestionnairePresenter.class, EndQuestionnairePresenter.MyView.class,
                EndQuestionnaireView.class,
                EndQuestionnairePresenter.MyProxy.class);
    }
}
