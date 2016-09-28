package org.consumersunion.stories.server.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.client.service.RpcQuestionnaireService;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.server.helper.AnalyticsProperties;
import org.consumersunion.stories.server.rest.cors.QuestionnaireCors;
import org.springframework.stereotype.Component;

@Component
@Path("/rest/questionnaire")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class QuestionnaireResource extends QuestionnaireCors {
    @Inject
    private RpcQuestionnaireService questionnaireService;
    @Inject
    private AnalyticsProperties analyticsProperties;

    @GET
    @Path("/{permLink}")
    public Response getCollectionSurvey(@PathParam("permLink") String permLink) {
        setupCorsResponse();

        QuestionnaireSurveyResponse questionnaireSurvey =
                questionnaireService.getQuestionnaireSurvey("/questionnaires/" + permLink);

        if (questionnaireSurvey.isError()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!questionnaireSurvey.getQuestionnaire().isPublished()) {
            questionnaireSurvey.setQuestionnaire(null);
        }

        return Response.ok(questionnaireSurvey)
                .header("ga-key", analyticsProperties.getApiKey())
                .build();
    }
}
