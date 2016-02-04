package org.consumersunion.stories.server.security.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class SecurityLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public SecurityLoginSuccessHandler() {
        super();
    }

    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {
        request.getSession().removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        if ("true".equals(request.getHeader("x-ajax-call"))) {
            response.setContentType("text/plain");
            response.getWriter().print("/stories.jsp");
            response.getWriter().flush();
        } else {
            super.onAuthenticationSuccess(request, response, auth);
        }
    }
}
