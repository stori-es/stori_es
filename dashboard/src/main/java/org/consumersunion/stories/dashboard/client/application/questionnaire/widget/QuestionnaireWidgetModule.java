package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab.QuestionnaireWidgetTabModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class QuestionnaireWidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new QuestionnaireWidgetTabModule());

        bindPresenterWidget(AnswerSetPresenter.class, AnswerSetPresenter.MyView.class, AnswerSetView.class);
        bindPresenterWidget(ListQuestionnairePresenter.class, ListQuestionnairePresenter.MyView.class,
                ListQuestionnaireView.class);

        bindSingletonPresenterWidget(BuilderPresenter.class, BuilderPresenter.MyView.class, BuilderView.class);
    }
}
