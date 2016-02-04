package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("permission_links")
public class PermissionRessourceLinks extends ResourceLinks {
    private ResourceLink organization;
    private List<ResourceLink> entities;
    private ResourceLink content;
    private ResourceLink creator;
    private ResourceLink owner;
    private List<ResourceLink> administrators;

    public ResourceLink getOrganization() {
        return organization;
    }

    public void setOrganization(ResourceLink organization) {
        this.organization = organization;
    }

    public List<ResourceLink> getEntities() {
        return entities;
    }

    public void setEntities(List<ResourceLink> entities) {
        this.entities = entities;
    }

    public ResourceLink getContent() {
        return content;
    }

    public void setContent(ResourceLink content) {
        this.content = content;
    }

    @Override
    public ResourceLink getCreator() {
        return creator;
    }

    @Override
    public void setCreator(ResourceLink creator) {
        this.creator = creator;
    }

    @Override
    public ResourceLink getOwner() {
        return owner;
    }

    @Override
    public void setOwner(ResourceLink owner) {
        this.owner = owner;
    }

    @Override
    public List<ResourceLink> getAdministrators() {
        return administrators;
    }

    @Override
    public void setAdministrators(
            List<ResourceLink> administrators) {
        this.administrators = administrators;
    }
}
