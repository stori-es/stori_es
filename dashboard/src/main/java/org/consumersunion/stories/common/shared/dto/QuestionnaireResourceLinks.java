package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionnaireResourceLinks extends BaseCollectionResourceLinks {
    private List<ResourceLink> collections;
    private ResourceLink next;
    private List<ResourceLink> responses;
    private List<ResourceLink> autotags;
    private List<ResourceLink> forms;
    @JsonProperty("default_form")
    private ResourceLink defaultForm;
    private List<ResourceLink> contents;

    public List<ResourceLink> getCollections() {
        return collections;
    }

    public void setCollections(List<ResourceLink> collections) {
        this.collections = collections;
    }

    public ResourceLink getNext() {
        return next;
    }

    public void setNext(ResourceLink next) {
        this.next = next;
    }

    public List<ResourceLink> getResponses() {
        return responses;
    }

    public void setResponses(List<ResourceLink> responses) {
        this.responses = responses;
    }

    public List<ResourceLink> getAutotags() {
        return autotags;
    }

    public void setAutotags(List<ResourceLink> autotags) {
        this.autotags = autotags;
    }

    public List<ResourceLink> getForms() {
        return forms;
    }

    public void setForms(List<ResourceLink> forms) {
        this.forms = forms;
    }

    public ResourceLink getDefaultForm() {
        return defaultForm;
    }

    public void setDefaultForm(ResourceLink defaultForm) {
        this.defaultForm = defaultForm;
    }

    public void setContents(List<ResourceLink> contents) {
        this.contents = contents;
    }

    public List<ResourceLink> getContents() {
        return contents;
    }
}
