package org.consumersunion.stories.common.client.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.StoriesApiResponse;

import com.gwtplatform.dispatch.rest.shared.RestAction;

@Path(EndPoints.STORIES)
public interface StoriesService {
    @GET
    @Path(EndPoints.ID)
    RestAction<StoriesApiResponse> getStory(@PathParam(UrlParameters.ID) int id);
}
