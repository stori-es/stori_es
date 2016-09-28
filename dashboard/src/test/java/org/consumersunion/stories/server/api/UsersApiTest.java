package org.consumersunion.stories.server.api;

import java.util.List;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.dto.ApiResponse;
import org.consumersunion.stories.common.shared.dto.ResourceLink;
import org.consumersunion.stories.common.shared.dto.UserResponse;
import org.consumersunion.stories.common.shared.dto.UsersApiResponse;
import org.consumersunion.stories.common.shared.dto.post.UserPost;
import org.consumersunion.stories.common.shared.dto.post.UserPut;
import org.consumersunion.stories.server.util.UniqueIdGenerator;
import org.consumersunion.stories.server.util.UuidUniqueIdGenerator;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.specification.RequestSpecification;

import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;

public class UsersApiTest extends ApiTestCase {
    private static final int UNEXISTING_USER_ID = 9999;
    private static final int USER_53_ID = 53;
    private static final int PRIVATE_USER_ID = 50;
    private static final int DEFAULT_PROFILE_ID = 1090;

    private UniqueIdGenerator idGenerator = new UuidUniqueIdGenerator();

    public void testGetSelf_noAuth_returnsNoAuth() {
        getSelfUser(withoutLogin(), UNAUTHORIZED, UsersApiResponse.class);
    }

    public void testGetSelf_withLogin_ok() {
        UsersApiResponse apiResponse = getSelfUser(withUser53Login(), Status.OK, UsersApiResponse.class);

        UserResponse userResponse = apiResponse.getData().get(0);

        assertEquals(53, (int) userResponse.getId());
        assertEquals("test", userResponse.getHandle());
        assertEquals(2, userResponse.getContacts().size());
        List<ResourceLink> profileLinks = userResponse.getLinks().getProfiles();
        assertEquals(1, profileLinks.size());
        assertEquals("Unexpected Profile link 'href' value.",
                "http://localhost:" + RestAssured.port + EndPoints.PROFILES + "1053",
                profileLinks.get(0).getHref());
    }

    public void test_deleteUser50_returnsNoContent() {
        delete(withRootLogin(), 50, NO_CONTENT, EndPoints.USERS);

        UsersApiResponse response = get(withRootLogin(), 50, OK, UsersApiResponse.class, EndPoints.USERS);

        assertThat(response.getData().get(0).isArchived()).isTrue();
    }

    public void testGetUser53_withLogin_returnsUser53() {
        UsersApiResponse apiResponse = getUser(withUser1Login(), USER_53_ID, Status.OK, UsersApiResponse.class);

        UserResponse userResponse = apiResponse.getData().get(0);

        assertEquals(USER_53_ID, (int) userResponse.getId());
    }

    public void testGetPublicUser_withoutLogin_returnsUser53() {
        UsersApiResponse apiResponse = getUser(withUser1Login(), USER_53_ID, Status.OK, UsersApiResponse.class);

        UserResponse userResponse = apiResponse.getData().get(0);

        assertEquals(USER_53_ID, (int) userResponse.getId());
    }

    public void testGetPrivateUser_withoutLogin_returns401() {
        getUser(withUser1Login(), PRIVATE_USER_ID, UNAUTHORIZED, UsersApiResponse.class);
    }

    public void testGetUnexistingUser_withLogin_returns404() {
        getUser(withUser1Login(), UNEXISTING_USER_ID, Status.NOT_FOUND, UsersApiResponse.class);
    }

    public void testCreateUser_withRootLogin_returnsUser() {
        // given
        String handle = idGenerator.get();

        // when
        UsersApiResponse apiResponse = post(withRootLogin(), CREATED, createUser(handle, DEFAULT_PROFILE_ID),
                UsersApiResponse.class, EndPoints.USERS);

        // then
        UserResponse userResponse = apiResponse.getData().get(0);
        assertThat(userResponse.getHandle()).isEqualTo(handle);
    }

    public void testCreateUser_withUser1_returnsUnauthorized() {
        post(withUser1Login(), UNAUTHORIZED, createUser(idGenerator.get(), DEFAULT_PROFILE_ID), UsersApiResponse.class,
                EndPoints.USERS);
    }

    public void testDeleteUser_withRootUser_returnsNoContent() {
        // given
        UsersApiResponse apiResponse = post(withRootLogin(), CREATED, createUser(idGenerator.get(), DEFAULT_PROFILE_ID),
                UsersApiResponse.class, EndPoints.USERS);
        UserResponse userResponse = apiResponse.getData().get(0);

        // when
        requestWithIdPathParam(withRootLogin(), userResponse.getId(), NO_CONTENT)
                .delete(EndPoints.USERS + EndPoints.ID);

        // then
        UsersApiResponse getApiResponse = getUser(withRootLogin(), userResponse.getId(), OK, UsersApiResponse.class);
        UserResponse getUserResponse = getApiResponse.getData().get(0);
        assertThat(getUserResponse.isArchived()).isTrue();
    }

    public void testUpdateUser_withRootUser_updatesUser() {
        // given
        UsersApiResponse apiResponse = post(withRootLogin(), CREATED, createUser(idGenerator.get(), DEFAULT_PROFILE_ID),
                UsersApiResponse.class, EndPoints.USERS);
        UserResponse userResponse = apiResponse.getData().get(0);
        String newHandle = "mynewhandle";

        // when
        UsersApiResponse putApiResponse = put(withRootLogin(), userResponse.getId(), OK, createUserPut(newHandle),
                UsersApiResponse.class, EndPoints.USERS);

        // then
        UserResponse getUserResponse = putApiResponse.getData().get(0);
        assertThat(getUserResponse.getHandle()).isEqualTo(newHandle);
    }

    private UserPut createUserPut(String newHandle) {
        return UserPut.builder()
                .withHandle(newHandle)
                .build();
    }

    private UserPost createUser(String handle, int defaultProfileId) {
        return UserPost.builder()
                .withHandle(handle)
                .withDefaultProfileId(defaultProfileId)
                .build();
    }

    private <T extends ApiResponse<?>> T getUser(
            RequestSpecification request,
            int id,
            Status expectedStatusCode,
            Class<T> bodyClass) {
        return get(request, id, expectedStatusCode, bodyClass, EndPoints.USERS);
    }

    private <T extends ApiResponse<?>> T getUser(
            RequestSpecification request,
            String id,
            Status expectedStatusCode,
            Class<T> bodyClass) {
        return get(request, id, expectedStatusCode, bodyClass, EndPoints.USERS);
    }

    private <T extends ApiResponse<?>> T getSelfUser(
            RequestSpecification request,
            Status expectedStatusCode,
            Class<T> bodyClass) {
        return getUser(request, "self", expectedStatusCode, bodyClass);
    }
}
