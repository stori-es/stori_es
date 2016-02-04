package org.consumersunion.stories.server.api;

import javax.ws.rs.core.Response.Status;

import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class QuestionnaireCorsApiTest extends ApiTestCase {
    public void testCors() {
        Response response = given().options("/rest/questionnaire/some-permalink");

        assertThat(response.getStatusCode()).isEqualTo(Status.OK.getStatusCode());
        assertThat(response.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");
        assertThat(response.getHeader("Access-Control-Allow-Methods")).isEqualTo("POST, GET, UPDATE, DELETE, OPTIONS");
        assertThat(response.getHeader("Access-Control-Allow-Headers")).isEqualTo(
                "content-type, x-http-method-override");
    }
}
