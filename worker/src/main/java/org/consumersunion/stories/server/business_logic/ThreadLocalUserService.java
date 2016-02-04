package org.consumersunion.stories.server.business_logic;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.springframework.stereotype.Service;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ROOT_ID;

@Service
public class ThreadLocalUserService implements UserService {
    private final ProfilePersister profilePersister;
    private final CredentialedUserPersister credentialedUserPersister;
    private final ThreadLocal<User> userContainer = new ThreadLocal<User>();
    private final ThreadLocal<Integer> profileIdContainer = new ThreadLocal<Integer>();

    @Inject
    ThreadLocalUserService(
            ProfilePersister profilePersister,
            CredentialedUserPersister credentialedUserPersister) {
        this.profilePersister = profilePersister;
        this.credentialedUserPersister = credentialedUserPersister;
    }

    @Override
    public CredentialedUser getCredentialedUser(String handle) {
        return credentialedUserPersister.getByHandle(handle);
    }

    @Override
    public CredentialedUser getCredentialedUser(Integer id) {
        return credentialedUserPersister.get(id);
    }

    @Override
    public User getLoggedInUser() {
        return userContainer.get();
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
        profileIdContainer.set(id);
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
        Integer profileId = profileIdContainer.get();

        if (profileId != null) {
            return (Integer) profileId;
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
        userContainer.set(user);
    }

    @Override
    public User getUser(int id) {
        return credentialedUserPersister.get(id).getUser();
    }

    @Override
    public CredentialedUser createUser(CredentialedUser credentialedUser) {
        return credentialedUser;
    }

    @Override
    public CredentialedUser updateUser(CredentialedUser credentialedUser) {
        return credentialedUser;
    }

    @Override
    public void deleteUser(int id) {
        throw new UnsupportedOperationException();
    }
}
