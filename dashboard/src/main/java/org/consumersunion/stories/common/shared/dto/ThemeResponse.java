package org.consumersunion.stories.common.shared.dto;

public class ThemeResponse extends BasicResponse<ThemeResourceLinks> {
    public static class Builder extends AbstractBuilder<Builder, ThemeResponse, ThemeResourceLinks> {
        private String title;
        private boolean archived;

        private Builder() {
        }

        public Builder withTitle(String title) {
            this.title = title;
            return self();
        }

        public Builder isArchived(boolean archived) {
            this.archived = archived;
            return self();
        }

        @Override
        public ThemeResponse build() {
            ThemeResponse response = super.build();
            response.setTitle(title);
            response.setArchived(archived);

            return response;
        }

        @Override
        protected ThemeResponse createBasicResponse() {
            return new ThemeResponse();
        }
    }

    private String title;
    private boolean archived;
    private ThemeResourceLinks links;

    public static Builder builder() {
        return new Builder();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    @Override
    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public ThemeResourceLinks getLinks() {
        return links;
    }

    @Override
    public void setLinks(ThemeResourceLinks links) {
        this.links = links;
    }
}
