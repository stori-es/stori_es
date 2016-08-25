package org.consumersunion.stories.server.api;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.consumersunion.stories.common.shared.dto.Metadata;
import org.consumersunion.stories.common.shared.dto.ProfileResponse;
import org.consumersunion.stories.common.shared.dto.ProfilesApiResponse;
import org.consumersunion.stories.common.shared.dto.ResponseStatus;
import org.consumersunion.stories.common.shared.dto.post.ProfilePost;
import org.consumersunion.stories.common.shared.dto.post.ProfilePut;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelperImpl;

import com.jayway.restassured.response.Response;

import junit.framework.TestCase;

import static javax.ws.rs.core.Response.Status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.consumersunion.stories.common.shared.api.EndPoints.ID;
import static org.consumersunion.stories.common.shared.api.EndPoints.PROFILES;
import static org.consumersunion.stories.common.shared.dto.ContactType.EMAIL;

public class ProfilesApiTest extends ApiTestCase {
    private static final String A_NEW_GIVEN_NAME = "A_NEW_GIVEN_NAME";
    private static final String A_NEW_SURNAME = "A_NEW_SURNAME";
    private static final Integer POST_ORG_ID = 27;
    private static final Integer POST_USER_ID = 1;
    private static final String POST_GIVEN_NAME = "Testity";
    private static final String POST_SURNAME = "McTester";
    private static final String POST_USER_EMAIL = "testity@test.com";

    public void test_deleteProfileWithData_returnsBadRequest() {
        delete(withUser1Login(), 1001, Status.BAD_REQUEST, EndPoints.PROFILES);
    }

    public void test_deleteProfileWithoutData_notAdmin_returnsUnauthorized() {
        delete(withUser53Login(), 10000, Status.UNAUTHORIZED, EndPoints.PROFILES);
    }

    public void test_deleteProfileWithoutData_returnsNoContent() {
        delete(withUser1Login(), 10001, Status.NO_CONTENT, EndPoints.PROFILES);
    }

    public void test_getUnexistingProfile_returnsNotFound() {
        get(withUser1Login(), 123456789, Status.NOT_FOUND, ErrorApiResponse.class, PROFILES);
    }

    public void test_getUnauthorizedProfile_returnsUnauthorized() {
        get(withUser53Login(), 1038, Status.UNAUTHORIZED, ErrorApiResponse.class, PROFILES);
    }

    public void test_getOwnerProfile_returnsProfile() {
        Response response = withUser1Login()
                .pathParam(UrlParameters.ID, 1001)
                .get(PROFILES + ID);

        ProfilesApiResponse apiResponse = response.as(ProfilesApiResponse.class);
        Metadata metadata = apiResponse.getMetadata();

        TestCase.assertEquals(Status.OK.getStatusCode(), response.getStatusCode());
        assertEquals(Status.OK.getStatusCode(), metadata.getHttpCode());
        assertEquals(ResponseStatus.SUCCESS, metadata.getStatus());
        assertFalse(apiResponse.getData().isEmpty());

        ProfileResponse profileResponse = apiResponse.getData().get(0);

        TestCase.assertEquals(1001, (int) profileResponse.getId());
    }

    public void test_postProfile() {
        ProfilesApiResponse postResponse =
                postWithProfile1001(Status.OK, createPost(POST_USER_ID), ProfilesApiResponse.class, PROFILES);

        ProfilesApiResponse getResponse = get(withUser1Login(), postResponse.getData().get(0).getId(), Status.OK,
                ProfilesApiResponse.class, EndPoints.PROFILES);
        verifyProfilePost(getResponse);
    }

    public void test_putProfile() {
        ProfilesApiResponse postResponse =
                post(withRootLogin(), Status.OK, createPost(null), ProfilesApiResponse.class, PROFILES);

        ProfilesApiResponse putResponse = put(withRootLogin(), postResponse.getData().get(0).getId(), Status.OK,
                createPut(), ProfilesApiResponse.class, EndPoints.PROFILES);

        ProfileResponse profileResponse = putResponse.getData().get(0);
        assertThat(profileResponse.getGivenName()).isEqualTo(A_NEW_GIVEN_NAME);
        assertThat(profileResponse.getSurname()).isEqualTo(A_NEW_SURNAME);
    }

    private ProfilePut createPut() {
        return ProfilePut.builder()
                .withGivenName(A_NEW_GIVEN_NAME)
                .withSurname(A_NEW_SURNAME)
                .build();
    }

    private ProfilePost createPost(Integer userId) {
        List<ApiContact> contacts = new ArrayList<ApiContact>();
        ApiContact contact = new ApiContact();
        contact.setContactType(EMAIL);
        contact.setValue(POST_USER_EMAIL);
        contact.setTitle(Contact.TYPE_HOME);
        contacts.add(contact);

        return ProfilePost.builder()
                .withOrganizationId(POST_ORG_ID)
                .withUserId(userId)
                .withGivenName(POST_GIVEN_NAME)
                .withSurname(POST_SURNAME)
                .withContacts(contacts)
                .build();
    }

    private ProfileResponse verifyProfilePost(ProfilesApiResponse profilesResponse) {
        ProfileResponse profileResponse = profilesResponse.getData().get(0);

        assertEquals(POST_ORG_ID,
                ResourceLinksHelperImpl.extractId(profileResponse.getLinks().getOrganization().getHref()));
        assertEquals(POST_USER_ID, ResourceLinksHelperImpl.extractId(profileResponse.getLinks().getUser().getHref()));
        assertEquals(POST_GIVEN_NAME, profileResponse.getGivenName());
        assertEquals(POST_SURNAME, profileResponse.getSurname());

        List<ApiContact> contacts = profileResponse.getContacts();
        ApiContact contact = contacts.get(0);
        assertEquals(EMAIL, contact.getContactType());
        assertEquals(POST_USER_EMAIL, contact.getValue());

        return profileResponse;
    }
}
