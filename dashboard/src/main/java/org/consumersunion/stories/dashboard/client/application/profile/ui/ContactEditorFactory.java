package org.consumersunion.stories.dashboard.client.application.profile.ui;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.entity.Contact;

public interface ContactEditorFactory {
    AddressEditor createAddress(Address address);

    ContactEditor createContact(Contact contact);

    SocialEditor createSocialContact(Contact contact);
}
