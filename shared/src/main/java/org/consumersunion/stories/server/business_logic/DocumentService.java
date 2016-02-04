package org.consumersunion.stories.server.business_logic;

import java.util.List;

import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.server.persistence.DocumentPersister.EntityAndRelationParams;

public interface DocumentService {
    List<Document> getDocuments(SystemEntity systemEntity);

    List<Document> getStoryDocuments(Story story);

    /**
     * Retrieves document. Requires document be public, or that the user own or have read privs over the associated
     * system entity.
     */
    Document getEntityDocument(int entityId, int documentId);

    Document getWaiverDocument(int organizationId);

    Document createDocument(Document document);

    Document updateDocument(Document document);

    Document getDocument(int documentId);

    Document getBodyDocument(int entityId);

    Document getOriginalBodyDocument(int entityId);

    List<Document> getDocumentsByEntityAndRelation(EntityAndRelationParams params);

    void deleteDocument(int documentId);
}
