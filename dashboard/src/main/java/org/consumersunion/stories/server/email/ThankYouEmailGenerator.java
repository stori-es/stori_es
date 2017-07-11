package org.consumersunion.stories.server.email;

import javax.inject.Inject;

import org.consumersunion.stories.common.server.model.Verification;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.notification_channel.StoriesAddedEvent;
import org.consumersunion.stories.server.velocity.VelocityWrapper;
import org.consumersunion.stories.server.velocity.VelocityWrapperFactory;
import org.consumersunion.stories.server.velocity.template.TemplatesLocation;
import org.springframework.stereotype.Component;

@Component
public class ThankYouEmailGenerator {
    private static final String TEMPLATE = TemplatesLocation.emails("thankyou.vm");
    private static final String SUBJECT = "[stori.es] Thanks for taking action!";
    private static final String VERIFICATION_URL = "https://stori.es/verify-contact/";

    private final VelocityWrapperFactory velocityWrapperFactory;

    @Inject
    ThankYouEmailGenerator(VelocityWrapperFactory velocityWrapperFactory) {
        this.velocityWrapperFactory = velocityWrapperFactory;
    }

    public EmailData generate(
            StoriesAddedEvent event,
            Contact contact,
            Organization organization,
            Verification verification) {
        VelocityWrapper velocityWrapper = velocityWrapperFactory.create(TEMPLATE);

        velocityWrapper.put("contact", contact);
        velocityWrapper.put("organization", organization);
        velocityWrapper.put("verificationLink", VERIFICATION_URL + verification.getNonce());

        String body = velocityWrapper.generate();

        return new EmailData(SUBJECT, body, contact.getValue(), event.getProfile().getFullName());
    }
}
