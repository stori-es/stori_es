package org.consumersunion.stories.server.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.client.service.RpcCollectionService;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.server.helper.AnalyticsProperties;
import org.consumersunion.stories.server.rest.cors.CollectionCors;
import org.springframework.stereotype.Component;

@Component
@Path("/rest/collection")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class CollectionResource extends CollectionCors {
    @Inject
    private RpcCollectionService collectionService;
    @Inject
    private AnalyticsProperties analyticsProperties;

    @POST
    public Response saveAnswers(AnswerSet answerSet) {
        setupCorsResponse();
        collectionService.saveAnswersAndStory(answerSet);
        return Response.ok().build();
    }

    @GET
    @Path("/{permLink}")
    public Response getCollectionSurvey(@PathParam("permLink") String permLink) {
        setupCorsResponse();

        Collection collection = collectionService.getCollectionByPermalink("/collections/" + permLink).getDatum();

        if (collection != null && collection.isPublished()) {
            return Response.ok(collection)
                    .header("ga-key", analyticsProperties.getApiKey())
                    .build();
        } else {
            return Response.ok().build();
        }
    }
}
