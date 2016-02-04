package org.consumersunion.stories.server.business_logic;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpSession;

import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.consumersunion.stories.server.util.HttpSessionProvider;
import org.consumersunion.stories.server.util.SecurityContextProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ROOT_ID;

@Service
public class HttpSessionUserService implements UserService {
    public static final String PROFILE_ID = "profileId";

    private final ProfilePersister profilePersister;
    private final CredentialedUserPersister credentialedUserPersister;
    private final Provider<SecurityContext> securityContextProvider;
    private final Provider<HttpSession> sessionProvider;
    private final Provider<AuthorizationService> authorizationServiceProvider;

    @Inject
    HttpSessionUserService(
            ProfilePersister profilePersister,
            CredentialedUserPersister credentialedUserPersister,
            SecurityContextProvider securityContextProvider,
            HttpSessionProvider sessionProvider,
            Provider<AuthorizationService> authorizationServiceProvider) {
        this.profilePersister = profilePersister;
        this.credentialedUserPersister = credentialedUserPersister;
        this.securityContextProvider = securityContextProvider;
        this.sessionProvider = sessionProvider;
        this.authorizationServiceProvider = authorizationServiceProvider;
    }

    @Override
    public CredentialedUser getCredentialedUser(String handle) {
        CredentialedUser credentialedUser = credentialedUserPersister.getByHandle(handle);

        AuthorizationService authService = authorizationServiceProvider.get();
        if (credentialedUser != null
                && !authService.isUserAuthorized(AuthConstants.ROLE_READER, credentialedUser.getUser())) {
            throw new NotAuthorizedException();
        }

        return credentialedUser;
    }

    @Override
    public CredentialedUser getCredentialedUser(Integer id) {
        AuthorizationService authService = authorizationServiceProvider.get();
        if (!authService.isUserAuthorized(AuthConstants.ROLE_READER, id)) {
            throw new NotAuthorizedException();
        }

        return credentialedUserPersister.get(id);
    }

    @Override
    public User getLoggedInUser() {
        Authentication auth = securityContextProvider.get().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String handle;
            try {
                handle = (String) auth.getPrincipal();
            } catch (ClassCastException ignored) {
                handle = auth.getName();
            }

            if (handle != null) {
                CredentialedUser user = credentialedUserPersister.getByHandle(handle);
                if (user != null) {
                    return user.getUser();
                }
            }
        }

        return null;
    }

    @Override
    public User getLoggedInUser(boolean throwOnNull) throws NotLoggedInException {
        User user = getLoggedInUser();

        if (user == null && throwOnNull) {
            throw new NotLoggedInException();
        }

        return user;
    }

    @Override
    public void setActiveProfileId(int id) {
        HttpSession httpSession = sessionProvider.get();

        httpSession.setAttribute(PROFILE_ID, id);
    }

    @Override
    public Integer getContextOrganizationId() throws NotLoggedInException {
        Profile profile = profilePersister.get(getActiveProfileId());

        if (profile == null) {
            return null;
        } else {
            return profile.getOrganizationId();
        }
    }

    @Override
    public int getActiveProfileId() throws NotLoggedInException {
        HttpSession session = sessionProvider.get();

        if (session != null) {
            Object profileId = session.getAttribute(PROFILE_ID);

            if (profileId != null) {
                return (Integer) profileId;
            }
        }

        throw new NotLoggedInException(LocaleFactory.get(CommonI18nErrorMessages.class).notLoggedIn());
    }

    @Override
    public Integer getEffectiveSubject() {
        return getEffectiveSubject(getLoggedInUser());
    }

    @Override
    public Integer getEffectiveSubject(User user) {
        if (user != null && user.getId() == ROOT_ID) {
            return 0;
        } else {
            try {
                return getActiveProfileId();
            } catch (NotLoggedInException ignored) {
                return null;
            }
        }
    }

    @Override
    public User getUserForProfile(int profileId) {
        return credentialedUserPersister.getByProfileId(profileId).getUser();
    }

    @Override
    public void setLoggedInUser(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User getUser(int id) {
        AuthorizationService authorizationService = authorizationServiceProvider.get();

        CredentialedUser credentialedUser = credentialedUserPersister.get(id);
        if (credentialedUser == null) {
            throw new NotFoundException();
        }

        User user = credentialedUser.getUser();
        if (authorizationService.canRead(user)) {
            return user;
        }

        throw new NotAuthorizedException();
    }

    @Override
    public CredentialedUser createUser(CredentialedUser credentialedUser) {
        AuthorizationService authService = authorizationServiceProvider.get();
        if (!authService.isSuperUser()) {
            throw new NotAuthorizedException();
        }

        return credentialedUserPersister.createUser(credentialedUser);
    }

    @Override
    public CredentialedUser updateUser(CredentialedUser credentialedUser) {
        AuthorizationService authService = authorizationServiceProvider.get();
        if (!authService.isUserAuthorized(AuthConstants.ROLE_CURATOR, credentialedUser.getUser())) {
            throw new NotAuthorizedException();
        }

        return credentialedUserPersister.update(credentialedUser);
    }

    @Override
    public void deleteUser(int id) {
        AuthorizationService authService = authorizationServiceProvider.get();
        if (!authService.isUserAuthorized(AuthConstants.ROLE_ADMIN, id)) {
            throw new NotAuthorizedException();
        }

        CredentialedUser user = credentialedUserPersister.get(id);
        user.getUser().setActive(false);
        credentialedUserPersister.update(user);
    }
}
