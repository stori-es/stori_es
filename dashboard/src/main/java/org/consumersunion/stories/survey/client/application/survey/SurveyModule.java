package org.consumersunion.stories.survey.client.application.survey;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SurveyModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        bind(SurveyUiHandlers.class).to(SurveyPresenter.class);

        bindPresenter(SurveyPresenter.class, SurveyPresenter.MyView.class, SurveyView.class,
                SurveyPresenter.MyProxy.class);
        bindPresenter(EndSurveyPresenter.class, EndSurveyPresenter.MyView.class, EndSurveyView.class,
                EndSurveyPresenter.MyProxy.class);
    }
}
