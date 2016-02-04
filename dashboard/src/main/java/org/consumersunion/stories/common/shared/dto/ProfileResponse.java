package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class ProfileResponse extends BasicResponse<ProfileResourceLinks> {
    public static class Builder extends AbstractBuilder<Builder, ProfileResponse, ProfileResourceLinks> {
        private String givenName;
        private String surname;
        private List<ApiContact> contacts;

        private Builder() {
        }

        public Builder withGivenName(String givenName) {
            this.givenName = givenName;
            return self();
        }

        public Builder withSurname(String surname) {
            this.surname = surname;
            return self();
        }

        public Builder withContacts(List<ApiContact> contacts) {
            this.contacts = contacts;
            return self();
        }

        @Override
        public ProfileResponse build() {
            ProfileResponse response = super.build();

            response.setGivenName(givenName);
            response.setSurname(surname);
            response.setContacts(contacts);

            return response;
        }

        @Override
        protected ProfileResponse createBasicResponse() {
            return new ProfileResponse();
        }
    }

    private final List<ApiContact> contacts;

    @JsonProperty("given_name")
    private String givenName;
    private String surname;
    private ProfileResourceLinks links;

    public ProfileResponse() {
        contacts = Lists.newArrayList();
    }

    public static Builder builder() {
        return new Builder();
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
        this.contacts.clear();
        this.contacts.addAll(contacts);
    }

    @Override
    public ProfileResourceLinks getLinks() {
        return links;
    }

    @Override
    public void setLinks(ProfileResourceLinks links) {
        this.links = links;
    }
}
