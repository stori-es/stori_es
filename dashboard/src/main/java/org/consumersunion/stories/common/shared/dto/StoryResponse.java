package org.consumersunion.stories.common.shared.dto;

import java.util.Set;

public class StoryResponse extends BasicResponse<StoryResourceLinks> {
    public static class Builder extends AbstractBuilder<Builder, StoryResponse, StoryResourceLinks> {
        private Set<String> tags;

        public Builder withTags(Set<String> tags) {
            this.tags = tags;
            return this;
        }

        @Override
        protected StoryResponse createBasicResponse() {
            return new StoryResponse();
        }

        @Override
        public StoryResponse build() {
            StoryResponse storyResponse = super.build();
            storyResponse.setTags(tags);

            return storyResponse;
        }
    }

    private StoryResourceLinks links;
    private Set<String> tags;

    @Override
    public StoryResourceLinks getLinks() {
        return links;
    }

    @Override
    public void setLinks(StoryResourceLinks links) {
        this.links = links;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
