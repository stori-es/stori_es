package org.consumersunion.stories.common.shared.dto;

public class ResourceLink {
    private String href;

    public ResourceLink(String href) {
        this.href = href;
    }

    ResourceLink() {
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
