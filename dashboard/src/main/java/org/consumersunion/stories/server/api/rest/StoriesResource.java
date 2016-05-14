package org.consumersunion.stories.server.api.rest;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
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
import org.consumersunion.stories.common.shared.dto.StoriesApiResponse;
import org.consumersunion.stories.common.shared.dto.StoryResourceLinks;
import org.consumersunion.stories.common.shared.dto.StoryResponse;
import org.consumersunion.stories.common.shared.dto.post.StoryPost;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.api.rest.converters.StoryPostConverter;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.business_logic.CollectionService;
import org.consumersunion.stories.server.business_logic.StoryService;
import org.consumersunion.stories.server.business_logic.TagsService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.api.EndPoints.DOCUMENTS;
import static org.consumersunion.stories.common.shared.api.EndPoints.NOTES;
import static org.consumersunion.stories.common.shared.api.EndPoints.PROFILES;
import static org.consumersunion.stories.common.shared.api.EndPoints.endsWithId;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ANSWER_SET;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ATTACHMENT;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.BODY;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.NOTE;

@Component
@Path(EndPoints.STORIES)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StoriesResource {
    private final UserService userService;
    private final StoryService storyService;
    private final CollectionService collectionService;
    private final StoryPostConverter storyPostConverter;
    private final TagsService tagsService;
    private final ResourceLinksHelper resourceLinksHelper;

    @Inject
    StoriesResource(
            UserService userService,
            StoryService storyService,
            CollectionService collectionService,
            StoryPostConverter storyPostConverter,
            TagsService tagsService,
            ResourceLinksHelper resourceLinksHelper) {
        this.userService = userService;
        this.storyService = storyService;
        this.collectionService = collectionService;
        this.storyPostConverter = storyPostConverter;
        this.tagsService = tagsService;
        this.resourceLinksHelper = resourceLinksHelper;
    }

    @POST
    public Response createStory(StoryPost storyPost) {
        if (storyPost == null) {
            storyPost = new StoryPost();
        }

        Story story = storyPostConverter.convert(storyPost);
        Story savedStory = storyService.createStory(story);
        collectionService.linkStoryToCollections(storyPost.getCollectionIds(), savedStory);
        tagsService.setTags(savedStory, storyPost.getTags());

        StorySummary savedStorySummary = storyService.getStorySummary(savedStory.getId());

        return Response.status(Response.Status.CREATED).entity(createStoryApiResponse(savedStorySummary)).build();
    }

    @GET
    @Path(EndPoints.ID)
    public Response getStory(@PathParam(UrlParameters.ID) int id) {
        StorySummary story = storyService.getStorySummary(id);

        StoriesApiResponse apiResponse = createStoryApiResponse(story);

        return Response.ok(apiResponse).build();
    }

    @PUT
    @Path(EndPoints.ID)
    public Response updateStory(@PathParam(UrlParameters.ID) int id,
            StoryPost storyPost) {
        Story story = storyService.getStory(id);

        story.setDocuments(ATTACHMENT, storyPost.getAttachmentIds());
        story.setDocuments(BODY, storyPost.getContentIds());
        story.setDocuments(NOTE, storyPost.getNoteIds());
        story.setDocuments(ANSWER_SET, storyPost.getResponseIds());
        story.setDefaultContent(storyPost.getDefaultContentId());

        storyService.updateStory(story);
        tagsService.setTags(story, storyPost.getTags());

        StorySummary storySummary = storyService.getStorySummary(id);

        return Response.ok(createStoryApiResponse(storySummary)).build();
    }

    @DELETE
    @Path(EndPoints.ID)
    public Response deleteStory(@PathParam(UrlParameters.ID) int id) {
        storyService.deleteStory(id);

        return Response.noContent().build();
    }

    private StoriesApiResponse createStoryApiResponse(StorySummary storySummary) {
        StoriesApiResponse response = new StoriesApiResponse();
        response.setData(Lists.newArrayList(toStoryResponse(storySummary)));
        return response;
    }

    private List<StoryResponse> toStoryResponse(StorySummary... storySummaries) {
        return toStoryResponse(Arrays.asList(storySummaries));
    }

    private List<StoryResponse> toStoryResponse(List<StorySummary> storySummaries) {
        return Lists.transform(storySummaries, new Function<StorySummary, StoryResponse>() {
            @Override
            public StoryResponse apply(StorySummary input) {
                return toStoryResponseBuilder(input.getStory())
                        .withTags(input.getTags())
                        .withLinks(generateLinks(input))
                        .build();
            }
        });
    }

    private StoryResponse.Builder toStoryResponseBuilder(Story story) {
        return new StoryResponse.Builder()
                .withId(story.getId())
                .withCreatedOn(story.getCreated())
                .withUpdatedOn(story.getUpdated());
    }

    private StoryResourceLinks generateLinks(StorySummary input) {
        StoryResourceLinks storyResourceLinks = new StoryResourceLinks();

        // TODO: Fill in missing links
        storyResourceLinks.setAttachments(
                resourceLinksHelper.replaceIds(endsWithId(DOCUMENTS), getDocuments(input, ATTACHMENT)));
        storyResourceLinks.setContents(
                resourceLinksHelper.replaceIds(endsWithId(DOCUMENTS), getDocuments(input, BODY)));
        storyResourceLinks.setDefaultContent(
                resourceLinksHelper.replaceId(endsWithId(DOCUMENTS), input.getStory().getDefaultContent()));
        storyResourceLinks.setNotes(
                resourceLinksHelper.replaceIds(endsWithId(NOTES), getDocuments(input, NOTE)));
        storyResourceLinks.setResponses(
                resourceLinksHelper.replaceIds(endsWithId(DOCUMENTS), getDocuments(input, ANSWER_SET)));
        storyResourceLinks.setOwner(
                resourceLinksHelper.replaceId(endsWithId(PROFILES), input.getStory().getOwner()));

        return storyResourceLinks;
    }

    private List<Document> getDocuments(StorySummary input, SystemEntityRelation relation) {
        return input.getDocuments(relation).getDocuments();
    }
}
