package org.consumersunion.stories.survey.client.application;

import org.consumersunion.stories.survey.client.application.error.ErrorModule;
import org.consumersunion.stories.survey.client.application.stories.StoriesModule;
import org.consumersunion.stories.survey.client.application.survey.SurveyModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new SurveyModule());
        install(new ErrorModule());
        install(new StoriesModule());

        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);
    }
}
