package org.consumersunion.stories.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.consumersunion.stories.common.client.service.RpcStoryService;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.rest.cors.StoryCors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/rest/story")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class StoryResource extends StoryCors {
    private static final Integer STORIES_COUNT = 10;

    @Autowired
    private RpcStoryService storyService;

    @GET
    @Path("/{collectionId}")
    public DataResponse<StorySummary> getRecentStories(@PathParam("collectionId") Integer collectionId) {
        return storyService.getRecentStories(collectionId, STORIES_COUNT);
    }
}
