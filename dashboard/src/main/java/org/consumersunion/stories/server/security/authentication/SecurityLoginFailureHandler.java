package org.consumersunion.stories.server.security.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

public class SecurityLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        request.getSession().removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        if ("true".equals(request.getHeader("x-ajax-call"))) {
            response.setContentType("text/plain");

            if (exception.getClass().equals(BadCredentialsException.class) ||
                    exception.getClass().equals(UsernameNotFoundException.class)) {
                request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", exception);
                response.getWriter().print("error=true");
            } else if (exception.getClass().equals(SessionAuthenticationException.class)) {
                response.getWriter().print("max_session_error");
            }

            response.getWriter().flush();
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
