package org.consumersunion.stories.common.shared.dto.post;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.consumersunion.stories.common.shared.dto.ApiBlock;
import org.consumersunion.stories.common.shared.dto.DocumentType;
import org.consumersunion.stories.common.shared.dto.ResourceLink;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentPost {
    @JsonProperty("document_type")
    @NotNull(message = "document_type cannot be null")
    private DocumentType documentType;
    private String title;
    private String byline;
    private List<ApiBlock> blocks;
    @JsonProperty("entity_id")
    private int entityId;
    @JsonProperty("theme_id")
    private int themeId;
    @JsonProperty("permission_id")
    private int permissionId;
    private ResourceLink source;

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

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public ResourceLink getSource() {
        return source;
    }

    public void setSource(ResourceLink source) {
        this.source = source;
    }

    public static class Builder {
        private DocumentType documentType;
        private String title;
        private String byline;
        private List<ApiBlock> blocks;
        private int entityId;
        private int themeId;
        private int permissionId;
        private ResourceLink source;

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

        public Builder withEntityId(int entityId) {
            this.entityId = entityId;
            return this;
        }

        public Builder withThemeId(int themeId) {
            this.themeId = themeId;
            return this;
        }

        public Builder withPermissionId(int permissionId) {
            this.permissionId = permissionId;
            return this;
        }

        public Builder withSource(ResourceLink source) {
            this.source = source;
            return this;
        }

        public DocumentPost build() {
            DocumentPost documentPost = new DocumentPost();
            documentPost.setDocumentType(documentType);
            documentPost.setTitle(title);
            documentPost.setByline(byline);
            documentPost.setBlocks(blocks);
            documentPost.setEntityId(entityId);
            documentPost.setThemeId(themeId);
            documentPost.setPermissionId(permissionId);
            documentPost.setSource(source);

            return documentPost;
        }
    }
}
