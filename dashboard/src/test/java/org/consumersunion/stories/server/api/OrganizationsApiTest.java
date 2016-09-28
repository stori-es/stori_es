package org.consumersunion.stories.server.api;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.consumersunion.stories.common.shared.dto.Metadata;
import org.consumersunion.stories.common.shared.dto.OrganizationResponse;
import org.consumersunion.stories.common.shared.dto.OrganizationsApiResponse;
import org.consumersunion.stories.common.shared.dto.ResponseStatus;
import org.consumersunion.stories.common.shared.dto.post.OrganizationPost;
import org.consumersunion.stories.common.shared.dto.post.OrganizationPut;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import junit.framework.TestCase;

import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import static org.assertj.core.api.Assertions.assertThat;
import static org.consumersunion.stories.common.shared.api.EndPoints.ORGANIZATIONS;
import static org.consumersunion.stories.server.api.rest.util.ResourceLinksHelperImpl.extractId;

public class OrganizationsApiTest extends ApiTestCase {
    private static final int WRONG_ORGANIZATION_ID = 1;
    public static final String ORG_NAME = "Deutschen Filmpreis";
    public static final String ORG_SHORT_NAME = "DF";
    private static final String OTHER_ORG_NAME = "OTHER_ORG_NAME";
    private static final String OTHER_ORG_SHORT_NAME = "SOME_SHORT_NAME";
    private static final String LONG_ORG_SHORT_NAME = "OTHER_ORG_SHORT_NAME";

    public void test_wrongOrganization_returnsNotFound() {
        Response response = RestAssured.given()
                .pathParam(UrlParameters.ID, WRONG_ORGANIZATION_ID)
                .get(ORGANIZATIONS + EndPoints.ID);

        ErrorApiResponse apiResponse = response.as(ErrorApiResponse.class);
        Metadata metadata = apiResponse.getMetadata();

        TestCase.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatusCode());
        assertEquals(Status.NOT_FOUND.getStatusCode(), metadata.getHttpCode());
        assertEquals(ResponseStatus.ERROR, metadata.getStatus());
    }

    public void testNoAuth_privateOrganization_returnsUnauthorized() {
        Response response = RestAssured.given()
                .pathParam(UrlParameters.ID, PRIVATE_ORGANIZATION_ID)
                .get(ORGANIZATIONS + EndPoints.ID);

        ErrorApiResponse apiResponse = response.as(ErrorApiResponse.class);
        Metadata metadata = apiResponse.getMetadata();

        TestCase.assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatusCode());
        assertEquals(UNAUTHORIZED.getStatusCode(), metadata.getHttpCode());
        assertEquals(ResponseStatus.ERROR, metadata.getStatus());
    }

    public void testWrongAuth_privateOrganization_returnsUnauthorized() {
        Response response = withWrongOrgContext()
                .pathParam(UrlParameters.ID, PRIVATE_ORGANIZATION_ID)
                .get(ORGANIZATIONS + EndPoints.ID);

        ErrorApiResponse apiResponse = response.as(ErrorApiResponse.class);
        Metadata metadata = apiResponse.getMetadata();

        TestCase.assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatusCode());
        assertEquals(UNAUTHORIZED.getStatusCode(), metadata.getHttpCode());
        assertEquals(ResponseStatus.ERROR, metadata.getStatus());
    }

    public void testAuth_privateOrganization_returnsOk() {
        Response response = withUser1Login()
                .pathParam(UrlParameters.ID, PRIVATE_ORGANIZATION_ID)
                .get(ORGANIZATIONS + EndPoints.ID);

        assertResponseSuccess(response);
    }

    public void test_postOrganization_createsOrg() {
        Response responsePost = withRootLogin()
                .content(createOrg())
                .post(ORGANIZATIONS);

        OrganizationsApiResponse apiResponsePost = assertResponseSuccess(responsePost);
        OrganizationResponse postOrganizationResponse = apiResponsePost.getData().get(0);

        Response responseGet = withRootLogin()
                .pathParam(UrlParameters.ID, postOrganizationResponse.getId())
                .get(ORGANIZATIONS + EndPoints.ID);

        OrganizationsApiResponse apiResponseGet = assertResponseSuccess(responseGet);
        OrganizationResponse getOrganizationResponse = apiResponseGet.getData().get(0);

        assertEquals(postOrganizationResponse.getId(), getOrganizationResponse.getId());
        assertEquals(postOrganizationResponse.getName(), getOrganizationResponse.getName());
        assertEquals(ORG_NAME, postOrganizationResponse.getName());
        assertEquals(postOrganizationResponse.getShortName(), getOrganizationResponse.getShortName());
        assertEquals(ORG_SHORT_NAME, postOrganizationResponse.getShortName());
    }

    public void test_putOrganization_updatesOrg() {
        // given
        OrganizationsApiResponse post =
                post(withRootLogin(), OK, createOrg(), OrganizationsApiResponse.class, ORGANIZATIONS);
        OrganizationResponse postOrganizationResponse = post.getData().get(0);
        OrganizationPut put = OrganizationPut.builder()
                .setName(OTHER_ORG_NAME)
                .setShortName(OTHER_ORG_SHORT_NAME)
                .build();

        // when
        put(withRootLogin(), postOrganizationResponse.getId(), OK, put, OrganizationsApiResponse.class, ORGANIZATIONS);

        // then
        OrganizationsApiResponse apiResponseGet = get(withRootLogin(), postOrganizationResponse.getId(), OK,
                OrganizationsApiResponse.class, ORGANIZATIONS);
        OrganizationResponse getOrganizationResponse = apiResponseGet.getData().get(0);

        assertThat(getOrganizationResponse.getName()).isEqualTo(OTHER_ORG_NAME);
        assertThat(getOrganizationResponse.getShortName()).isEqualTo(OTHER_ORG_SHORT_NAME);
    }

    public void test_putLongShortName_badRequest() {
        // given
        OrganizationsApiResponse post =
                post(withRootLogin(), OK, createOrg(), OrganizationsApiResponse.class, ORGANIZATIONS);
        OrganizationResponse postOrganizationResponse = post.getData().get(0);
        OrganizationPut put = OrganizationPut.builder()
                .setName(OTHER_ORG_NAME)
                .setShortName(LONG_ORG_SHORT_NAME)
                .build();

        // when
        put(withRootLogin(), postOrganizationResponse.getId(), BAD_REQUEST, put, OrganizationsApiResponse.class,
                ORGANIZATIONS);
    }

    public void testAuth_deleteOrganization_deletesOrg() {
        Response responsePost = withRootLogin()
                .content(createOrg())
                .post(ORGANIZATIONS);

        OrganizationsApiResponse apiResponsePost = assertResponseSuccess(responsePost);
        OrganizationResponse postOrganizationResponse = apiResponsePost.getData().get(0);

        delete(withRootLogin(), postOrganizationResponse.getId(), Status.NO_CONTENT, ORGANIZATIONS);

        Response responseGet = withRootLogin()
                .pathParam(UrlParameters.ID, postOrganizationResponse.getId())
                .get(ORGANIZATIONS + EndPoints.ID);

        Metadata apiResponseGetMetadata = responseGet.as(ErrorApiResponse.class).getMetadata();

        TestCase.assertEquals(Status.NOT_FOUND.getStatusCode(), responseGet.getStatusCode());
        assertEquals(Status.NOT_FOUND.getStatusCode(), apiResponseGetMetadata.getHttpCode());
        assertEquals(ResponseStatus.ERROR, apiResponseGetMetadata.getStatus());
    }

    public void testNoAuth_deleteOrganization_unauthorized() {
        Metadata metadata =
                delete(withUser53Login(), PRIVATE_ORGANIZATION_ID, UNAUTHORIZED, ORGANIZATIONS, ErrorApiResponse.class)
                        .getMetadata();

        assertEquals(UNAUTHORIZED.getStatusCode(), metadata.getHttpCode());
        assertEquals(ResponseStatus.ERROR, metadata.getStatus());
    }

    private OrganizationsApiResponse assertResponseSuccess(Response response) {
        OrganizationsApiResponse apiResponse = response.as(OrganizationsApiResponse.class);
        Metadata metadata = apiResponse.getMetadata();

        TestCase.assertEquals(OK.getStatusCode(), response.getStatusCode());
        assertEquals(OK.getStatusCode(), metadata.getHttpCode());
        assertEquals(ResponseStatus.SUCCESS, metadata.getStatus());

        return apiResponse;
    }

    private OrganizationPost createOrg() {
        return OrganizationPost.builder()
                .setName(ORG_NAME)
                .setShortName(ORG_SHORT_NAME)
                .build();
    }
}
