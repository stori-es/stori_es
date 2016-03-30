package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.web.bindery.event.shared.EventBus;

public class QuestionnaireListItem extends AbstractListItem<QuestionnaireI15d> {
    @AssistedInject
    QuestionnaireListItem(
            EventBus eventBus,
            @Assisted QuestionnaireI15d questionnaire,
            @Assisted ListItemHandler<QuestionnaireI15d> handler,
            @Assisted boolean canRemove) {
        this(eventBus, questionnaire, handler, canRemove, true);
    }

    @AssistedInject
    QuestionnaireListItem(
            EventBus eventBus,
            @Assisted QuestionnaireI15d questionnaire,
            @Assisted ListItemHandler<QuestionnaireI15d> handler,
            @Assisted("canRemove") boolean canRemove,
            @Assisted("withClickAction") boolean withClickAction) {
        super(eventBus, questionnaire, handler, canRemove, withClickAction);
    }

    @Override
    protected ContentKind getContentKind(QuestionnaireI15d content) {
        return ContentKind.QUESTIONNAIRE;
    }
}
