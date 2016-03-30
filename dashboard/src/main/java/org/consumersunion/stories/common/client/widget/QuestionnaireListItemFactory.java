package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;

import com.google.inject.assistedinject.Assisted;

public interface QuestionnaireListItemFactory {
    QuestionnaireListItem create(
            QuestionnaireI15d questionnaire,
            ListItemHandler<QuestionnaireI15d> handler,
            boolean canRemove);

    QuestionnaireListItem create(
            QuestionnaireI15d questionnaire,
            ListItemHandler<QuestionnaireI15d> handler,
            @Assisted("canRemove") boolean canRemove,
            @Assisted("withClickAction") boolean withClickAction);
}
