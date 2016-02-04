package org.consumersunion.stories.common.shared.dto;

public class CollectionResponse extends AbstractCollectionResponse<CollectionResourceLinks> {
    private CollectionResourceLinks links;

    @Override
    public CollectionResourceLinks getLinks() {
        return links;
    }

    @Override
    public void setLinks(CollectionResourceLinks links) {
        this.links = links;
    }
}
