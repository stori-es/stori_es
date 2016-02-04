package org.consumersunion.stories.common.shared.service.datatransferobject;

import java.util.List;

import org.consumersunion.stories.common.shared.model.document.Document;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Documents implements IsSerializable {
    private List<Document> documents;

    public Documents(List<Document> documents) {
        this.documents = documents;
    }

    public Documents() {
        this(Lists.<Document>newArrayList());
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addDocument(Document document) {
        documents.add(document);
    }

    public boolean hasDocuments() {
        return !documents.isEmpty();
    }
}
