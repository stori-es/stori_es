package org.consumersunion.stories.server.rest.cors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public class CorsResource {
    @Context
    protected HttpServletResponse response;
    @Context
    protected HttpServletRequest request;

    protected void setupCorsResponse() {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, UPDATE, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "content-type, x-http-method-override");
        response.addHeader("Access-Control-Expose-Headers", "ga-key");
    }

    protected Response buildCorsPreflightResponse() {
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, UPDATE, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "content-type, x-http-method-override").build();
    }
}
