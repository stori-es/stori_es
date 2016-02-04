package org.consumersunion.stories.common.shared.service.datatransferobject;

import java.io.Serializable;

import org.consumersunion.stories.common.shared.model.Collection;

import com.google.common.base.MoreObjects;

public class CollectionSummary implements Serializable, Comparable<CollectionSummary> {
    private Integer id;
    private String title;
    private boolean questionnaire;
    private boolean deleted;
    private int nbStories;

    // For serialization
    CollectionSummary() {
    }

    public CollectionSummary(Collection collection) {
        this.id = collection.getId();
        this.title = collection.getTitle();
        this.questionnaire = collection.isQuestionnaire();
        this.deleted = collection.getDeleted();
        this.nbStories = collection.getStories().size();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(boolean isQuestionnaire) {
        this.questionnaire = isQuestionnaire;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getNbStories() {
        return nbStories;
    }

    public void setNbStories(int nbStories) {
        this.nbStories = nbStories;
    }

    @Override
    public int compareTo(CollectionSummary o) {
        int sourceIsQuestionnaire = this.isQuestionnaire() ? 1 : 0;
        int destIsQuestionnaire = o.isQuestionnaire() ? 1 : 0;
        return destIsQuestionnaire - sourceIsQuestionnaire;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("questionnaire", questionnaire)
                .add("nbStories", nbStories)
                .toString();
    }
}
