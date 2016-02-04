package org.consumersunion.stories.server.api.rest;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
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
import org.consumersunion.stories.common.shared.dto.DocumentResponse;
import org.consumersunion.stories.common.shared.dto.DocumentsApiResponse;
import org.consumersunion.stories.common.shared.dto.post.DocumentPost;
import org.consumersunion.stories.common.shared.dto.post.DocumentPut;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.server.api.rest.converters.DocumentPostConverter;
import org.consumersunion.stories.server.api.rest.converters.DocumentResponseConverter;
import org.consumersunion.stories.server.api.rest.merger.DocumentPutMerger;
import org.consumersunion.stories.server.business_logic.DocumentService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@Path(EndPoints.DOCUMENTS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ValidateRequest
public class DocumentsResource {
    private final DocumentResponseConverter documentResponseConverter;
    private final DocumentPostConverter documentPostConverter;
    private final DocumentPutMerger documentPutMerger;
    private final DocumentService documentService;
    private final UserService userService;

    @Inject
    DocumentsResource(
            DocumentResponseConverter documentResponseConverter,
            DocumentPostConverter documentPostConverter,
            DocumentPutMerger documentPutMerger,
            DocumentService documentService,
            UserService userService) {
        this.documentResponseConverter = documentResponseConverter;
        this.documentPostConverter = documentPostConverter;
        this.documentPutMerger = documentPutMerger;
        this.documentService = documentService;
        this.userService = userService;
    }

    @GET
    @Path(EndPoints.ID)
    public Response getDocument(@PathParam(UrlParameters.ID) int id) {
        Document document = documentService.getDocument(id);

        DocumentsApiResponse response = new DocumentsApiResponse();
        response.setData(toResponses(document));

        return Response.ok(response).build();
    }

    @POST
    public Response createDocument(@Valid DocumentPost documentPost) {
        Document document = documentPostConverter.convert(documentPost);
        document.setOwner(userService.getContextOrganizationId());

        Document savedDocument = documentService.createDocument(document);
        DocumentsApiResponse response = new DocumentsApiResponse();
        response.setData(toResponses(savedDocument));

        return Response.ok(response).build();
    }

    @PUT
    @Path(EndPoints.ID)
    public Response updateDocument(@PathParam(UrlParameters.ID) int id, @Valid DocumentPut documentPut) {
        Document document = documentService.getDocument(id);
        documentPutMerger.merge(document, documentPut);

        Document savedDocument = documentService.updateDocument(document);

        DocumentsApiResponse response = new DocumentsApiResponse();
        response.setData(toResponses(savedDocument));

        return Response.ok(response).build();
    }

    @DELETE
    @Path(EndPoints.ID)
    public Response deleteDocument(@PathParam(UrlParameters.ID) int id) {
        documentService.deleteDocument(id);

        return Response.noContent().build();
    }

    private List<DocumentResponse> toResponses(Document... documents) {
        return toResponses(Arrays.asList(documents));
    }

    private List<DocumentResponse> toResponses(List<Document> documents) {
        Iterable<DocumentResponse> responses = documentResponseConverter.reverse().convertAll(documents);

        return Lists.newArrayList(responses);
    }
}
