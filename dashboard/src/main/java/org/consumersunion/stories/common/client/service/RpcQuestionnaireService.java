package org.consumersunion.stories.common.client.service;

import org.consumersunion.stories.common.client.service.response.AnswerSetAndQuestionnaireResponse;
import org.consumersunion.stories.common.client.service.response.CollectionSurveyI15dResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.server.business_logic.QuestionnaireService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/questionnaire")
public interface RpcQuestionnaireService extends RemoteService {
    /**
     * @see QuestionnaireService#getQuestionnaire(String)
     */
    QuestionnaireSurveyResponse getQuestionnaireSurvey(String permalink);

    /**
     * Retrieves the fully i18ned {@link Questionnaire}. Requires user has read
     * access to the Collection.
     */
    DatumResponse<QuestionnaireI15d> getQuestionnaireI15d(int id);

    /**
     * @see QuestionnaireService#createQuestionnaire(QuestionnaireI15d)
     */
    CollectionSurveyI15dResponse saveQuestionnaire(Collection collection, QuestionnaireI15d questionnaire);

    /**
     * @see QuestionnaireService#createQuestionnaire(QuestionnaireI15d)
     */
    CollectionSurveyI15dResponse saveQuestionnaire(QuestionnaireI15d newQuestionnaire);

    /**
     * Gets list of questionnaires data. User must have read access to the
     * associated collection.
     */
    PagedDataResponse<QuestionnaireI15d> getQuestionnaireSummaries(final int collectionId, final int start,
            final int length);

    /**
     * Retrieves a specific answer set and related questionnaire data. User must
     * have WRITE access to the associated questionnaire. The write access
     * stands in for sensitive read access.
     */
    AnswerSetAndQuestionnaireResponse getAnswerSetAndQuestionnaire(final int answerSetId);

    PagedDataResponse<QuestionnaireI15d> getQuestionnairesExcludeAssociated(int collectionId, int start,
            int length);

    CollectionSurveyI15dResponse copyQuestionnaire(int sourceId, String newTitle, String summary, Locale locale);
}
