package org.consumersunion.stories.server.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.common.server.model.Verification;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.login.place.NameTokens;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.consumersunion.stories.server.persistence.VerificationPersister;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.EMAIL;
import static org.consumersunion.stories.common.shared.model.entity.Contact.TYPE_HOME;
import static org.consumersunion.stories.common.shared.model.entity.ContactStatus.VERIFIED;

@Component
public final class ConfirmEmailServlet extends SpringServlet {
    private static final Logger LOGGER = Logger.getLogger(ConfirmEmailServlet.class.getName());

    @Inject
    private VerificationPersister verificationPersister;
    @Inject
    private ContactPersister contactPersister;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher reqDispatcher;
        try {
            String code = Strings.nullToEmpty(request.getPathInfo()).replace("/", "");

            Verification verification = verificationPersister.get(code);
            if (verification != null) {
                Contact primaryEmail = new Contact(verification.getEntityId(), EMAIL.name(), TYPE_HOME,
                        verification.getEmail(), VERIFIED);

                contactPersister.saveContact(verification.getEntityId(), null, primaryEmail);

                response.sendRedirect("/signin.jsp#" + NameTokens.confirmSuccess);
                return;
            } else {
                reqDispatcher = get404Dispatcher();
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            reqDispatcher = get404Dispatcher();
        }

        reqDispatcher.forward(request, response);
    }

    private RequestDispatcher get404Dispatcher() {
        return getServletContext().getRequestDispatcher("/404page.jsp");
    }
}
