package org.consumersunion.stories.dashboard.client.application.profile.ui;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.dashboard.client.application.ui.EditorView;

public interface ContactEditorHandler {
    void onSaveContact(String originalContact, Contact contact);

    void onSaveSocial();

    void onSaveAddress(Address address);

    void removeContact(Contact contact);

    void removeSocial(Contact contact);

    void removeAddress(Address address);

    void onEdit(EditorView<?> editor);

    void onCancelAddress(Address address);
}
