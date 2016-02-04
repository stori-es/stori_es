package org.consumersunion.stories.server.api.rest;

import java.io.IOException;

import javax.annotation.Nullable;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.consumersunion.stories.common.shared.dto.ApiResponse;
import org.consumersunion.stories.common.shared.dto.Metadata;
import org.consumersunion.stories.common.shared.dto.ResponseStatus;
import org.springframework.stereotype.Component;

@Provider
@Component
public class ApiResponseFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        @Nullable Object entity = responseContext.getEntity();
        if (entity instanceof ApiResponse) {
            ApiResponse apiResponse = (ApiResponse) entity;

            Metadata metadata = apiResponse.getMetadata();
            if (metadata == null) {
                metadata = new Metadata();
                apiResponse.setMetadata(metadata);
            }

            addMetadata(metadata, requestContext, responseContext);
        }
    }

    private void addMetadata(Metadata metadata,
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) {
        metadata.setSelf(requestContext.getUriInfo().getAbsolutePath().toString());
        metadata.setHttpCode(responseContext.getStatusInfo().getStatusCode());
        metadata.setStatus(ResponseStatus.fromCode(metadata.getHttpCode()));
    }
}
