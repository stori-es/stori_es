package org.consumersunion.stories.server.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.common.shared.model.ResetPassword;
import org.consumersunion.stories.login.place.NameTokens;
import org.consumersunion.stories.server.persistence.ResetPasswordPersister;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public final class ResetPasswordServlet extends SpringServlet {
    private static final Logger LOGGER = Logger.getLogger(ResetPasswordServlet.class.getName());

    @Inject
    private ResetPasswordPersister resetPasswordPersister;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher reqDispatcher;
        try {
            String code = Strings.nullToEmpty(request.getPathInfo()).replace("/", "");

            ResetPassword resetPassword = resetPasswordPersister.getByCode(code);
            if (resetPassword != null) {
                response.sendRedirect("/signin.jsp#" + NameTokens.reset + ";" + NameTokens.code + "=" + code);
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
