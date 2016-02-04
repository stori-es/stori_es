package org.consumersunion.stories.common.shared.dto;

import java.util.List;

public abstract class BaseCollectionResourceLinks extends ResourceLinks {
    private ResourceLink content;
    private List<ResourceLink> stories;
    private List<ResourceLink> notes;
    private List<ResourceLink> attachments;

    public ResourceLink getContent() {
        return content;
    }

    public void setContent(ResourceLink content) {
        this.content = content;
    }

    public List<ResourceLink> getStories() {
        return stories;
    }

    public void setStories(List<ResourceLink> stories) {
        this.stories = stories;
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
