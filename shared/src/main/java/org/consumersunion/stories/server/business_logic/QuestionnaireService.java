package org.consumersunion.stories.server.business_logic;

import java.util.List;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;

public interface QuestionnaireService {
    Questionnaire getQuestionnaire(int id);

    Questionnaire getQuestionnaire(String permalink);

    void deleteQuestionnaire(int id);

    /**
     * Saves the {@link Questionnaire}. The associated {@link Collection} must be provided as well. This requires the
     * effective role own or have explicit write privileges over the Collection.
     */
    QuestionnaireI15d createQuestionnaire(QuestionnaireI15d questionnaire);

    List<QuestionnaireI15d> searchByCollectionsPaged(QuestionnaireI15dPersister.SearchByCollectionPagedParams params);

    QuestionnaireI15d updateQuestionnaire(QuestionnaireI15d questionnaire);

    QuestionnaireI15d getQuestionnaireI15d(int id);
}
