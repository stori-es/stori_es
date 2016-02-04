package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.PermissionsApiResponse;
import org.consumersunion.stories.common.shared.dto.post.PermissionPost;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.server.api.rest.converters.PermissionsConverter;
import org.consumersunion.stories.server.business_logic.DocumentService;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.springframework.stereotype.Component;

import com.google.gwt.thirdparty.guava.common.collect.Lists;

@Component
@Path(EndPoints.PERMISSIONS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PermissionsResource {
    private final DocumentService documentService;
    private final PermissionsConverter permissionsConverter;

    @Inject
    PermissionsResource(
            DocumentService documentService,
            PermissionsConverter permissionsConverter) {
        this.documentService = documentService;
        this.permissionsConverter = permissionsConverter;
    }

    @GET
    @Path(EndPoints.ID)
    public Response getPermission(@PathParam(UrlParameters.ID) int id) {
        Document document = documentService.getDocument(id);

        if (!SystemEntityRelation.DEFAULT_PERMISSIONS.equals(document.getSystemEntityRelation())) {
            throw new NotFoundException();
        }

        PermissionsApiResponse response = new PermissionsApiResponse();
        response.setData(Lists.newArrayList(permissionsConverter.convert(document)));

        return Response.ok(response).build();
    }

    @POST
    public Response createPermission(PermissionPost permissionPost) {
        throw new UnsupportedOperationException();
    }

    @PUT
    @Path(EndPoints.ID)
    public Response putPermission(@PathParam(UrlParameters.ID) int id) {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path(EndPoints.ID)
    public Response deletePermission(@PathParam(UrlParameters.ID) int id) {
        throw new UnsupportedOperationException();
    }
}
