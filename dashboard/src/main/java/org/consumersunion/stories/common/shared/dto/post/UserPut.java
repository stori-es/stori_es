package org.consumersunion.stories.common.shared.dto.post;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.UserResourceLinks;
import org.hibernate.validator.constraints.NotEmpty;

public class UserPut {
    @NotNull(message = "handle must not be null")
    @NotEmpty(message = "handle must not be empty")
    private String handle;
    private List<ApiContact> contacts;
    private UserResourceLinks links;

    public static Builder builder() {
        return new Builder();
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public List<ApiContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<ApiContact> contacts) {
        this.contacts = contacts;
    }

    public UserResourceLinks getLinks() {
        return links;
    }

    public void setLinks(UserResourceLinks links) {
        this.links = links;
    }

    public static class Builder {
        private String handle;
        private List<ApiContact> contacts;
        private UserResourceLinks links;

        private Builder() {
        }

        public Builder withHandle(String handle) {
            this.handle = handle;
            return this;
        }

        public Builder withContacts(List<ApiContact> contacts) {
            this.contacts = contacts;
            return this;
        }

        public Builder withLinks(UserResourceLinks links) {
            this.links = links;
            return this;
        }

        public UserPut build() {
            UserPut userPost = new UserPut();
            userPost.setHandle(handle);
            userPost.setContacts(contacts);
            userPost.setLinks(links);

            return userPost;
        }
    }
}
