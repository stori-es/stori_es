package org.consumersunion.stories.common.shared.dto.post;

import org.consumersunion.stories.common.shared.dto.CollectionResourceLinks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CollectionPut extends AbstractEntityData {
    private CollectionResourceLinks links;
    @JsonProperty("is_published")
    private Boolean published;

    public static Builder builder() {
        return new Builder();
    }

    public CollectionResourceLinks getLinks() {
        return links;
    }

    public void setLinks(CollectionResourceLinks links) {
        this.links = links;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public static class Builder extends AbstractEntityDataBuilder<Builder> {
        private CollectionResourceLinks links;
        private Boolean published;

        private Builder() {
        }

        public Builder withLinks(CollectionResourceLinks links) {
            this.links = links;
            return this;
        }

        public Builder isPublished(boolean published) {
            this.published = published;
            return this;
        }

        public CollectionPut build() {
            CollectionPut collectionPut = buildBase();
            collectionPut.setLinks(links);
            collectionPut.setPublished(published);

            return collectionPut;
        }

        @Override
        protected CollectionPut createEmpty() {
            return new CollectionPut();
        }
    }
}
