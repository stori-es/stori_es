package org.consumersunion.stories.common.shared.dto.post;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.consumersunion.stories.common.shared.dto.ApiBlock;
import org.consumersunion.stories.common.shared.dto.ApiLocale;
import org.consumersunion.stories.common.shared.dto.DocumentType;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentPut {
    @NotNull(message = "document_type must not be null")
    @JsonProperty("document_type")
    private DocumentType documentType;
    @NotNull(message = "locale must not be null")
    private ApiLocale locale;
    private String title;
    private String byline;
    private List<ApiBlock> blocks;

    public static Builder builder() {
        return new Builder();
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
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

    public static class Builder {
        private DocumentType documentType;
        private String title;
        private String byline;
        private List<ApiBlock> blocks;
        private ApiLocale locale;

        private Builder() {
        }

        public Builder withDocumentType(DocumentType documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withByline(String byline) {
            this.byline = byline;
            return this;
        }

        public Builder withBlocks(List<ApiBlock> blocks) {
            this.blocks = blocks;
            return this;
        }

        public Builder withLocale(ApiLocale locale) {
            this.locale = locale;
            return this;
        }

        public DocumentPut build() {
            DocumentPut documentPut = new DocumentPut();
            documentPut.setDocumentType(documentType);
            documentPut.setTitle(title);
            documentPut.setByline(byline);
            documentPut.setBlocks(blocks);
            documentPut.setLocale(locale);

            return documentPut;
        }
    }
}
