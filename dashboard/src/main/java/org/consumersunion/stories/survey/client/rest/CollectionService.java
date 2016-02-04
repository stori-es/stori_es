package org.consumersunion.stories.survey.client.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.survey.client.util.CorsVariables;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.cors.CORS;

@CORS(domain = "${" + CorsVariables.DOMAIN + "}", port = "${" + CorsVariables.PORT + "}")
public interface CollectionService extends RestService {

    @POST
    @Path("/collection")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    void saveAnswers(AnswerSet answerSet, MethodCallback<Void> callback);

    @GET
    @Path("/collection/{permLink}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    void getCollection(@PathParam("permLink") String permLink, MethodCallback<Collection> callback);
}
