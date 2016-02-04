package org.consumersunion.stories.server.api;

import java.util.List;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.ApiResponse;
import org.consumersunion.stories.common.shared.dto.BasicResponse;
import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.consumersunion.stories.common.shared.dto.Metadata;
import org.consumersunion.stories.common.shared.dto.ResourceLink;
import org.consumersunion.stories.common.shared.dto.ResponseStatus;
import org.consumersunion.stories.common.shared.dto.StoriesApiResponse;
import org.consumersunion.stories.common.shared.dto.StoryResourceLinks;
import org.consumersunion.stories.common.shared.dto.StoryResponse;
import org.consumersunion.stories.common.shared.dto.post.StoryPost;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.specification.RequestSpecification;

import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;

public class StoriesApiTest extends ApiTestCase {
    public void testNoAuth_privateStory_returnsUnauthorized() {
        int privateStory = 8;

        ErrorApiResponse response =
                getStory(RestAssured.given(), privateStory, Status.UNAUTHORIZED, ErrorApiResponse.class);

        testErrorGetStory(response);
    }

    public void testWithAuth_privateStory_returnsOk() {
        getStory(withUser1Login(), 8, Status.OK, StoriesApiResponse.class);
    }

    public void testWithWrongAuth_privateStory_returnsUnauthorized() {
        int privateStory = 8;

        ErrorApiResponse response =
                getStory(withWrongOrgContext(), privateStory, Status.UNAUTHORIZED, ErrorApiResponse.class);

        testErrorGetStory(response);
    }

    public void testNoAuth_publicStory_returnsStory() {
        // When
        StoriesApiResponse response = getStory(RestAssured.given(), 11, Status.OK, StoriesApiResponse.class);

        // Then
        List<StoryResponse> stories = response.getData();
        assertNotNull(stories);
        assertEquals(1, stories.size());

        StoryResponse storyData = stories.get(0);
        assertNotNull(storyData);

        StoryResourceLinks links = storyData.getLinks();
        assertNotNull(links.getContents());

        List<ResourceLink> responseLinks = links.getResponses();
        assertNotNull("'responses' not found.", responseLinks);
        assertEquals("'responses' has no members.", 1, responseLinks.size());
        assertThat(responseLinks.get(0).getHref()).endsWith(EndPoints.DOCUMENTS + "106");
    }

    public void test_updateStory() {
        StoriesApiResponse apiResponse = createEmptyStory(Status.CREATED, StoriesApiResponse.class);
        BasicResponse<?> basicResponse = apiResponse.getData().get(0);
        Integer storyId = basicResponse.getId();

        StoryPost storyPost = StoryPost.builder()
                .build();

        withUser1Login().pathParam(UrlParameters.ID, storyId)
                .body(storyPost)
                .expect()
                .statusCode(Status.OK.getStatusCode())
                .put(EndPoints.STORIES + EndPoints.ID);

        getStory(withUser1Login(), storyId, Status.OK, StoriesApiResponse.class);
    }

    public void test_deleteStory() {
        StoriesApiResponse apiResponse = createEmptyStory(Status.CREATED, StoriesApiResponse.class);
        BasicResponse<?> basicResponse = apiResponse.getData().get(0);
        Integer storyId = basicResponse.getId();

        getStory(withUser1Login(), storyId, Status.OK, StoriesApiResponse.class);

        delete(withUser1Login(), storyId, NO_CONTENT, EndPoints.STORIES);

        getStory(withUser1Login(), storyId, Status.NOT_FOUND, ErrorApiResponse.class);
    }

    public void test_createStory() {
        StoriesApiResponse apiResponse = createEmptyStory(Status.CREATED, StoriesApiResponse.class);

        Metadata metadata = apiResponse.getMetadata();
        List<StoryResponse> stories = apiResponse.getData();

        assertEquals(Status.CREATED.getStatusCode(), metadata.getHttpCode());
        assertEquals(ResponseStatus.SUCCESS, metadata.getStatus());
        assertEquals(1, stories.size());

        getStory(withUser1Login(), stories.get(0).getId(), Status.OK, StoriesApiResponse.class);
    }

    public void test_getUnexistingStory_returnsNotFound() {
        int bogusId = 123456789;

        ErrorApiResponse response =
                getStory(withUser1Login(), bogusId, Status.NOT_FOUND, ErrorApiResponse.class);

        testErrorGetStory(response);
    }

    private void testErrorGetStory(ErrorApiResponse response) {
        assertNull(response.getData());
    }

    private <T extends ApiResponse<?>> T createEmptyStory(Status expectedStatusCode,
            Class<T> responseBodyClass) {
        StoryPost storyPost = StoryPost.builder().build();

        return postWithProfile1001(expectedStatusCode, storyPost, responseBodyClass, EndPoints.STORIES);
    }

    private <T extends ApiResponse<?>> T getStory(RequestSpecification request,
            int id,
            Status expectedStatusCode,
            Class<T> bodyClass) {
        return get(request, id, expectedStatusCode, bodyClass, EndPoints.STORIES);
    }
}
