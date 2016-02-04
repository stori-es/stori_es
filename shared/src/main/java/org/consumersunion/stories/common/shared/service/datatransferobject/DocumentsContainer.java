package org.consumersunion.stories.common.shared.service.datatransferobject;

import java.util.List;
import java.util.Map;

import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;

import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentsContainer implements IsSerializable {
    private Map<String, Documents> documents;

    public DocumentsContainer() {
        this.documents = Maps.newHashMap();
    }

    public DocumentsContainer(List<Document> documents) {
        this();

        addDocuments(documents);
    }

    public Map<String, Documents> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<String, Documents> documents) {
        this.documents.clear();
        this.documents.putAll(documents);
    }

    public Documents getDocuments(SystemEntityRelation relation) {
        String relationName = relation.name();
        if (documents.containsKey(relationName)) {
            return documents.get(relationName);
        } else {
            return new Documents();
        }
    }

    public void addDocuments(List<Document> documentsToAdd) {
        for (Document document : documentsToAdd) {
            addDocument(document);
        }
    }

    public void addDocument(Document document) {
        String documentKind = document.getSystemEntityRelation().name();
        Documents documentsContainer = documents.get(documentKind);

        if (documentsContainer == null) {
            documentsContainer = new Documents();
            documents.put(documentKind, documentsContainer);
        }

        documentsContainer.addDocument(document);
    }
}
