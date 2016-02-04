package org.consumersunion.stories.server.email;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.ResetPassword;
import org.consumersunion.stories.server.velocity.VelocityWrapper;
import org.consumersunion.stories.server.velocity.VelocityWrapperFactory;
import org.consumersunion.stories.server.velocity.template.TemplatesLocation;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordEmailGenerator {
    private static final String TEMPLATE = TemplatesLocation.emails("resetpassword.vm");
    private static final String SUBJECT = "[stori.es] Resetting your password";
    private static final String VERIFICATION_URL = "https://stori.es/reset-password/";

    private final VelocityWrapperFactory velocityWrapperFactory;

    @Inject
    ResetPasswordEmailGenerator(VelocityWrapperFactory velocityWrapperFactory) {
        this.velocityWrapperFactory = velocityWrapperFactory;
    }

    public EmailData generate(String email, ResetPassword resetPassword, String firstName) {
        VelocityWrapper velocityWrapper = velocityWrapperFactory.create(TEMPLATE);

        velocityWrapper.put("resetLink", VERIFICATION_URL + resetPassword.getNonce());

        String body = velocityWrapper.generate();

        return new EmailData(SUBJECT, body, email, firstName);
    }
}
