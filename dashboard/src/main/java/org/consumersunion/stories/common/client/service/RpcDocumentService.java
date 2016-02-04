package org.consumersunion.stories.common.client.service;

import java.util.Set;

import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.server.business_logic.DocumentService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/document")
public interface RpcDocumentService extends RemoteService {

    /**
     * Retrieves document. Requires document be public, or that the user own or
     * have read privs over the associated system entity.
     */
    DatumResponse<Document> getDocument(final int documentId);

    /**
     * @see DocumentService#getEntityDocument(int, int)
     */
    DatumResponse<Document> getDocument(int entityId, int documentId);

    /**
     * Retrieves document. Requires document be public, or that the user own or
     * have read privs over the associated system entity.
     */
    DataResponse<Document> getByEntityAndRelation(final int entityId, final SystemEntityRelation relation);

    /**
     * Saves the document. Requires ownership or WRITE over the associated
     * entity.
     */
    DatumResponse<Document> saveDocument(final Document document);

    /**
     * Saves the document. Requires ownership or WRITE over the associated
     * entity.
     */
    DatumResponse<Document> createUntitledDocument(final Document document);

    DatumResponse<Document> createDocument(Document document);

    ActionResponse createNotes(String text, Set<Integer> entityIds);

    /**
     * Saves the document. Requires ownership or WRITE over the associated
     * entity.
     */
    DatumResponse<Document> createAttachment(final Document document);

    /**
     * Deletes the document. User must own the associated system entity or have
     * WRITE over the system entity. Note, it's not DELETE because the document
     * is a not a thing unto itself (in this case), but data associated to the
     * entity.
     */
    ActionResponse deleteDocument(final int documentId);
}
