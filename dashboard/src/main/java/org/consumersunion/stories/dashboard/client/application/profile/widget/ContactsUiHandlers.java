package org.consumersunion.stories.dashboard.client.application.profile.widget;

import java.util.List;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.entity.Contact;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ContactsUiHandlers extends UiHandlers {
    void saveSocials(List<Contact> contacts);

    void saveAddresses(int idx, List<Address> addresses);

    void saveAddresses(List<Address> addresses);

    void saveContact(String originalValue, Contact contact);

    void saveContacts(List<Contact> contacts);
}
