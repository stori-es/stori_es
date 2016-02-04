package org.consumersunion.stories.server.api;

import java.util.List;
import java.util.UUID;

import org.consumersunion.stories.Random;
import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.dto.ApiResponse;
import org.consumersunion.stories.common.shared.dto.CollectionsApiResponse;
import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.consumersunion.stories.common.shared.dto.Metadata;
import org.consumersunion.stories.common.shared.dto.QuestionnaireResponse;
import org.consumersunion.stories.common.shared.dto.QuestionnairesApiResponse;
import org.consumersunion.stories.common.shared.dto.ResponseStatus;
import org.consumersunion.stories.common.shared.dto.post.QuestionnairePost;
import org.consumersunion.stories.common.shared.dto.post.QuestionnairePut;

import com.jayway.restassured.specification.RequestSpecification;

import junit.framework.TestCase;

import static com.jayway.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

public class QuestionnairesApiTest extends ApiTestCase {
    private static final int A_PRIVATE_DRAFT_QUESTIONNAIRE_ID = 960;
    private static final int A_PRIVATE_PUBLISHED_QUESTIONNAIRE_ID = 962;
    private static final int A_PUBLIC_PUBLISHED_QUESTIONNAIRE_ID = 961;
    private static final int A_PUBLIC_DRAFT_QUESTIONNAIRE_ID = 963;
    private static final String UPDATED_TITLE = "An updated tile";

    public void testNoAuth_publicPublishedQuestionnaire_returnsOk() {
        getQuestionnaire(given(), A_PUBLIC_PUBLISHED_QUESTIONNAIRE_ID, OK, CollectionsApiResponse.class);
    }

    public void testNoAuth_privatePublishedQuestionnaire_returnsUnauthorized() {
        getQuestionnaire(given(), A_PRIVATE_PUBLISHED_QUESTIONNAIRE_ID, UNAUTHORIZED, ErrorApiResponse.class);
    }

    public void testNoAuth_publicDraftQuestionnaire_returnsUnauthorized() {
        getQuestionnaire(given(), A_PUBLIC_DRAFT_QUESTIONNAIRE_ID, UNAUTHORIZED, ErrorApiResponse.class);
    }

    public void testNoAuth_privateDraftQuestionnaire_returnsUnauthorized() {
        getQuestionnaire(given(), A_PRIVATE_DRAFT_QUESTIONNAIRE_ID, UNAUTHORIZED, ErrorApiResponse.class);
    }

    public void testWithAuthNoGrant_privateDraftQuestionnaire_returnsUnauthorized() {
        getQuestionnaire(withUser53Login(), A_PRIVATE_DRAFT_QUESTIONNAIRE_ID, Status.UNAUTHORIZED,
                ErrorApiResponse.class);
    }

    public void testWithAuthNoGrant_privatePublishedQuestionnaire_returnsUnauthorized() {
        getQuestionnaire(withUser53Login(), A_PRIVATE_PUBLISHED_QUESTIONNAIRE_ID, Status.UNAUTHORIZED,
                ErrorApiResponse.class);
    }

    public void testWithAuthNoGrant_publicDraftQuestionnaire_returnsUnauthorized() {
        getQuestionnaire(withUser53Login(), A_PUBLIC_DRAFT_QUESTIONNAIRE_ID, Status.UNAUTHORIZED,
                ErrorApiResponse.class);
    }

    public void testWithAuthNoGrant_publicPublishedQuestionnaire_returnsOk() {
        getQuestionnaire(withUser53Login(), A_PUBLIC_PUBLISHED_QUESTIONNAIRE_ID, Status.OK,
                CollectionsApiResponse.class);
    }

    public void testWithWrongOrgContext_privateQuestionnaire_returnsUnauthorized() {
        getQuestionnaire(withWrongOrgContext(), A_PRIVATE_DRAFT_QUESTIONNAIRE_ID, UNAUTHORIZED,
                ErrorApiResponse.class);
    }

    public void testWithAuth_privateDraftQuestionnaire_returnsOk() {
        getQuestionnaire(withUser1Login(), A_PRIVATE_DRAFT_QUESTIONNAIRE_ID, Status.OK, CollectionsApiResponse.class);
    }

    public void testWithAuth_privatePublishedQuestionnaire_returnsOk() {
        getQuestionnaire(withUser1Login(), A_PRIVATE_PUBLISHED_QUESTIONNAIRE_ID, Status.OK,
                CollectionsApiResponse.class);
    }

    public void testWithAuth_publicDraftQuestionnaire_returnsOk() {
        getQuestionnaire(withUser1Login(), A_PUBLIC_DRAFT_QUESTIONNAIRE_ID, Status.OK, CollectionsApiResponse.class);
    }

    public void testWithAuth_publicPublishedQuestionnaire_returnsOk() {
        getQuestionnaire(withUser1Login(), A_PUBLIC_PUBLISHED_QUESTIONNAIRE_ID, Status.OK,
                CollectionsApiResponse.class);
    }

    public void test_createQuestionnaire() {
        QuestionnairesApiResponse apiResponse =
                createEmptyQuestionnaire(Status.OK, UUID.randomUUID().toString(), QuestionnairesApiResponse.class);

        Metadata metadata = apiResponse.getMetadata();
        List<QuestionnaireResponse> questionnaires = apiResponse.getData();

        assertEquals(Status.OK.getStatusCode(), metadata.getHttpCode());
        assertEquals(ResponseStatus.SUCCESS, metadata.getStatus());
        TestCase.assertEquals(1, questionnaires.size());

        QuestionnairesApiResponse questionnaire = getQuestionnaire(withUser1Login(), questionnaires.get(0).getId(),
                Status.OK, QuestionnairesApiResponse.class);

        assertTrue(questionnaire.getData().get(0).getLinks().getOwner().getHref().endsWith("2"));
    }

    public void test_updateQuestionnaire() {
        String title = Random.string(10);
        QuestionnairesApiResponse createResponse =
                createEmptyQuestionnaire(Status.OK, title, QuestionnairesApiResponse.class);
        QuestionnaireResponse questionnaireResponse = createResponse.getData().get(0);

        QuestionnairePut questionnairePut = QuestionnairePut.builder().withTitle(UPDATED_TITLE).build();
        QuestionnairesApiResponse updatedQuestionnaireResponse = putWithProfile1001(questionnaireResponse.getId(),
                Status.OK, questionnairePut, QuestionnairesApiResponse.class, EndPoints.QUESTIONNAIRES);
        QuestionnaireResponse updatedQuestionnaire = updatedQuestionnaireResponse.getData().get(0);

        assertEquals(UPDATED_TITLE, updatedQuestionnaire.getTitle());
        assertFalse(updatedQuestionnaire.isPublished());
    }

    public void test_deleteQuestionnaire() {
        QuestionnairesApiResponse apiResponse =
                createEmptyQuestionnaire(Status.OK, UUID.randomUUID().toString(), QuestionnairesApiResponse.class);
        QuestionnaireResponse questionnaireResponse = apiResponse.getData().get(0);
        Integer questionnaireId = questionnaireResponse.getId();

        getQuestionnaire(withUser1Login(), questionnaireId, Status.OK, QuestionnairesApiResponse.class);

        delete(withUser1Login(), questionnaireId, NO_CONTENT, EndPoints.QUESTIONNAIRES);

        getQuestionnaire(withUser1Login(), questionnaireId, Status.NOT_FOUND, ErrorApiResponse.class);
    }

    private <T extends ApiResponse<?>> T createEmptyQuestionnaire(
            Status expectedStatusCode,
            String title,
            Class<T> responseBodyClass) {
        QuestionnairePost questionnairePost = QuestionnairePost.builder()
                .withTitle(title)
                .build();

        return postWithProfile1001(expectedStatusCode, questionnairePost, responseBodyClass, EndPoints.QUESTIONNAIRES);
    }

    private <T extends ApiResponse<?>> T getQuestionnaire(
            RequestSpecification request,
            int id,
            Status expectedStatusCode,
            Class<T> bodyClass) {
        return get(request, id, expectedStatusCode, bodyClass, EndPoints.QUESTIONNAIRES);
    }
}
