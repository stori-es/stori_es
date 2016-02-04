package org.consumersunion.stories.common.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiContact {
    @JsonProperty("contact_type")
    private ContactType contactType;
    private String title;
    private String value;
    private Location location;

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
