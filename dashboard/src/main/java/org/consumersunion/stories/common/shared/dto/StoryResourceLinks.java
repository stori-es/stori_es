package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StoryResourceLinks extends ResourceLinks {
    private List<ResourceLink> contents;
    @JsonProperty("default_content")
    private ResourceLink defaultContent;
    private List<ResourceLink> responses;
    private List<ResourceLink> permissions;
    private List<ResourceLink> notes;
    private List<ResourceLink> attachments;

    public List<ResourceLink> getContents() {
        return contents;
    }

    public void setContents(List<ResourceLink> contents) {
        this.contents = contents;
    }

    public ResourceLink getDefaultContent() {
        return defaultContent;
    }

    public void setDefaultContent(ResourceLink defaultContent) {
        this.defaultContent = defaultContent;
    }

    public List<ResourceLink> getResponses() {
        return responses;
    }

    public void setResponses(List<ResourceLink> responses) {
        this.responses = responses;
    }

    public List<ResourceLink> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ResourceLink> permissions) {
        this.permissions = permissions;
    }

    public List<ResourceLink> getNotes() {
        return notes;
    }

    public void setNotes(List<ResourceLink> notes) {
        this.notes = notes;
    }

    public List<ResourceLink> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ResourceLink> attachments) {
        this.attachments = attachments;
    }
}
