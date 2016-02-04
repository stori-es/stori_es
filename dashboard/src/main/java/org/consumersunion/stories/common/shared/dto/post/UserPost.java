package org.consumersunion.stories.common.shared.dto.post;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPost {
    @NotNull(message = "handle must not be null")
    @NotEmpty(message = "handle must not be empty")
    private String handle;
    @JsonProperty("profile_ids")
    private List<Integer> profileIds;
    @JsonProperty("default_profile_id")
    @NotNull(message = "default_profile_id must not be null")
    private Integer defaultProfileId;
    private List<ApiContact> contacts;

    public static Builder builder() {
        return new Builder();
    }

    public List<Integer> getProfileIds() {
        return profileIds;
    }

    public void setProfileIds(List<Integer> profileIds) {
        this.profileIds = profileIds;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Integer getDefaultProfileId() {
        return defaultProfileId;
    }

    public void setDefaultProfileId(Integer defaultProfileId) {
        this.defaultProfileId = defaultProfileId;
    }

    public List<ApiContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<ApiContact> contacts) {
        this.contacts = contacts;
    }

    public static class Builder {
        private String handle;
        private List<Integer> profileIds;
        private Integer defaultProfileId;
        private List<ApiContact> contacts;

        private Builder() {
        }

        public Builder withHandle(String handle) {
            this.handle = handle;
            return this;
        }

        public Builder withProfileIds(List<Integer> profileIds) {
            this.profileIds = profileIds;
            return this;
        }

        public Builder withDefaultProfileId(Integer defaultProfileId) {
            this.defaultProfileId = defaultProfileId;
            return this;
        }

        public Builder withContacts(List<ApiContact> contacts) {
            this.contacts = contacts;
            return this;
        }

        public UserPost build() {
            UserPost userPost = new UserPost();
            userPost.setHandle(handle);
            userPost.setContacts(contacts);
            userPost.setProfileIds(profileIds);
            userPost.setDefaultProfileId(defaultProfileId);

            return userPost;
        }
    }
}
