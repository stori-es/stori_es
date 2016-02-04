package org.consumersunion.stories.server.business_logic;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.consumersunion.stories.common.server.model.Verification;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.ContactStatus;
import org.consumersunion.stories.server.email.EmailData;
import org.consumersunion.stories.server.email.ThankYouEmailGenerator;
import org.consumersunion.stories.server.notification_channel.StoriesAddedEvent;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.consumersunion.stories.server.persistence.OrganizationPersister;
import org.consumersunion.stories.server.persistence.VerificationPersister;
import org.consumersunion.stories.server.util.UniqueIdGenerator;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@Service
public class VerificationServiceImpl implements VerificationService {
    private static final Logger LOGGER = Logger.getLogger(VerificationServiceImpl.class.getName());

    private final EmailService emailService;
    private final VerificationPersister verificationPersister;
    private final ContactPersister contactPersister;
    private final OrganizationPersister organizationPersister;
    private final ThankYouEmailGenerator thankYouEmailGenerator;
    private final UniqueIdGenerator uniqueIdGenerator;

    @Inject
    VerificationServiceImpl(
            EventBus eventBus,
            EmailService emailService,
            VerificationPersister verificationPersister,
            ContactPersister contactPersister,
            OrganizationPersister organizationPersister,
            ThankYouEmailGenerator thankYouEmailGenerator,
            UniqueIdGenerator uniqueIdGenerator) {
        this.emailService = emailService;
        this.verificationPersister = verificationPersister;
        this.contactPersister = contactPersister;
        this.organizationPersister = organizationPersister;
        this.thankYouEmailGenerator = thankYouEmailGenerator;
        this.uniqueIdGenerator = uniqueIdGenerator;

        eventBus.register(this);
    }

    @Override
    public Verification create(Contact contact) {
        int profileId = contact.getEntityId();
        String email = contact.getValue();
        String nonce = uniqueIdGenerator.get();

        return verificationPersister.create(new Verification(profileId, email, nonce));
    }

    @Override
    public Verification get(String code) {
        return verificationPersister.get(code);
    }

    @Override
    public void delete(String code) {
        verificationPersister.delete(code);
    }

    @Subscribe
    public void onStoryAdded(StoriesAddedEvent event) {
        try {
            if (event.getProfile() != null && event.getPrimaryEmail() != null) {
                Contact contact = contactPersister.retrieveEmailContact(event.getProfile().getId(),
                        event.getPrimaryEmail());
                if (contact != null && ContactStatus.UNVERIFIED.equals(contact.getStatus())) {
                    try {
                        Verification verification = create(contact);

                        EmailData emailData = generateVerificationEmail(event, contact, verification);

                        emailService.sendEmail(emailData);
                    } catch (DuplicateKeyException e) {
                        LOGGER.info(e.getMessage());
                    }
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.severe(e.getMessage());
            throw e;
        }
    }

    private EmailData generateVerificationEmail(StoriesAddedEvent event, Contact contact, Verification verification) {
        Organization organization = organizationPersister.get(event.getSystemEntity().getOwner());

        return thankYouEmailGenerator.generate(event, contact, organization, verification);
    }
}
