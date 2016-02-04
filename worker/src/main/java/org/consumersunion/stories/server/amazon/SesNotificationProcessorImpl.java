package org.consumersunion.stories.server.amazon;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.entity.ContactStatus;
import org.consumersunion.stories.server.amazon.model.Bounce;
import org.consumersunion.stories.server.amazon.model.Complaint;
import org.consumersunion.stories.server.amazon.model.HasEmailAddress;
import org.consumersunion.stories.server.amazon.model.SesNotification;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class SesNotificationProcessorImpl implements SesNotificationProcessor {
    private final ContactPersister contactPersister;

    @Inject
    SesNotificationProcessorImpl(ContactPersister contactPersister) {
        this.contactPersister = contactPersister;
    }

    @Override
    public int process(List<SesNotification> notifications) {
        for (SesNotification notification : notifications) {
            Map<String, ContactStatus> contactStatus = getContactStatus(notification);
            contactPersister.updateEmailContactStatus(contactStatus);
        }

        return notifications.size();
    }

    private Map<String, ContactStatus> getContactStatus(SesNotification notification) {
        Map<String, ContactStatus> contactsStatus = Maps.newHashMap();
        switch (notification.getNotificationType()) {
            case BOUNCE:
                Bounce bounce = notification.getBounce();
                putAll(contactsStatus, bounce.getBouncedRecipients(), getBouncedContactStatus(bounce));
                break;
            case COMPLAINT:
                Complaint complaint = notification.getComplaint();
                putAll(contactsStatus, complaint.getComplainedRecipients(), ContactStatus.COMPLAINTS);
                break;
            case DELIVERY:
                ContactStatus verifiedStatus = ContactStatus.VERIFIED;
                for (String email : notification.getDelivery().getRecipients()) {
                    contactsStatus.put(email, verifiedStatus);
                }
                break;
            default:
                throw new IllegalArgumentException(
                        notification.getNotificationType().name() + " is an unexpected type");
        }

        return contactsStatus;
    }

    private void putAll(Map<String, ContactStatus> contactsStatus,
            List<? extends HasEmailAddress> recipients,
            ContactStatus contactStatus) {
        for (HasEmailAddress hasEmailAddress : recipients) {
            contactsStatus.put(hasEmailAddress.getEmailAddress(), contactStatus);
        }
    }

    private ContactStatus getBouncedContactStatus(Bounce bounce) {
        switch (bounce.getBounceType()) {
            case PERMANENT:
                return ContactStatus.PERM_BOUNCED;
            case TRANSIENT:
                return ContactStatus.BOUNCED;
            default:
                return ContactStatus.BOUNCED;
        }
    }
}
