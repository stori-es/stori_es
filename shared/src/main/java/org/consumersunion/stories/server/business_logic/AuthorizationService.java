package org.consumersunion.stories.server.business_logic;

import java.sql.Connection;
import java.util.List;

import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.exception.NotLoggedInException;

public interface AuthorizationService {
    AuthorizationService withConnection(Connection connection);

    boolean isUserAuthorized(int operation, SystemEntity target) throws NotLoggedInException;

    boolean isUserAuthorized(int operation, int target) throws NotLoggedInException;

    /**
     * Indicates whether the indicated principal is granted (or denied) the operation over the target. An authenticated
     * user may call this method with themselves as principal. This allows clients to determine whether to render
     * certain options. To ask about other principals other than the authenticated user, the authenticated user must
     * have 'admin' privileges over the system, or or 'admin' privileges over the target or principal.
     */
    boolean isEntityAuthorized(int profileId, int operation, SystemEntity target);

    boolean isSuperUser(User user);

    boolean isSuperUser();

    boolean hasMinRole(int profileId, int minRole, int target);

    /**
     * Allows authorized user to grant permissions. The granting of authorization requires the authenticated user has
     * the 'grant' operation over the target entity.
     */
    void grant(int subject, int role, int target);

    /**
     * Like {@link #grant(int, int, int)} except that any existing role is left intact if it is superior to the
     * indicated minimum role.
     */
    void grantAtLeast(int subject, int minRole, int target);

    /**
     * Allows authorized users to explicitly deny authorization. The granting of authorization requires the
     * authenticated user has the 'grant' operation over the target entity. Generally, a denial is realized by removing
     * a grant, though in some cases it may result in an explicit denial, for example, to override a group grant.
     */
    void deny(Integer subject, Integer target);

    /**
     * Given a principal and operation, returns a list of all the valid targets. Authenticated user may call this method
     * with themselves as principal. To call this method with an entity other than the authenticated user, the user must
     * have admin privileges over the system or the principal. The 'joinTable' is used to limit the results to a single
     * type and should be the name of the appropriate SQL table.
     * TODO: nice to uses class instead, but this would require an intervening mapping mechanism we don't have time to
     * worry about it right now.
     */
    List<SystemEntity> getValidTargets(int subject, int operation, String joinTable);

    /**
     * Given an operation and target, returns a list of all the valid targets. The authenticated user must have admin
     * privileges over the system or target in order to make this call.
     */
    List<SystemEntity> getValidPrincipals(int minRole, int target, String joinTable);

    boolean canRead(SystemEntity systemEntity) throws NotLoggedInException;

    boolean hasMinRoleOnStory(int profileId, int minRole, int target);

    boolean hasMinRoleOnProfile(int organizationId, int minRole, Profile profile);

    boolean canWrite(SystemEntity systemEntity) throws NotLoggedInException;

    boolean canWrite(Integer entityId);
}
