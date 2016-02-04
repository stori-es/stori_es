package org.consumersunion.stories.server.security.authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import com.google.common.base.Strings;

public class SessionAuthenticationStrategy extends ConcurrentSessionControlStrategy {
    private final SessionRegistry sessionRegistry;
    private final Map<String, Authentication> connectedSessions;

    public SessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
        super(sessionRegistry);

        this.sessionRegistry = sessionRegistry;
        connectedSessions = new HashMap<String, Authentication>();
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request,
            HttpServletResponse response) {
        if (sessionRegistry.getAllPrincipals().contains(authentication.getPrincipal())
                && Strings.isNullOrEmpty(request.getHeader("renew"))) {
            throw new SessionAuthenticationException("Multiple login attempt!");
        } else if (!Strings.isNullOrEmpty(request.getHeader("renew"))) {
            List<SessionInformation> sessions = sessionRegistry.
                    getAllSessions(authentication.getPrincipal(), false);
            if (sessions.size() > 0) {
                SessionInformation sessionInformation = sessions.get(0);
                sessionInformation.expireNow();
                connectedSessions.get(authentication.getPrincipal().toString()).setAuthenticated(false);
            }
        }
        connectedSessions.put(authentication.getPrincipal().toString(), authentication);
        super.onAuthentication(authentication, request, response);
    }
}
