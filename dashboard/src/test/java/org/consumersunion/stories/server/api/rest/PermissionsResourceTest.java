package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.PermissionsApiResponse;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.server.api.rest.converters.PermissionsConverter;
import org.consumersunion.stories.server.business_logic.DocumentService;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(JukitoRunner.class)
public class PermissionsResourceTest {
    private static final int DOCUMENT_ID = 400;

    @Inject
    private PermissionsResource permissionsResource;
    @Inject
    private DocumentService documentService;
    @Inject
    private PermissionsConverter permissionsConverter;

    @Test(expected = NotFoundException.class)
    public void getPermission_notAPermissionDocument_throwsNotFound() {
        Document document = new Document();
        document.setSystemEntityRelation(SystemEntityRelation.BODY);
        given(documentService.getDocument(DOCUMENT_ID)).willReturn(document);

        permissionsResource.getPermission(DOCUMENT_ID);
    }

    @Test
    public void getPermission_returnsDocument() {
        Document document = new Document(DOCUMENT_ID, 1);
        document.setSystemEntityRelation(SystemEntityRelation.DEFAULT_PERMISSIONS);
        given(documentService.getDocument(DOCUMENT_ID)).willReturn(document);

        Response response = permissionsResource.getPermission(DOCUMENT_ID);

        PermissionsApiResponse apiResponse = (PermissionsApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(DOCUMENT_ID);
    }
}
