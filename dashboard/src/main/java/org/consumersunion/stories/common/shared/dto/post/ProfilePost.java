package org.consumersunion.stories.common.shared.dto.post;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.consumersunion.stories.common.shared.dto.ApiContact;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfilePost {
    @JsonProperty("organization_id")
    @NotNull(message = "organization_id must not be null")
    private Integer organizationId;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("given_name")
    private String givenName;
    private String surname;
    private List<ApiContact> contacts;

    public static Builder builder() {
        return new Builder();
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<ApiContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<ApiContact> contacts) {
        this.contacts = contacts;
    }

    public static class Builder {
        private int organizationId;
        private Integer userId;
        private String givenName;
        private String surname;
        private List<ApiContact> contacts;

        protected Builder() {
        }

        public Builder withOrganizationId(int organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder withUserId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder withGivenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public Builder withSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder withContacts(List<ApiContact> contacts) {
            this.contacts = contacts;
            return this;
        }

        public ProfilePost build() {
            ProfilePost profilePost = new ProfilePost();
            profilePost.setOrganizationId(organizationId);
            profilePost.setUserId(userId);
            profilePost.setGivenName(givenName);
            profilePost.setSurname(surname);
            profilePost.setContacts(contacts);

            return profilePost;
        }
    }
}
