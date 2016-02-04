package org.consumersunion.stories.server.api;

import javax.ws.rs.core.Response.Status;

import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ConnectionApiTest extends ApiTestCase {
    public void testApiFound() {
        Response response = given().get("/api/collections/959");
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isNotEqualTo(Status.NOT_FOUND.getStatusCode());
    }

    public void testRestQuestionnaire() {
        Response response = given().get("/rest/questionnaire/some-perma-link");
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isNotEqualTo(Status.NOT_FOUND.getStatusCode());
    }

    public void testRestCollection() {
        Response response = given().get("/rest/collection/some-perma-link");
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isNotEqualTo(Status.NOT_FOUND.getStatusCode());
    }

    public void testRestStory() {
        Response response = given().get("/rest/story/1");
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isNotEqualTo(Status.NOT_FOUND.getStatusCode());
    }

    public void testApiNotFound() {
        Response response = given().get("/api/questionnaire/some-perma-link");
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isEqualTo(Status.NOT_FOUND.getStatusCode());
    }

    public void testQuestionnaireNotFound() {
        Response response = given().get("/questionnaire/some-perma-link");
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isEqualTo(Status.NOT_FOUND.getStatusCode());
    }

    public void testRoot() {
        Response response = given().get("");
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isNotEqualTo(Status.NOT_FOUND.getStatusCode());
    }

    public void testStoriesJsp() {
        Response response = given().get("/stories.jsp");
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isNotEqualTo(Status.NOT_FOUND.getStatusCode());
    }

    public void testRpcAuthorization() {
        Response response = given().post("/storiesmvp/service/authorization");
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isNotEqualTo(Status.NOT_FOUND.getStatusCode());
    }

    public void testSurveyMVP() {
        Response response = given().post("/surveymvp/service/story");
        int statusCode = response.getStatusCode();
        assertThat(statusCode).isNotEqualTo(Status.NOT_FOUND.getStatusCode());
    }
}
