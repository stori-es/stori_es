package org.consumersunion.stories.survey.client.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.survey.client.util.CorsVariables;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.cors.CORS;

@CORS(domain = "${" + CorsVariables.DOMAIN + "}", port = "${" + CorsVariables.PORT + "}")
public interface QuestionnaireService extends RestService {

    @GET
    @Path("/questionnaire/{permLink}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    void getCollectionSurvey(@PathParam("permLink") String permLink,
            MethodCallback<QuestionnaireSurveyResponse> callback);
}
