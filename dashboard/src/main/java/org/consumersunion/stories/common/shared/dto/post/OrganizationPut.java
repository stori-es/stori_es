package org.consumersunion.stories.common.shared.dto.post;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.OrganizationResourceLinks;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class OrganizationPut {
    private List<ApiContact> contacts;
    private OrganizationResourceLinks links;
    @NotNull(message = "name should not be null")
    @NotEmpty(message = "name should not be empty")
    private String name;
    @JsonProperty("short_name")
    @Length(max = 15, message = "short_name should not be longer than 15 characters")
    private String shortName;

    public OrganizationPut() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<ApiContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<ApiContact> contacts) {
        if (contacts != null) {
            this.contacts = ensureList(this.contacts);
            this.contacts.addAll(contacts);
        }
    }

    public OrganizationResourceLinks getLinks() {
        return links;
    }

    public void setLinks(OrganizationResourceLinks links) {
        this.links = links;
    }

    private <T> List<T> ensureList(List<T> list) {
        if (list == null) {
            list = Lists.newArrayList();
        }

        return list;
    }

    public static class Builder {
        private List<ApiContact> contacts;
        private String name;
        private String shortName;
        private OrganizationResourceLinks links;

        private Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public Builder setContacts(List<ApiContact> contacts) {
            this.contacts = contacts;
            return this;
        }

        public Builder setLinks(OrganizationResourceLinks links) {
            this.links = links;
            return this;
        }

        public OrganizationPut build() {
            OrganizationPut organizationPut = new OrganizationPut();
            organizationPut.setName(name);
            organizationPut.setShortName(shortName);
            organizationPut.setContacts(contacts);
            organizationPut.setLinks(links);

            return organizationPut;
        }
    }
}
