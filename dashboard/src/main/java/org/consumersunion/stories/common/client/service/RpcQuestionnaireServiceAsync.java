package org.consumersunion.stories.common.client.service;

import org.consumersunion.stories.common.client.service.response.AnswerSetAndQuestionnaireResponse;
import org.consumersunion.stories.common.client.service.response.CollectionSurveyI15dResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcQuestionnaireServiceAsync {
    void getQuestionnaireSurvey(final String permalink, AsyncCallback<QuestionnaireSurveyResponse> callback);

    void getQuestionnaireI15d(int id, AsyncCallback<DatumResponse<QuestionnaireI15d>> callback);

    void saveQuestionnaire(Collection collection, QuestionnaireI15d questionnaire,
            AsyncCallback<CollectionSurveyI15dResponse> callback);

    void saveQuestionnaire(QuestionnaireI15d newQuestionnaire,
            AsyncCallback<CollectionSurveyI15dResponse> responseHandlerLoader);

    void getQuestionnaireSummaries(int collectionId, final int start, final int length,
            AsyncCallback<PagedDataResponse<QuestionnaireI15d>> callback);

    void getAnswerSetAndQuestionnaire(final int answerSetId, AsyncCallback<AnswerSetAndQuestionnaireResponse> callback);

    void getQuestionnairesExcludeAssociated(int collectionId, int start,
            int length, AsyncCallback<PagedDataResponse<QuestionnaireI15d>> callback);

    void copyQuestionnaire(int sourceId, String newTitle, String summary, Locale locale,
            AsyncCallback<CollectionSurveyI15dResponse> callback);
}
