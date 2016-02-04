package org.consumersunion.stories.server.rest.cors;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class QuestionnaireCors extends CorsResource {
    @OPTIONS
    @Path("/{permLink}")
    public Response getCollectionSurvey() {
        return buildCorsPreflightResponse();
    }
}
