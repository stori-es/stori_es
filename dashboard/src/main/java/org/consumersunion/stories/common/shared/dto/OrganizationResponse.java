package org.consumersunion.stories.common.shared.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class OrganizationResponse extends BasicResponse<OrganizationResourceLinks> {
    public static class Builder extends AbstractBuilder<Builder, OrganizationResponse, OrganizationResourceLinks> {
        private String name;
        private String shortName;
        private List<ApiContact> contacts;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return self();
        }

        public Builder withShortName(String shortName) {
            this.shortName = shortName;
            return self();
        }

        public Builder withContacts(List<ApiContact> contacts) {
            this.contacts = contacts;
            return self();
        }

        @Override
        public OrganizationResponse build() {
            OrganizationResponse response = super.build();

            response.setName(name);
            response.setShortName(shortName);
            response.setContacts(contacts);

            return response;
        }

        @Override
        protected OrganizationResponse createBasicResponse() {
            return new OrganizationResponse();
        }
    }

    private final List<ApiContact> contacts;

    private String name;
    @JsonProperty("short_name")
    private String shortName;
    private OrganizationResourceLinks links;

    public OrganizationResponse() {
        contacts = Lists.newArrayList();
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
        this.contacts.clear();
        this.contacts.addAll(contacts);
    }

    @Override
    public OrganizationResourceLinks getLinks() {
        return links;
    }

    @Override
    public void setLinks(OrganizationResourceLinks links) {
        this.links = links;
    }
}
