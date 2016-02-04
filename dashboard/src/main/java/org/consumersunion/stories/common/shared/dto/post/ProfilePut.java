package org.consumersunion.stories.common.shared.dto.post;

import java.util.List;

import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.ProfileResourceLinks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfilePut {
    private ProfileResourceLinks links;
    @JsonProperty("given_name")
    private String givenName;
    private String surname;
    private List<ApiContact> contacts;

    public static Builder builder() {
        return new Builder();
    }

    public ProfileResourceLinks getLinks() {
        return links;
    }

    public void setLinks(ProfileResourceLinks links) {
        this.links = links;
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
        private ProfileResourceLinks links;
        private String givenName;
        private String surname;
        private List<ApiContact> contacts;

        private Builder() {
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

        public Builder withLinks(ProfileResourceLinks links) {
            this.links = links;
            return this;
        }

        public ProfilePut build() {
            ProfilePut profilePut = new ProfilePut();
            profilePut.setLinks(links);
            profilePut.setGivenName(givenName);
            profilePut.setSurname(surname);
            profilePut.setContacts(contacts);

            return profilePut;
        }
    }
}
