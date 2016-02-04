package org.consumersunion.stories.common.client.ui.questionnaire;

import org.consumersunion.stories.common.client.ui.questionnaire.ui.UiModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class QuestionnaireModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new UiModule());
    }
}
