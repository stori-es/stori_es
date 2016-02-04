package org.consumersunion.stories.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.login.place.NameTokens;
import org.springframework.stereotype.Component;

@Component
public final class ForgotPasswordServlet extends SpringServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("/signin.jsp#" + NameTokens.forgot);
    }
}
