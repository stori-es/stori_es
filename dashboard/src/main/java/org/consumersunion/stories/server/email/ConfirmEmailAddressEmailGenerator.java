package org.consumersunion.stories.server.email;

import javax.inject.Inject;

import org.consumersunion.stories.common.server.model.Verification;
import org.consumersunion.stories.server.velocity.VelocityWrapper;
import org.consumersunion.stories.server.velocity.VelocityWrapperFactory;
import org.consumersunion.stories.server.velocity.template.TemplatesLocation;
import org.springframework.stereotype.Component;

@Component
public class ConfirmEmailAddressEmailGenerator {
    private static final String TEMPLATE = TemplatesLocation.emails("emailadded.vm");
    private static final String SUBJECT = "[stori.es] Confirm your email address";
    private static final String VERIFICATION_URL = "https://stori.es/confirm-email/";

    private final VelocityWrapperFactory velocityWrapperFactory;

    @Inject
    ConfirmEmailAddressEmailGenerator(VelocityWrapperFactory velocityWrapperFactory) {
        this.velocityWrapperFactory = velocityWrapperFactory;
    }

    public EmailData generate(Verification verification, String fullName) {
        VelocityWrapper velocityWrapper = velocityWrapperFactory.create(TEMPLATE);

        velocityWrapper.put("confirmLink", VERIFICATION_URL + verification.getNonce());

        String body = velocityWrapper.generate();

        return new EmailData(SUBJECT, body, verification.getEmail(), fullName);
    }
}
