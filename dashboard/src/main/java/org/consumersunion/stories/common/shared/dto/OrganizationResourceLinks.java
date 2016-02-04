package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.Lists;

@JsonTypeName("organization_links")
public class OrganizationResourceLinks extends ResourceLinks {
    private final List<ResourceLink> profiles;
    private final List<ResourceLink> questionnaires;
    private final List<ResourceLink> collections;
    private final List<ResourceLink> permissions;
    private final List<ResourceLink> themes;
    private final List<ResourceLink> notes;
    private final List<ResourceLink> attachments;

    @JsonProperty("default_permission")
    private ResourceLink defaultPermission;
    @JsonProperty("default_theme")
    private ResourceLink defaultTheme;

    public OrganizationResourceLinks() {
        profiles = Lists.newArrayList();
        questionnaires = Lists.newArrayList();
        collections = Lists.newArrayList();
        permissions = Lists.newArrayList();
        themes = Lists.newArrayList();
        notes = Lists.newArrayList();
        attachments = Lists.newArrayList();
    }

    public List<ResourceLink> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ResourceLink> profiles) {
        this.profiles.clear();
        this.profiles.addAll(profiles);
    }

    public List<ResourceLink> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(List<ResourceLink> questionnaires) {
        this.questionnaires.clear();
        this.questionnaires.addAll(questionnaires);
    }

    public List<ResourceLink> getCollections() {
        return collections;
    }

    public void setCollections(List<ResourceLink> collections) {
        this.collections.clear();
        this.collections.addAll(collections);
    }

    public List<ResourceLink> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ResourceLink> permissions) {
        this.permissions.clear();
        this.permissions.addAll(permissions);
    }

    public ResourceLink getDefaultPermission() {
        return defaultPermission;
    }

    public void setDefaultPermission(ResourceLink defaultPermission) {
        this.defaultPermission = defaultPermission;
    }

    public List<ResourceLink> getThemes() {
        return themes;
    }

    public void setThemes(List<ResourceLink> themes) {
        this.themes.clear();
        this.themes.addAll(themes);
    }

    public ResourceLink getDefaultTheme() {
        return defaultTheme;
    }

    public void setDefaultTheme(ResourceLink defaultTheme) {
        this.defaultTheme = defaultTheme;
    }

    public List<ResourceLink> getNotes() {
        return notes;
    }

    public void setNotes(List<ResourceLink> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
    }

    public List<ResourceLink> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ResourceLink> attachments) {
        this.attachments.clear();
        this.attachments.addAll(attachments);
    }
}
