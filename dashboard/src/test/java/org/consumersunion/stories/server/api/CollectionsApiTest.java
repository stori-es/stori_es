package org.consumersunion.stories.server.api;

import java.util.List;
import java.util.Set;

import org.consumersunion.stories.Random;
import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.dto.ApiResponse;
import org.consumersunion.stories.common.shared.dto.CollectionResponse;
import org.consumersunion.stories.common.shared.dto.CollectionsApiResponse;
import org.consumersunion.stories.common.shared.dto.ErrorApiResponse;
import org.consumersunion.stories.common.shared.dto.Metadata;
import org.consumersunion.stories.common.shared.dto.ResponseStatus;
import org.consumersunion.stories.common.shared.dto.post.CollectionPost;
import org.consumersunion.stories.common.shared.dto.post.CollectionPut;

import com.google.common.collect.Sets;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.specification.RequestSpecification;

import static javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

public class CollectionsApiTest extends ApiTestCase {
    private static final int A_PRIVATE_COLLECTION_ID = 958;
    private static final int A_PUBLIC_COLLECTION_ID = 959;
    private static final String UPDATED_TITLE = "Updated title";

    public void testNoAuth_privateCollection_returnsUnauthorized() {
        getCollection(RestAssured.given(), A_PRIVATE_COLLECTION_ID, Status.UNAUTHORIZED, ErrorApiResponse.class);
    }

    public void testWithAuth_privateCollection_returnsOk() {
        getCollection(withUser1Login(), A_PRIVATE_COLLECTION_ID, Status.OK, CollectionsApiResponse.class);
    }

    public void testWithWrongAuth_privateCollection_returnsUnauthorized() {
        getCollection(withWrongOrgContext(), A_PRIVATE_COLLECTION_ID, Status.UNAUTHORIZED, ErrorApiResponse.class);
    }

    public void testNoAuth_publicCollection_returnsCollection() {
        getCollection(RestAssured.given(), A_PUBLIC_COLLECTION_ID, Status.OK, CollectionsApiResponse.class);
    }

    public void test_createCollection() {
        CollectionsApiResponse apiResponse =
                createEmptyCollection(Status.OK, Random.string(10), CollectionsApiResponse.class);

        Metadata metadata = apiResponse.getMetadata();
        List<CollectionResponse> collections = apiResponse.getData();

        assertEquals(Status.OK.getStatusCode(), metadata.getHttpCode());
        assertEquals(ResponseStatus.SUCCESS, metadata.getStatus());
        assertEquals(1, collections.size());

        getCollection(withUser1Login(), collections.get(0).getId(), Status.OK, CollectionsApiResponse.class);
    }

    public void test_createCollection_withTags() {
        CollectionsApiResponse apiResponse = createCollectionWithTags(Status.OK, Random.string(10),
                Sets.newHashSet("tag1", "tag2"), CollectionsApiResponse.class);

        List<CollectionResponse> collections = apiResponse.getData();

        assertEquals(1, collections.size());
        assertEquals(2, collections.get(0).getTags().size());

        getCollection(withUser1Login(), collections.get(0).getId(), Status.OK, CollectionsApiResponse.class);
    }

    public void test_updateCollection() {
        String title = Random.string(10);
        CollectionsApiResponse createResponse = createEmptyCollection(Status.OK, title, CollectionsApiResponse.class);
        CollectionResponse collectionResponse = createResponse.getData().get(0);

        CollectionPut updateCollectionRequest = CollectionPut.builder().withTitle(UPDATED_TITLE).build();
        CollectionsApiResponse updatedCollectionResponse = putCollectionAsProfile1001(collectionResponse.getId(),
                Status.OK, updateCollectionRequest, CollectionsApiResponse.class);
        CollectionResponse updatedCollection = updatedCollectionResponse.getData().get(0);

        assertEquals(UPDATED_TITLE, updatedCollection.getTitle());
        assertFalse(updatedCollection.isPublished());
        assertNull(updatedCollection.getPublishedOn());
    }

    public void test_deleteCollection() {
        CollectionsApiResponse apiResponse =
                createEmptyCollection(Status.OK, Random.string(10), CollectionsApiResponse.class);
        CollectionResponse collectionResponse = apiResponse.getData().get(0);
        Integer collectionId = collectionResponse.getId();

        getCollection(withUser1Login(), collectionId, Status.OK, CollectionsApiResponse.class);

        delete(withUser1Login(), collectionId, NO_CONTENT, EndPoints.COLLECTIONS);

        getCollection(withUser1Login(), collectionId, Status.NOT_FOUND, ErrorApiResponse.class);
    }

    public void testWrongAuth_deleteCollection() {
        CollectionsApiResponse apiResponse =
                createEmptyCollection(Status.OK, Random.string(10), CollectionsApiResponse.class);
        CollectionResponse collectionResponse = apiResponse.getData().get(0);
        Integer collectionId = collectionResponse.getId();

        getCollection(withUser1Login(), collectionId, Status.OK, CollectionsApiResponse.class);

        delete(withWrongOrgContext(), collectionId, Status.UNAUTHORIZED, EndPoints.COLLECTIONS);
    }

    private <T extends ApiResponse<?>> T createEmptyCollection(
            Status expectedStatusCode,
            String title,
            Class<T> responseBodyClass) {
        CollectionPost collectionPost = CollectionPost.builder()
                .withTitle(title)
                .build();

        return postWithProfile1001(expectedStatusCode, collectionPost, responseBodyClass, EndPoints.COLLECTIONS);
    }

    private <T extends ApiResponse<?>> T createCollectionWithTags(
            Status expectedStatusCode,
            String title,
            Set<String> tags,
            Class<T> responseBodyClass) {
        CollectionPost collectionPost = CollectionPost.builder()
                .withTitle(title)
                .withTags(tags)
                .build();

        return postWithProfile1001(expectedStatusCode, collectionPost, responseBodyClass, EndPoints.COLLECTIONS);
    }

    private <T extends ApiResponse<?>> T getCollection(
            RequestSpecification request,
            int id,
            Status expectedStatusCode,
            Class<T> bodyClass) {
        return get(request, id, expectedStatusCode, bodyClass, EndPoints.COLLECTIONS);
    }

    private <T extends ApiResponse<?>, B> T putCollection(
            RequestSpecification request, int id,
            Status expectedStatusCode,
            B body,
            Class<T> bodyClass) {
        return put(request, id, expectedStatusCode, body, bodyClass, EndPoints.COLLECTIONS);
    }

    private <T extends ApiResponse<?>, B> T putCollectionAsProfile1001(
            int id,
            Status expectedStatusCode,
            B body,
            Class<T> bodyClass) {
        return putWithProfile1001(id, expectedStatusCode, body, bodyClass, EndPoints.COLLECTIONS);
    }
}
