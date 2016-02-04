package org.consumersunion.stories.common.shared.dto;

public class DocumentResourceLinks extends ResourceLinks {
    private ResourceLink entity;
    private ResourceLink source;
    private ResourceLink theme;
    private ResourceLink permission;

    public ResourceLink getEntity() {
        return entity;
    }

    public void setEntity(ResourceLink entity) {
        this.entity = entity;
    }

    public ResourceLink getSource() {
        return source;
    }

    public void setSource(ResourceLink source) {
        this.source = source;
    }

    public ResourceLink getTheme() {
        return theme;
    }

    public void setTheme(ResourceLink theme) {
        this.theme = theme;
    }

    public ResourceLink getPermission() {
        return permission;
    }

    public void setPermission(ResourceLink permission) {
        this.permission = permission;
    }
}
