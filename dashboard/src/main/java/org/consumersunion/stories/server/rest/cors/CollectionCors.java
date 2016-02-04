package org.consumersunion.stories.server.rest.cors;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class CollectionCors extends CorsResource {
    @OPTIONS
    public Response saveAnswers() {
        return buildCorsPreflightResponse();
    }

    @OPTIONS
    @Path("/{permLink}")
    public Response getCollection() {
        return buildCorsPreflightResponse();
    }
}
