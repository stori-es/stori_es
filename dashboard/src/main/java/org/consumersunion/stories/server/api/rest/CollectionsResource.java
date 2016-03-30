package org.consumersunion.stories.server.api.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.CollectionResourceLinks;
import org.consumersunion.stories.common.shared.dto.CollectionResponse;
import org.consumersunion.stories.common.shared.dto.CollectionsApiResponse;
import org.consumersunion.stories.common.shared.dto.Message;
import org.consumersunion.stories.common.shared.dto.Metadata;
import org.consumersunion.stories.common.shared.dto.post.CollectionPost;
import org.consumersunion.stories.common.shared.dto.post.CollectionPut;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.server.api.rest.converters.CollectionConverter;
import org.consumersunion.stories.server.api.rest.merger.CollectionPutMerger;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.business_logic.CollectionService;
import org.consumersunion.stories.server.business_logic.TagsService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import static org.consumersunion.stories.common.shared.api.EndPoints.DOCUMENTS;
import static org.consumersunion.stories.common.shared.api.EndPoints.ORGANIZATIONS;
import static org.consumersunion.stories.common.shared.api.EndPoints.QUESTIONNAIRES;
import static org.consumersunion.stories.common.shared.api.EndPoints.STORIES;
import static org.consumersunion.stories.common.shared.api.EndPoints.endsWithId;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ATTACHMENT;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.NOTE;

@Component
@Path(EndPoints.COLLECTIONS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ValidateRequest
public class CollectionsResource {
    private final CollectionService collectionService;
    private final UserService userService;
    private final TagsService tagsService;
    private final CollectionConverter collectionConverter;
    private final CollectionPutMerger collectionPutMerger;
    private final ResourceLinksHelper resourceLinksHelper;

    @Inject
    CollectionsResource(
            CollectionService collectionService,
            UserService userService,
            TagsService tagsService,
            CollectionConverter collectionConverter,
            CollectionPutMerger collectionPutMerger,
            ResourceLinksHelper resourceLinksHelper) {
        this.collectionService = collectionService;
        this.userService = userService;
        this.tagsService = tagsService;
        this.collectionConverter = collectionConverter;
        this.collectionPutMerger = collectionPutMerger;
        this.resourceLinksHelper = resourceLinksHelper;
    }

    @POST
    public Response createCollection(CollectionPost collectionPost) {
        Collection collection = collectionConverter.convert(collectionPost);
        collection.setOwner(userService.getContextOrganizationId());

        Collection savedCollection = collectionService.createCollection(collection);
        tagsService.setTags(savedCollection, collectionPost.getTags());

        CollectionData collectionData = new CollectionData(collection, collectionPost.getTags());
        CollectionsApiResponse collectionsResponse = createCollectionsResponse(collectionData);

        handleNotAuthorizedStories(collectionPost, savedCollection, collectionsResponse);

        return Response.ok(collectionsResponse).build();
    }

    @PUT
    @Path(EndPoints.ID)
    public Response updateCollection(@PathParam(UrlParameters.ID) int id, @Valid CollectionPut collectionPut) {
        CollectionData collectionData = collectionService.getCollectionData(id);
        Collection collection = collectionData.getCollection();

        collectionPutMerger.merge(collection, collectionPut);

        Collection updatedCollection = collectionService.updateCollection(collection);

        Set<String> tags = collectionPut.getTags();
        tagsService.setTags(updatedCollection, tags);

        CollectionData updatedCollectionData = new CollectionData(updatedCollection, tags);

        return Response.ok(createCollectionsResponse(updatedCollectionData)).build();
    }

    @GET
    @Path(EndPoints.ID)
    public Response getCollection(@PathParam(UrlParameters.ID) int id) {
        CollectionData collection = collectionService.getCollectionData(id);

        CollectionsApiResponse apiResponse = createCollectionsResponse(collection);

        return Response.ok(apiResponse).build();
    }

    @DELETE
    @Path(EndPoints.ID)
    public Response deleteCollection(@PathParam(UrlParameters.ID) int id) {
        collectionService.deleteCollection(id);

        return Response.noContent().build();
    }

    private void handleNotAuthorizedStories(
            CollectionPost collectionPost,
            Collection savedCollection,
            CollectionsApiResponse collectionsResponse) {
        List<Message> messages = collectionsResponse.getMetadata().getMessages();

        Message message = new Message("Not authorized to create the requested resource", "401");

        Set<Integer> storiesToAdd = Sets.newHashSet(collectionPost.getStoryIds());
        Set<Integer> storiesAdded = FluentIterable.from(savedCollection.getStories())
                .transform(new Function<StoryLink, Integer>() {
                    @Nullable
                    @Override
                    public Integer apply(StoryLink input) {
                        return input.getStory();
                    }
                }).toSet();

        Sets.SetView<Integer> difference = Sets.difference(storiesToAdd, storiesAdded);
        if (!difference.isEmpty()) {
            message.setTarget(createStoryIdsArray(difference));
            messages.add(message);
        }
    }

    private String createStoryIdsArray(Iterable<Integer> storyIds) {
        return "{ \"story_ids\": [" + Joiner.on(",").join(storyIds) + "]}";
    }

    private CollectionsApiResponse createCollectionsResponse(CollectionData... collections) {
        CollectionsApiResponse response = new CollectionsApiResponse();
        response.setData(toCollectionResponse(collections));
        response.setMetadata(new Metadata());
        return response;
    }

    private List<CollectionResponse> toCollectionResponse(CollectionData... collections) {
        return toCollectionResponse(Arrays.asList(collections));
    }

    private List<CollectionResponse> toCollectionResponse(List<CollectionData> collections) {
        return Lists.transform(collections, new Function<CollectionData, CollectionResponse>() {
            @Override
            public CollectionResponse apply(CollectionData collectionData) {
                CollectionResponse response = new CollectionResponse();

                Collection collection = collectionData.getCollection();
                response.setId(collection.getId());
                response.setCreatedOn(collection.getCreated());
                response.setUpdatedOn(collection.getUpdated());
                response.setTitle(collection.getTitle());
                response.setSummary(collection.getSummary());
                response.setArchived(collection.getDeleted());
                response.setLinks(generateLinks(collectionData));
                response.setTags(collectionData.getTags());
                response.setPublished(collection.isPublished());
                response.setPublishedOn(collection.getPublishedDate());

                return response;
            }
        });
    }

    private CollectionResourceLinks generateLinks(CollectionData collectionData) {
        CollectionResourceLinks collectionResourceLinks = new CollectionResourceLinks();

        Collection collection = collectionData.getCollection();
        // TODO : Add missing links
        collectionResourceLinks.setContent(
                resourceLinksHelper.replaceId(endsWithId(DOCUMENTS), collection.getBodyDocument().getId()));
        collectionResourceLinks.setNotes(resourceLinksHelper.replaceIds(endsWithId(DOCUMENTS),
                collectionData.getDocuments(NOTE).getDocuments()));
        collectionResourceLinks.setAttachments(resourceLinksHelper.replaceIds(endsWithId(DOCUMENTS),
                collectionData.getDocuments(ATTACHMENT).getDocuments()));
        collectionResourceLinks.setQuestionnaires(
                resourceLinksHelper.replaceIntIds(endsWithId(QUESTIONNAIRES), collection.getCollectionSources()));
        collectionResourceLinks.setOwner(
                resourceLinksHelper.replaceId(endsWithId(ORGANIZATIONS), collection.getOwner()));

        Set<StoryLink> stories = collection.getStories();
        if (stories != null) {
            collectionResourceLinks.setStories(
                    resourceLinksHelper.replaceIntIds(endsWithId(STORIES), convertStoryLinks(stories)));
        }

        return collectionResourceLinks;
    }

    private Iterable<Integer> convertStoryLinks(Set<StoryLink> stories) {
        return Iterables.transform(stories, new Function<StoryLink, Integer>() {
            @Override
            public Integer apply(StoryLink input) {
                return input.getStory();
            }
        });
    }
}
