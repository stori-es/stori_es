package org.consumersunion.stories.server.business_logic;

import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.exception.NotLoggedInException;

public interface UserService {
    CredentialedUser getCredentialedUser(String handle);

    CredentialedUser getCredentialedUser(Integer id);

    /**
     * Retrieves the currently logged in user (or null) associated with the
     * user's session. There are no restrictions on this method.
     */
    User getLoggedInUser();

    int getActiveProfileId() throws NotLoggedInException;

    User getLoggedInUser(boolean dieOnNull) throws NotLoggedInException;

    void setActiveProfileId(int id);

    Integer getContextOrganizationId();

    Integer getEffectiveSubject();

    Integer getEffectiveSubject(User user);

    User getUserForProfile(int profileId);

    void setLoggedInUser(User user);

    User getUser(int id);

    CredentialedUser createUser(CredentialedUser credentialedUser);

    CredentialedUser updateUser(CredentialedUser credentialedUser);

    void deleteUser(int id);
}
