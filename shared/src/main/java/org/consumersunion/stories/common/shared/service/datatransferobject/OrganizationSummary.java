package org.consumersunion.stories.common.shared.service.datatransferobject;

import java.util.HashMap;
import java.util.Map;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrganizationSummary implements IsSerializable {
    private Organization organization;
    private String admin;
    private Address address;
    private Contact emailContact;
    private Contact phoneContact;
    private boolean adminRequest;
    private Map<Integer, User> admins;

    public OrganizationSummary() {
        organization = null;
        admin = "";
    }

    public OrganizationSummary(Organization organization) {
        this();

        this.organization = organization;
    }

    public OrganizationSummary(Organization organization, Address address) {
        this();

        this.address = address;
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public int getOrganizationId() {
        return organization.getId();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Contact getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(Contact emailContact) {
        this.emailContact = emailContact;
    }

    public Contact getPhoneContact() {
        return phoneContact;
    }

    public void setPhoneContact(Contact phoneContact) {
        this.phoneContact = phoneContact;
    }

    public boolean isAdminRequest() {
        return adminRequest;
    }

    public void setAdminRequest(boolean adminRequest) {
        this.adminRequest = adminRequest;
    }

    public Map<Integer, User> getAdmins() {
        return admins;
    }

    public void setAdmins(HashMap<Integer, User> admins) {
        this.admins = admins;
    }
}
