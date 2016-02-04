package org.consumersunion.stories.common.client.service.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.consumersunion.stories.common.shared.model.entity.Contact;

public class ContactResponse extends Response {

    private Map<String, List<Contact>> contacts;
    private List<String> contactMediums;

    public ContactResponse() {
        contacts = null;
        contactMediums = null;
    }

    public void setContacts(Map<String, List<Contact>> contacts) {
        this.contacts = contacts;
        if (contacts != null) {
            for (String key : contacts.keySet()) {
                Collections.sort(contacts.get(key));
            }

            setContactMediums();
        }
    }

    public List<Contact> getContacts(final String medium) {
        if (contacts != null) {
            return contacts.get(medium);
        }
        return null;
    }

    private void setContactMediums() {
        if (contactMediums == null) {
            contactMediums = new ArrayList<String>(contacts.keySet().size());
        } else {
            contactMediums.clear();
        }

        for (final String key : contacts.keySet()) {
            contactMediums.add(key);
        }
    }

    public List<String> getContactMediums() {
        return contactMediums;
    }

    public boolean containsMedium(final String medium) {
        if (contacts == null) {
            return false;
        }

        return contacts.containsKey(medium);
    }
}
