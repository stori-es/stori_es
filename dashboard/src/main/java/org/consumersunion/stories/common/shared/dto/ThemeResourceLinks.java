package org.consumersunion.stories.common.shared.dto;

import java.util.List;

public class ThemeResourceLinks extends ResourceLinks {
    private List<ResourceLink> organizations;
    private List<ResourceLink> entities;

    public List<ResourceLink> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<ResourceLink> organizations) {
        this.organizations = organizations;
    }

    public List<ResourceLink> getEntities() {
        return entities;
    }

    public void setEntities(List<ResourceLink> entities) {
        this.entities = entities;
    }
}
