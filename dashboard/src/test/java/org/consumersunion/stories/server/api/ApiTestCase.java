package org.consumersunion.stories.server.api;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.ApiResponse;
import org.consumersunion.stories.common.shared.dto.Metadata;
import org.consumersunion.stories.common.shared.dto.ResponseStatus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.mapping.Jackson2Mapper;
import com.jayway.restassured.mapper.factory.DefaultJackson2ObjectMapperFactory;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;

import junit.framework.TestCase;

import static com.jayway.restassured.RestAssured.given;

public abstract class ApiTestCase extends TestCase {
    protected static final String USER_1_TEST_API_KEY = "abcdef0123456789";
    protected static final String USER_53_TEST_API_KEY = "abcdefabcdef";
    protected static final String ROOT_API_KEY = "12345678901234567890";
    protected static final int PRIVATE_ORGANIZATION_ID = 2;
    protected static final int WRONG_ORGANIZATION_ID = 99999;

    static {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestContentType(ContentType.JSON);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;

        Jackson2Mapper objectMapper = new Jackson2Mapper(new DefaultJackson2ObjectMapperFactory() {
            @Override
            public ObjectMapper create(Class cls, String charset) {
                ObjectMapper objectMapper = super.create(cls, charset);

                return objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            }
        });
        RestAssured.objectMapper(objectMapper);
    }

    protected RequestSpecification withoutLogin() {
        return given()
                .queryParam(UrlParameters.ORGANIZATION_CONTEXT, PRIVATE_ORGANIZATION_ID);
    }

    protected RequestSpecification withUser1Login() {
        return given()
                .header(HttpHeaders.AUTHORIZATION, SecurityContext.BASIC_AUTH + " " + USER_1_TEST_API_KEY)
                .queryParam(UrlParameters.ORGANIZATION_CONTEXT, PRIVATE_ORGANIZATION_ID);
    }

    protected RequestSpecification withUser53Login() {
        return given()
                .header(HttpHeaders.AUTHORIZATION, SecurityContext.BASIC_AUTH + " " + USER_53_TEST_API_KEY)
                .queryParam(UrlParameters.ORGANIZATION_CONTEXT, PRIVATE_ORGANIZATION_ID);
    }

    protected RequestSpecification withRootLogin() {
        return given()
                .header(HttpHeaders.AUTHORIZATION, SecurityContext.BASIC_AUTH + " " + ROOT_API_KEY)
                .queryParam(UrlParameters.ORGANIZATION_CONTEXT, PRIVATE_ORGANIZATION_ID);
    }

    protected RequestSpecification withWrongOrgContext() {
        return given()
                .header(HttpHeaders.AUTHORIZATION, SecurityContext.BASIC_AUTH + " " + USER_1_TEST_API_KEY)
                .queryParam(UrlParameters.ORGANIZATION_CONTEXT, WRONG_ORGANIZATION_ID);
    }

    protected <T extends ApiResponse> T get(RequestSpecification request,
            int id,
            Status expectedStatusCode,
            Class<T> responseBodyClass,
            String endpoint) {
        Response response = requestWithIdPathParam(request, id, expectedStatusCode)
                .get(endpoint + EndPoints.ID);

        return get(response, expectedStatusCode, responseBodyClass);
    }

    protected <T extends ApiResponse> T get(RequestSpecification request,
            String id,
            Status expectedStatusCode,
            Class<T> responseBodyClass,
            String endpoint) {
        // Note, during the tests, the server side logs are going to the cargo logs, to wit, something like:
        // /tmp/cargo/installs/apache-tomcat-7.0.55/apache-tomcat-7.0.55/output.txt
        Response response = basicRequest(request, expectedStatusCode)
                .get(endpoint + id);

        return get(response, expectedStatusCode, responseBodyClass);
    }

    protected <T extends ApiResponse<?>, B> T post(
            RequestSpecification request,
            Status expectedStatusCode,
            B body,
            Class<T> responseBodyClass,
            String endpoint) {
        Response response = basicRequest(request.body(body), expectedStatusCode)
                .post(endpoint);

        return validateResponse(expectedStatusCode, responseBodyClass, response);
    }

    protected <T extends ApiResponse<?>, B> T put(
            RequestSpecification request,
            int id,
            Status expectedStatusCode,
            B body,
            Class<T> responseBodyClass,
            String endpoint) {
        Response response = requestWithIdPathParam(request.body(body), id, expectedStatusCode)
                .put(endpoint + EndPoints.ID);

        return validateResponse(expectedStatusCode, responseBodyClass, response);
    }

    protected Response delete(
            RequestSpecification request,
            int id,
            Status expectedStatusCode,
            String endpoint) {
        Response response = requestWithIdPathParam(request, id, expectedStatusCode)
                .delete(endpoint + EndPoints.ID);

        validateResponse(expectedStatusCode, null, response);

        return response;
    }

    protected <T extends ApiResponse<?>> T delete(
            RequestSpecification request,
            int id,
            Status expectedStatusCode,
            String endpoint,
            Class<T> responseBodyClass) {
        Response response = requestWithIdPathParam(request, id, expectedStatusCode)
                .delete(endpoint + EndPoints.ID);

        return validateResponse(expectedStatusCode, responseBodyClass, response);
    }

    protected <T extends ApiResponse<?>, B> T putWithProfile1001(
            int id,
            Status expectedStatusCode,
            B body,
            Class<T> responseBodyClass,
            String endpoint) {
        return put(withUser1Login(), id, expectedStatusCode, body, responseBodyClass, endpoint);
    }

    protected <T extends ApiResponse<?>, B> T postWithProfile1001(
            Status expectedStatusCode,
            B body,
            Class<T> responseBodyClass,
            String endpoint) {
        return post(withUser1Login(), expectedStatusCode, body, responseBodyClass, endpoint);
    }

    protected ResponseSpecification basicRequest(RequestSpecification request, Status expectedStatusCode) {
        return request
                .log().all()
                .expect()
                .statusCode(expectedStatusCode.getStatusCode())
                .log().all();
    }

    protected ResponseSpecification requestWithIdPathParam(
            RequestSpecification request,
            int id,
            Status expectedStatusCode) {
        return basicRequest(request.pathParam(UrlParameters.ID, id), expectedStatusCode);
    }

    private <T extends ApiResponse<?>> T get(Response response, Status expectedStatusCode, Class<T> responseBodyClass) {
        return validateResponse(expectedStatusCode, responseBodyClass, response);
    }

    private <T extends ApiResponse<?>> T validateResponse(
            Status expectedStatusCode,
            Class<T> responseBodyClass,
            Response response) {
        T responseBody = null;
        if (responseBodyClass != null) {
            responseBody = response.as(responseBodyClass);
            Metadata metadata = responseBody.getMetadata();

            assertEquals(expectedStatusCode.getStatusCode(), metadata.getHttpCode());
            assertEquals(ResponseStatus.fromCode(expectedStatusCode.getStatusCode()), metadata.getStatus());
        }

        assertEquals(expectedStatusCode.getStatusCode(), response.getStatusCode());

        return responseBody;
    }
}
