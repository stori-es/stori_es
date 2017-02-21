package org.consumersunion.stories.server.security.authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.server.persistence.UserPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import com.google.common.base.Strings;

public class SessionAuthenticationStrategy extends ConcurrentSessionControlStrategy {
    private final SessionRegistry sessionRegistry;
    private final UserPersister userPersister;
    private final Map<String, Authentication> connectedSessions;

    @Inject
    public SessionAuthenticationStrategy(
            SessionRegistry sessionRegistry,
            UserPersister userPersister) {
        super(sessionRegistry);

        this.sessionRegistry = sessionRegistry;
        this.userPersister = userPersister;
        connectedSessions = new HashMap<String, Authentication>();
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request,
            HttpServletResponse response) {
        String username = authentication.getPrincipal().toString();
        if (sessionRegistry.getAllPrincipals().contains(authentication.getPrincipal())
                && Strings.isNullOrEmpty(request.getHeader("renew"))) {
            throw new SessionAuthenticationException("Multiple login attempt!");
        } else if (!Strings.isNullOrEmpty(request.getHeader("renew"))) {
            List<SessionInformation> sessions = sessionRegistry.
                    getAllSessions(authentication.getPrincipal(), false);
            if (sessions.size() > 0) {
                SessionInformation sessionInformation = sessions.get(0);
                sessionInformation.expireNow();
                connectedSessions.get(username).setAuthenticated(false);
            }
        }

        userPersister.updateLastLogin(username);

        connectedSessions.put(username, authentication);
        super.onAuthentication(authentication, request, response);
    }
}
