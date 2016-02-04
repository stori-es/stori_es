package org.consumersunion.stories.common.shared.dto;

import java.util.List;

public abstract class ResourceLinks {
    private ResourceLink creator;
    private ResourceLink owner;
    private List<ResourceLink> administrators;
    private List<ResourceLink> curators;
    private List<ResourceLink> readers;

    public ResourceLink getCreator() {
        return creator;
    }

    public void setCreator(ResourceLink creator) {
        this.creator = creator;
    }

    public ResourceLink getOwner() {
        return owner;
    }

    public void setOwner(ResourceLink owner) {
        this.owner = owner;
    }

    public List<ResourceLink> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<ResourceLink> administrators) {
        this.administrators = administrators;
    }

    public List<ResourceLink> getCurators() {
        return curators;
    }

    public void setCurators(List<ResourceLink> curators) {
        this.curators = curators;
    }

    public List<ResourceLink> getReaders() {
        return readers;
    }

    public void setReaders(List<ResourceLink> readers) {
        this.readers = readers;
    }
}
