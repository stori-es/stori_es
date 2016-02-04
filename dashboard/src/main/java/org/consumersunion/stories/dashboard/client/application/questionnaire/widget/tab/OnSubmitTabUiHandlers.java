package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import java.util.Set;

import com.gwtplatform.mvp.client.UiHandlers;

public interface OnSubmitTabUiHandlers extends UiHandlers {
    void saveQuestionnaireTags(Set<String> tags);
}
