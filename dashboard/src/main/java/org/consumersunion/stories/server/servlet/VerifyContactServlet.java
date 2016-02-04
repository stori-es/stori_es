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
import org.consumersunion.stories.common.shared.model.entity.ContactStatus;
import org.consumersunion.stories.server.business_logic.VerificationService;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public final class VerifyContactServlet extends SpringServlet {
    private static final Logger LOGGER = Logger.getLogger(VerifyContactServlet.class.getName());

    @Inject
    private VerificationService verificationService;
    @Inject
    private ContactPersister contactPersister;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher reqDispacher;
        try {
            String code = Strings.nullToEmpty(request.getPathInfo()).replace("/", "");

            Verification verification = verificationService.get(code);
            if (verification != null) {
                contactPersister.setContactStatus(verification.getEntityId(), verification.getEmail(),
                        ContactStatus.VERIFIED);
                verificationService.delete(verification.getNonce());

                reqDispacher = getServletContext().getRequestDispatcher("/verifyContact.jsp");
            } else {
                reqDispacher = get404Dispatcher();
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            reqDispacher = get404Dispatcher();
        }

        reqDispacher.forward(request, response);
    }

    private RequestDispatcher get404Dispatcher() {
        return getServletContext().getRequestDispatcher("/404page.jsp");
    }
}
