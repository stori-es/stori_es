package org.consumersunion.stories.common.shared.dto;

public class PermissionResponse extends BasicResponse<PermissionRessourceLinks> {
    private PermissionRessourceLinks links;
    private String title;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public PermissionRessourceLinks getLinks() {
        return links;
    }

    @Override
    public void setLinks(PermissionRessourceLinks links) {
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class Builder extends AbstractBuilder<Builder, PermissionResponse, PermissionRessourceLinks> {
        private String title;

        private Builder() {
        }

        public Builder withTitle(String title) {
            this.title = title;
            return self();
        }

        @Override
        public PermissionResponse build() {
            PermissionResponse response = super.build();

            response.setTitle(title);

            return response;
        }

        @Override
        protected PermissionResponse createBasicResponse() {
            return new PermissionResponse();
        }
    }
}
