package org.consumersunion.stories.service.client;

import java.util.List;

import org.consumersunion.stories.common.client.service.RpcDocumentService;
import org.consumersunion.stories.common.client.service.RpcDocumentServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.Document.DocumentContributorRole;
import org.consumersunion.stories.common.shared.model.document.DocumentContributor;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentServiceGwtTest extends GWTTestCaseExposed {
    @Override
    public String getModuleName() {
        return "org.consumersunion.stories.storiesJUnit";
    }

    ;

    @Test
    public void testSaveNewDocument() {
        final RpcDocumentServiceAsync service = GWT.create(RpcDocumentService.class);
        final Document doc = new Document();

        service.saveDocument(doc, new AsyncCallback<DatumResponse<Document>>() {
            @Override
            public void onSuccess(final DatumResponse<Document> result) {
                checkBasicDocumentResponse(result);

                final Document document = result.getDatum();
                assertEquals(1, document.getVersion());
            }

            @Override
            public void onFailure(final Throwable caught) {
                fail("Call to service failed when saving a new document");
            }
        });
    }

    @Test
    public void testCreateNote() {
        final RpcDocumentServiceAsync documentService = GWT.create(RpcDocumentService.class);
        final Document doc = new Document();
        final String noteText = "Note text";
        doc.setEntity(1);
        doc.addBlock(new Content(BlockType.CONTENT, noteText, TextType.PLAIN));
        doc.setSystemEntityRelation(SystemEntityRelation.NOTE);

        documentService.createUntitledDocument(doc, new AsyncCallback<DatumResponse<Document>>() {
            @Override
            public void onSuccess(final DatumResponse<Document> result) {
                checkBasicDocumentResponse(result);

                final Document response = result.getDatum();
                assertEquals(1, response.getVersion());
                checkNoteDetails(doc, response.getId(), documentService);
            }

            @Override
            public void onFailure(final Throwable caught) {
                fail("Call to service failed when saving a new document");
            }
        });
    }

    protected void checkNoteDetails(final Document note, final int documentId, final RpcDocumentServiceAsync service) {
        service.getDocument(documentId, new AsyncCallback<DatumResponse<Document>>() {
            @Override
            public void onSuccess(final DatumResponse<Document> arg0) {
                final Document response = arg0.getDatum();
                assertEquals("Note's title should be blank", "", response.getTitle());
                assertEquals("Note's permalink should be blank", "", response.getPermalink());
                assertFalse("Notes shouldn't be public", response.isPublic());
                assertEquals("Note is not related to the proper enity", note.getSystemEntity(),
                        response.getSystemEntity());
                assertEquals("Note's relation should be 'NOTE'", SystemEntityRelation.NOTE,
                        response.getSystemEntityRelation());
                assertEquals("Unexpected block count in response.", 1, response.getBlocks().size());
                assertEquals("Unexpceted block type in response.", Content.class, response.getBlocks().get(0));
                Content responseBlock = (Content) note.getBlocks().get(0);
                assertEquals("Note's TextType should be 'PLAIN'", TextType.PLAIN, responseBlock.getTextType());
                assertEquals("Note created with wrong version", 1, response.getVersion());
                assertEquals("Note created with wrong text", ((Content) note.getBlocks().get(0)).getContent(),
                        responseBlock.getContent());
                assertEquals("Wrong primaryAuthor ID", 1, response.getPrimaryAuthor());

                final List<DocumentContributor> contributors = response.getContributors();
                assertEquals("Note's contributor not set", 1, contributors.size());

                final DocumentContributor contributor = contributors.get(0);
                assertEquals("Wrong role in note contributor", DocumentContributorRole.AUTHOR, contributor.getRole());
                assertEquals("Wrong contributor ID", 1, contributor.getContributor());
            }

            @Override
            public void onFailure(final Throwable arg0) {
                fail("Got an unexpected exception");
            }
        });
    }

    @Test
    public void testGetDocument() {
        final RpcDocumentServiceAsync service = GWT.create(RpcDocumentService.class);

        service.getDocument(1, new AsyncCallback<DatumResponse<Document>>() {
            @Override
            public void onSuccess(final DatumResponse<Document> result) {
                checkBasicDocumentResponse(result);
            }

            @Override
            public void onFailure(final Throwable caught) {
                fail("Unexpected exception getting document");
            }
        });
    }

    @Test
    public void testSaveExistingDocument() {
        final RpcDocumentServiceAsync service = GWT.create(RpcDocumentService.class);

        service.getDocument(1, new AsyncCallback<DatumResponse<Document>>() {
            @Override
            public void onSuccess(final DatumResponse<Document> result) {
                doUpdate(result.getDatum(), service);
            }

            @Override
            public void onFailure(final Throwable caught) {
                fail("Unexpected exception getting document");
            }
        });
    }

    protected void doUpdate(final Document document, final RpcDocumentServiceAsync service) {
        service.saveDocument(document, new AsyncCallback<DatumResponse<Document>>() {
            @Override
            public void onSuccess(final DatumResponse<Document> result) {
                checkBasicDocumentResponse(result);

                final Document updatedDoc = result.getDatum();
                assertEquals(document.getVersion() + 1, updatedDoc.getVersion());
            }

            @Override
            public void onFailure(final Throwable caught) {
                fail("Unexpected exception updating document");
            }
        });
    }

    @Test
    public void testDeleteDocument() {
        final RpcDocumentServiceAsync service = GWT.create(RpcDocumentService.class);
        final Document doc = new Document();

        service.saveDocument(doc, new AsyncCallback<DatumResponse<Document>>() {
            @Override
            public void onSuccess(final DatumResponse<Document> result) {
                checkBasicDocumentResponse(result);
                doDelete(result.getDatum(), service);
            }

            @Override
            public void onFailure(final Throwable caught) {
                fail("Call to service failed when saving a new document");
            }
        });
    }

    protected void doDelete(final Document document, final RpcDocumentServiceAsync service) {
        service.deleteDocument(document.getId(), new AsyncCallback<ActionResponse>() {
            @Override
            public void onSuccess(final ActionResponse result) {

            }

            @Override
            public void onFailure(final Throwable caught) {
                fail("Call to service failed when deleting a new document");
            }
        });
    }

    private void checkBasicDocumentResponse(final DatumResponse<Document> result) {
        assertNotNull("Invalid result: null", result);
        assertNotNull("Invalid response document: null", result.getDatum());
        assertFalse("Returned document has bad id", result.getDatum().isNew());
    }
}
