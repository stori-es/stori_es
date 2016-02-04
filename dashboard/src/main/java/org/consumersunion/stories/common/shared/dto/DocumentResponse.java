package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentResponse extends BasicResponse<DocumentResourceLinks> {
    public static class Builder extends AbstractBuilder<Builder, DocumentResponse, DocumentResourceLinks> {
        private String title;
        private DocumentType documentType;
        private List<ApiBlock> blocks;
        private ApiLocale locale;

        @Override
        protected DocumentResponse createBasicResponse() {
            return new DocumentResponse();
        }

        @Override
        public DocumentResponse build() {
            DocumentResponse response = super.build();
            response.setTitle(title);
            response.setDocumentType(documentType);
            response.setBlocks(blocks);
            response.setLocale(locale);

            return response;
        }

        public DocumentResponse.Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public DocumentResponse.Builder withDocumentType(DocumentType documentType) {
            this.documentType = documentType;
            return this;
        }

        public DocumentResponse.Builder withBlocks(List<ApiBlock> blocks) {
            this.blocks = blocks;
            return this;
        }

        public DocumentResponse.Builder withLocale(ApiLocale locale) {
            this.locale = locale;
            return this;
        }
    }

    private String title;
    @JsonProperty("document_type")
    private DocumentType documentType;
    private List<ApiBlock> blocks;
    private DocumentResourceLinks links;
    private ApiLocale locale;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    @Override
    public DocumentResourceLinks getLinks() {
        return links;
    }

    @Override
    public void setLinks(DocumentResourceLinks links) {
        this.links = links;
    }

    public List<ApiBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<ApiBlock> blocks) {
        this.blocks = blocks;
    }

    public ApiLocale getLocale() {
        return locale;
    }

    public void setLocale(ApiLocale locale) {
        this.locale = locale;
    }
}
