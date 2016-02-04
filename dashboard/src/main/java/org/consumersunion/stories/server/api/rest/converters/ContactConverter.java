package org.consumersunion.stories.server.api.rest.converters;

import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.ContactType;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.Contact.MediumType;
import org.springframework.stereotype.Component;

@Component
public class ContactConverter extends AbstractConverter<Contact, ApiContact> {
    @Override
    public ApiContact convert(Contact contact) {
        MediumType mediumType = getMediumType(contact);
        ApiContact result = new ApiContact();

        if (mediumType != null) {
            result.setTitle(getApiContactTitle(contact, mediumType));
            result.setContactType(toApiContactType(mediumType));
        }

        result.setValue(contact.getValue());

        return result;
    }

    private MediumType getMediumType(Contact contact) {
        MediumType mediumType;
        try {
            mediumType = MediumType.valueOf(contact.getMedium());
        } catch (IllegalArgumentException e) {
            mediumType = null;
        }
        return mediumType;
    }

    private String getApiContactTitle(Contact contact, MediumType mediumType) {
        if (mediumType == MediumType.PHONE || mediumType == MediumType.EMAIL) {
            return contact.getType();
        } else {
            return mediumType.asString();
        }
    }

    private ContactType toApiContactType(MediumType medium) {
        if (medium.isSocialMedium()) {
            return ContactType.SOCIAL;
        } else if (MediumType.EMAIL == medium) {
            return ContactType.EMAIL;
        } else if (MediumType.PHONE == medium) {
            return ContactType.PHONE;
        }

        return null;
    }
}
