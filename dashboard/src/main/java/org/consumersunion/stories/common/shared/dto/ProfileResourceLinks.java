package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.google.common.collect.Lists;

public class ProfileResourceLinks extends ResourceLinks {
    private final List<ResourceLink> stories;
    private final List<ResourceLink> notes;

    private ResourceLink organization;
    private ResourceLink user;

    public ProfileResourceLinks() {
        stories = Lists.newArrayList();
        notes = Lists.newArrayList();
    }

    public ResourceLink getOrganization() {
        return organization;
    }

    public void setOrganization(ResourceLink organization) {
        this.organization = organization;
    }

    public ResourceLink getUser() {
        return user;
    }

    public void setUser(ResourceLink user) {
        this.user = user;
    }

    public List<ResourceLink> getStories() {
        return stories;
    }

    public void setStories(List<ResourceLink> stories) {
        this.stories.clear();
        this.stories.addAll(stories);
    }

    public List<ResourceLink> getNotes() {
        return notes;
    }

    public void setNotes(List<ResourceLink> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
    }
}
