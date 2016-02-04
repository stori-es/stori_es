package org.consumersunion.stories.common.client.service;

import java.util.Set;

import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcDocumentServiceAsync {
    void getDocument(int documentId, AsyncCallback<DatumResponse<Document>> callback);

    void getDocument(int entityId, int documentId, AsyncCallback<DatumResponse<Document>> callback);

    void getByEntityAndRelation(int entityId, SystemEntityRelation relation,
            AsyncCallback<DataResponse<Document>> callback);

    void saveDocument(Document document, AsyncCallback<DatumResponse<Document>> callback);

    void createNotes(String text, Set<Integer> entityIds, AsyncCallback<ActionResponse> callback);

    void createUntitledDocument(Document document, AsyncCallback<DatumResponse<Document>> callback);

    void createDocument(Document document, AsyncCallback<DatumResponse<Document>> callback);

    void createAttachment(Document document, AsyncCallback<DatumResponse<Document>> callback);

    void deleteDocument(int documentId, AsyncCallback<ActionResponse> callback);
}
