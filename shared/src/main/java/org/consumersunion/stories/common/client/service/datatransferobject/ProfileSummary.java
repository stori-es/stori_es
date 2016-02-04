package org.consumersunion.stories.common.client.service.datatransferobject;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.entity.Contact;

/**
 * Encapsulates a {@link Profile} and related data. Any particular profile may contain contact plus address
 * information, the related organization name, or both depending on the calling context.
 */
public class ProfileSummary implements Serializable {
    private Profile profile;
    private String organizationName;
    private Boolean adminOverOrg;
    private String primaryEmail;
    private Set<String> emails;
    private String primaryPhone;
    private Set<String> phones;
    private List<Contact> contacts;

    /**
     * Serialization constructor; do not use.
     */
    public ProfileSummary() {
        this(null, null, false);
    }

    public ProfileSummary(Profile profile,
            String primaryEmail,
            Set<String> emails,
            String primaryPhone,
            Set<String> phones) {
        this.profile = profile;
        this.organizationName = null;
        this.adminOverOrg = null;
        this.primaryEmail = primaryEmail;
        this.emails = emails;
        this.primaryPhone = primaryPhone;
        this.phones = phones;
    }

    public ProfileSummary(Profile profile, String organizationName, boolean adminOverOrg) {
        this(profile, organizationName, adminOverOrg, null);
    }

    public ProfileSummary(Profile profile, String organizationName, boolean adminOverOrg, String primaryEmail) {
        this.profile = profile;
        this.organizationName = organizationName;
        this.adminOverOrg = adminOverOrg;
        this.primaryEmail = primaryEmail;
        this.emails = null;
        this.primaryPhone = null;
        this.phones = null;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public Boolean isAdminOverOrg() {
        return adminOverOrg;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public Set<String> getPhones() {
        return phones;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
