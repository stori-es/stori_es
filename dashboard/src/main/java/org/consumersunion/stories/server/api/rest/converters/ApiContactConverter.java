package org.consumersunion.stories.server.api.rest.converters;

import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.ContactType;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.Contact.MediumType;
import org.springframework.stereotype.Component;

@Component
public class ApiContactConverter extends AbstractConverter<ApiContact, Contact> {
    @Override
    public Contact convert(ApiContact contact) {
        Contact result = new Contact();
        MediumType mediumType = toMediumType(contact);

        if (mediumType != null) {
            result.setMedium(mediumType.name());
            result.setType(toContactType(contact));
            result.setValue(contact.getValue());

            return result;
        }

        return null;
    }

    private MediumType toMediumType(ApiContact contact) {
        ContactType type = contact.getContactType();
        String title = contact.getTitle();

        if (ContactType.SOCIAL == type) {
            return MediumType.valueOf(title);
        } else if (ContactType.EMAIL == type) {
            return MediumType.EMAIL;
        } else if (ContactType.PHONE == type) {
            return MediumType.PHONE;
        }

        return null;
    }

    private String toContactType(ApiContact contact) {
        ContactType type = contact.getContactType();
        String title = contact.getTitle();

        if (type == ContactType.EMAIL || type == ContactType.PHONE) {
            if (Contact.TYPE_HOME.equals(title) || Contact.TYPE_WORK.equals(title) || Contact.TYPE_MOBILE.equals(title)
                    || Contact.TYPE_OTHER.equals(title)) {
                return title;
            }
        } else if (ContactType.SOCIAL == contact.getContactType()) {
            return Contact.SOCIAL;
        }

        return null;
    }
}
