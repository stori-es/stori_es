package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ListQuestionnaireUiHandlers extends UiHandlers {
    void loadQuestionnaire(int start, int length);

    void questionnaireDetails(QuestionnaireI15d questionnaire);

    void removeSourceQuestionnaire(QuestionnaireI15d questionnaire);
}
