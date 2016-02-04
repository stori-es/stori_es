package org.consumersunion.stories.dashboard.client.application.questionnaire;

import org.consumersunion.stories.dashboard.client.application.questionnaire.ui.QuestionnaireUiModule;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.QuestionnaireWidgetModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class QuestionnaireModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new QuestionnaireUiModule());
        install(new QuestionnaireWidgetModule());
    }
}
