package org.consumersunion.stories.common.shared.service.datatransferobject;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Subscription;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;

public class CollectionData implements Serializable {
    private static final long serialVersionUID = 1699021069950202536L;

    private Collection collection;
    private Set<String> tags;
    private String owner;
    private Integer storiesCount;
    private List<QuestionnaireI15d> sourceQuestionnaires;
    private List<CollectionSummary> targetCollections;
    private List<Subscription> subscriptionsForActiveProfile;
    private DocumentsContainer documents;

    // For serialization
    CollectionData() {
        this(null, null);
    }

    public CollectionData(Collection collection, Set<String> tags) {
        this(collection, tags, null);
    }

    public CollectionData(Collection collection, Set<String> tags, String owner) {
        this.owner = owner;
        this.collection = collection;
        this.tags = tags;
        this.documents = new DocumentsContainer();
    }

    public Collection getCollection() {
        return collection;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return collection.getTitle();
    }

    public int getId() {
        return collection.getId();
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setStoriesCount(Integer storiesCount) {
        this.storiesCount = storiesCount;
    }

    public Integer getStoriesCount() {
        return storiesCount;
    }

    public void setSourceQuestionnaires(List<QuestionnaireI15d> sourceQuestionnaires) {
        this.sourceQuestionnaires = sourceQuestionnaires;
    }

    public List<QuestionnaireI15d> getSourceQuestionnaires() {
        return sourceQuestionnaires;
    }

    public void setTargetCollections(List<CollectionSummary> targetCollections) {
        this.targetCollections = targetCollections;
    }

    public List<CollectionSummary> getTargetCollections() {
        return targetCollections;
    }

    public void setSubscriptionsForActiveProfile(List<Subscription> subscriptionsForActiveProfile) {
        this.subscriptionsForActiveProfile = subscriptionsForActiveProfile;
    }

    public List<Subscription> getSubscriptionsForActiveProfile() {
        return subscriptionsForActiveProfile;
    }

    public DocumentsContainer getDocuments() {
        return documents;
    }

    public void setDocuments(DocumentsContainer documents) {
        this.documents = documents;
    }

    public Documents getDocuments(SystemEntityRelation relation) {
        return documents.getDocuments(relation);
    }

    public void addDocuments(List<Document> documentsToAdd) {
        documents.addDocuments(documentsToAdd);
    }

    public void addDocument(Document document) {
        documents.addDocument(document);
    }
}
