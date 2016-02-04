package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.google.common.collect.Lists;

public class UserResponse extends BasicResponse<UserResourceLinks> {
    public static class Builder extends AbstractBuilder<Builder, UserResponse, UserResourceLinks> {
        private String handle;
        private List<ApiContact> contacts;

        @Override
        protected UserResponse createBasicResponse() {
            return new UserResponse();
        }

        public Builder withHandle(String handle) {
            this.handle = handle;

            return self();
        }

        public Builder withContacts(List<ApiContact> contacts) {
            this.contacts = contacts;
            return self();
        }

        @Override
        public UserResponse build() {
            UserResponse response = super.build();
            response.setHandle(handle);
            response.setContacts(contacts);

            return response;
        }
    }

    private String handle;
    private List<ApiContact> contacts;
    private UserResourceLinks links;

    public UserResponse() {
        contacts = Lists.newArrayList();
    }

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
        this.contacts.clear();
        if (contacts != null) {
            this.contacts.addAll(contacts);
        }
    }

    @Override
    public UserResourceLinks getLinks() {
        return links;
    }

    @Override
    public void setLinks(UserResourceLinks links) {
        this.links = links;
    }
}
