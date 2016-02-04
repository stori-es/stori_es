package org.consumersunion.stories.common.shared.dto.post;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StoryPost extends AbstractEntityData {
    @JsonProperty("content_ids")
    private Set<Integer> contentIds;
    @JsonProperty("default_content_id")
    private Integer defaultContentId;
    @JsonProperty("response_ids")
    private Set<Integer> responseIds;
    @JsonProperty("permission_ids")
    private Set<Integer> permissionIds;
    @JsonProperty("note_ids")
    private Set<Integer> noteIds;
    @JsonProperty("attachment_ids")
    private Set<Integer> attachmentIds;
    @JsonProperty("collection_ids")
    private Set<Integer> collectionIds;

    public static Builder builder() {
        return new Builder();
    }

    public Set<Integer> getContentIds() {
        return contentIds;
    }

    public void setContentIds(Set<Integer> contentIds) {
        this.contentIds = contentIds;
    }

    public Integer getDefaultContentId() {
        return defaultContentId;
    }

    public void setDefaultContentId(Integer defaultContentId) {
        this.defaultContentId = defaultContentId;
    }

    public Set<Integer> getResponseIds() {
        return responseIds;
    }

    public void setResponseIds(Set<Integer> responseIds) {
        this.responseIds = responseIds;
    }

    public Set<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(Set<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public Set<Integer> getNoteIds() {
        return noteIds;
    }

    public void setNoteIds(Set<Integer> noteIds) {
        this.noteIds = noteIds;
    }

    public Set<Integer> getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(Set<Integer> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public Set<Integer> getCollectionIds() {
        return collectionIds;
    }

    public void setCollectionIds(Set<Integer> collectionIds) {
        this.collectionIds = collectionIds;
    }

    public static class Builder extends AbstractEntityDataBuilder<Builder> {
        private Set<Integer> contentIds;
        private Integer defaultContentId;
        private Set<Integer> responseIds;
        private Set<Integer> permissionIds;
        private Set<Integer> noteIds;
        private Set<Integer> attachmentIds;
        private Set<Integer> collectionIds;

        private Builder() {
        }

        public Builder withContentIds(Set<Integer> contentIds) {
            this.contentIds = contentIds;
            return this;
        }

        public Builder withDefaultContentId(Integer defaultContentId) {
            this.defaultContentId = defaultContentId;
            return this;
        }

        public Builder withResponseIds(Set<Integer> responseIds) {
            this.responseIds = responseIds;
            return this;
        }

        public Builder withPermissionIds(Set<Integer> permissionIds) {
            this.permissionIds = permissionIds;
            return this;
        }

        public Builder withNoteIds(Set<Integer> noteIds) {
            this.noteIds = noteIds;
            return this;
        }

        public Builder withAttachmentIds(Set<Integer> attachmentIds) {
            this.attachmentIds = attachmentIds;
            return this;
        }

        public Builder withCollectionIds(Set<Integer> collectionIds) {
            this.collectionIds = collectionIds;
            return this;
        }

        public StoryPost build() {
            StoryPost storyPost = buildBase();
            storyPost.setContentIds(contentIds);
            storyPost.setDefaultContentId(defaultContentId);
            storyPost.setResponseIds(responseIds);
            storyPost.setPermissionIds(permissionIds);
            storyPost.setNoteIds(noteIds);
            storyPost.setAttachmentIds(attachmentIds);
            storyPost.setCollectionIds(collectionIds);

            return storyPost;
        }

        @Override
        protected StoryPost createEmpty() {
            return new StoryPost();
        }
    }
}
