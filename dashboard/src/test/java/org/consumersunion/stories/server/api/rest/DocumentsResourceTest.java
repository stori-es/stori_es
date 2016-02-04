package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.DocumentsApiResponse;
import org.consumersunion.stories.common.shared.dto.post.DocumentPost;
import org.consumersunion.stories.common.shared.dto.post.DocumentPut;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.server.api.rest.converters.DocumentPostConverter;
import org.consumersunion.stories.server.api.rest.converters.DocumentResponseConverter;
import org.consumersunion.stories.server.api.rest.merger.DocumentPutMerger;
import org.consumersunion.stories.server.business_logic.DocumentService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class DocumentsResourceTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(DocumentPostConverter.class);
            forceMock(DocumentPutMerger.class);
        }
    }

    private static final int DOCUMENT_ID = 3;
    private static final int ORGANIZATION_ID = 2;

    @Inject
    private DocumentsResource documentsResource;
    @Inject
    private DocumentResponseConverter documentResponseConverter;
    @Inject
    private DocumentPostConverter documentPostConverter;
    @Inject
    private DocumentPutMerger documentPutMerger;
    @Inject
    private DocumentService documentService;
    @Inject
    private UserService userService;

    @Test
    public void getDocument() throws Exception {
        given(documentService.getDocument(DOCUMENT_ID)).willReturn(createDocumentWithId());

        Response response = documentsResource.getDocument(DOCUMENT_ID);

        DocumentsApiResponse apiResponse = (DocumentsApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(DOCUMENT_ID);
    }

    @Test
    public void createDocument() throws Exception {
        DocumentPost documentPost = new DocumentPost();
        Document document = spy(new Document());
        Document savedDocument = createDocumentWithId();
        given(documentPostConverter.convert(same(documentPost))).willReturn(document);
        given(userService.getContextOrganizationId()).willReturn(ORGANIZATION_ID);
        given(documentService.createDocument(same(document))).willReturn(savedDocument);

        Response response = documentsResource.createDocument(documentPost);

        DocumentsApiResponse apiResponse = (DocumentsApiResponse) response.getEntity();
        InOrder inOrder = Mockito.inOrder(document, documentService);
        inOrder.verify(document).setOwner(ORGANIZATION_ID);
        inOrder.verify(documentService).createDocument(same(document));
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(DOCUMENT_ID);
    }

    @Test
    public void updateDocument() throws Exception {
        DocumentPut documentPut = new DocumentPut();
        Document document = new Document();
        Document savedDocument = createDocumentWithId();
        given(documentService.getDocument(DOCUMENT_ID)).willReturn(document);
        given(documentService.updateDocument(same(document))).willReturn(savedDocument);

        Response response = documentsResource.updateDocument(DOCUMENT_ID, documentPut);

        DocumentsApiResponse apiResponse = (DocumentsApiResponse) response.getEntity();
        InOrder inOrder = Mockito.inOrder(documentPutMerger, documentService);
        inOrder.verify(documentPutMerger).merge(same(document), same(documentPut));
        inOrder.verify(documentService).updateDocument(same(document));
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(DOCUMENT_ID);
    }

    @Test
    public void deleteDocument() throws Exception {
        documentsResource.deleteDocument(DOCUMENT_ID);

        verify(documentService).deleteDocument(DOCUMENT_ID);
    }

    private Document createDocumentWithId() {
        Document document = new Document(DOCUMENT_ID, 1);
        document.setSystemEntityRelation(SystemEntityRelation.ATTACHMENT);
        return document;
    }
}
