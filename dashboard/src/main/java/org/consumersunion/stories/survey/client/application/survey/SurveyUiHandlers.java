package org.consumersunion.stories.survey.client.application.survey;

import java.util.List;

import org.consumersunion.stories.common.shared.model.questionnaire.Answer;

import com.gwtplatform.mvp.client.UiHandlers;

public interface SurveyUiHandlers extends UiHandlers {
    void save(List<Answer> answers);
}
