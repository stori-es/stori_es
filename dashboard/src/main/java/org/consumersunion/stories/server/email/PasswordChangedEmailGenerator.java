package org.consumersunion.stories.server.email;

import javax.inject.Inject;

import org.consumersunion.stories.server.velocity.VelocityWrapper;
import org.consumersunion.stories.server.velocity.VelocityWrapperFactory;
import org.consumersunion.stories.server.velocity.template.TemplatesLocation;
import org.springframework.stereotype.Component;

@Component
public class PasswordChangedEmailGenerator {
    private static final String TEMPLATE = TemplatesLocation.emails("passwordchanged.vm");
    private static final String SUBJECT = "[stori.es] Your password has changed";

    private final VelocityWrapperFactory velocityWrapperFactory;

    @Inject
    PasswordChangedEmailGenerator(VelocityWrapperFactory velocityWrapperFactory) {
        this.velocityWrapperFactory = velocityWrapperFactory;
    }

    public EmailData generate(String email, String firstName) {
        VelocityWrapper velocityWrapper = velocityWrapperFactory.create(TEMPLATE);

        String body = velocityWrapper.generate();

        return new EmailData(SUBJECT, body, email, firstName);
    }
}
