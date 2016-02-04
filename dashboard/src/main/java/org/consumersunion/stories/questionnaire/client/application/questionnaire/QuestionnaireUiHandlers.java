package org.consumersunion.stories.questionnaire.client.application.questionnaire;

import java.util.List;

import org.consumersunion.stories.common.shared.model.questionnaire.Answer;

import com.gwtplatform.mvp.client.UiHandlers;

public interface QuestionnaireUiHandlers extends UiHandlers {
    void save(List<Answer> answers);
}
