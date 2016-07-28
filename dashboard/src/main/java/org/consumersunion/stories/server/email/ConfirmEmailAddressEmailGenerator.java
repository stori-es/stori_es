package org.consumersunion.stories.server.email;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import org.consumersunion.stories.common.server.model.Verification;
import org.consumersunion.stories.server.velocity.VelocityWrapper;
import org.consumersunion.stories.server.velocity.VelocityWrapperFactory;
import org.consumersunion.stories.server.velocity.template.TemplatesLocation;
import org.springframework.stereotype.Component;

@Component
public class ConfirmEmailAddressEmailGenerator {
    private static final String TEMPLATE = TemplatesLocation.emails("emailadded.vm");
    private static final String SUBJECT = "[stori.es] Confirm your email address";
    private static final String VERIFICATION_URL = "/confirm-email/";

    private final VelocityWrapperFactory velocityWrapperFactory;
    private Provider<HttpServletRequest> requestProvider;

    @Inject
    ConfirmEmailAddressEmailGenerator(
            VelocityWrapperFactory velocityWrapperFactory,
            Provider<HttpServletRequest> requestProvider) {
        this.velocityWrapperFactory = velocityWrapperFactory;
        this.requestProvider = requestProvider;
    }

    public EmailData generate(Verification verification, String fullName) {
        VelocityWrapper velocityWrapper = velocityWrapperFactory.create(TEMPLATE);

        String protocolAndHost = getProtocolAndHost();

        velocityWrapper.put("confirmLink", protocolAndHost + VERIFICATION_URL + verification.getNonce());

        String body = velocityWrapper.generate();

        return new EmailData(SUBJECT, body, verification.getEmail(), fullName);
    }

    private String getProtocolAndHost() {
        HttpServletRequest request = requestProvider.get();
        StringBuffer url = request.getRequestURL();

        return url.substring(0, url.indexOf(request.getRequestURI()));
    }
}
