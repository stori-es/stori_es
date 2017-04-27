package org.consumersunion.stories.common.client.service;

import java.util.List;

import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.server.business_logic.UserService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * User services.
 */
@RemoteServiceRelativePath("service/user")
public interface RpcUserService extends RemoteService {
    DatumResponse<User> login(final String handle, String password);

    ActionResponse logout();

    /**
     * Sets the current user's locale preference. There are no restrictions on
     * this method.
     */
    ActionResponse registerLocale(String locale);

    /**
     * Creates a new {@link User} account a {@link Profile} for the organizations.
     * The current Organization context will be the default {@link Profile}.
     * The current {@link User}'s active {@link Profile} must have ADMIN privs over the {@link Organization}.
     */
    ActionResponse createAccount(User newUser, String password, String resetQuestion, String resetAnswer,
            String givenName, String surname, List<String> organizationIds, String defaultOrg);

    /**
     * @see UserService#getLoggedInUser()
     */
    DatumResponse<User> getLoggedInUser();

    /**
     * Retrieves the specified user. Logged in user must be root for this operation.
     */
    DatumResponse<User> getUser(String handle);

    /**
     * Retrieves the specified user.Logged in user must be root for this operation.
     */
    DatumResponse<User> getUser(int id);

    /**
     * Updates the specified {@link User}. Logged in {@link User} must be same or
     * root.
     */
    DatumResponse<User> updateAccount(User user);

    /**
     * Updates the current logged in user passwords. Only works on the
     * authenticated user so requires no special authorizations.
     */
    ActionResponse updatePassword(String currentPassword, String newPassword);

    /**
     * Updates a {@link User} data and password. Logged in {@link User} must root
     * or target of operation.
     */
    ActionResponse updateUserAndPassword(String newPassword, CredentialedUser user,
            String givenName, String surname, List<String> organizations,
            String defaultOrg);

    /**
     * Checks whether a given handle is available. Requires no special
     * authorization.
     */
    ActionResponse checkHandle(String username);

    /**
     * Checks whether a given handle is available to be assigned as admin. Requires no special
     * authorization.
     */
    ActionResponse checkAssignableHandle(String username);

    /**
     * Browse users over which the effective user has the indicated privilege.
     */
    PagedDataResponse<CredentialedUser> getUsersByPermissionMask(int start, int length, SortField sortField,
            boolean ascending, int permissionMask);

    DatumResponse<CredentialedUser> changeAccountStatus(CredentialedUser user);
}
