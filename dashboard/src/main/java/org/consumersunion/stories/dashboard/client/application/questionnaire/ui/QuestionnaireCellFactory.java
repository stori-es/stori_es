package org.consumersunion.stories.dashboard.client.application.questionnaire.ui;

import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;

import com.google.gwt.cell.client.ActionCell;
import com.google.inject.assistedinject.Assisted;

public interface QuestionnaireCellFactory {
    QuestionnaireCell create(
            @Assisted("detail") ActionCell.Delegate<QuestionnaireI15d> detailDelegate,
            @Assisted("delete") ActionCell.Delegate<QuestionnaireI15d> deleteDelegate);
}
