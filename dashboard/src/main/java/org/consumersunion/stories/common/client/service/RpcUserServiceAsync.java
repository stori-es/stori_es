package org.consumersunion.stories.common.client.service;

import java.util.List;

import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.SortField;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of {@link RpcUserService}.
 */
public interface RpcUserServiceAsync {
    void login(final String handle, String password, AsyncCallback<DatumResponse<User>> response);

    void logout(AsyncCallback<ActionResponse> callback);

    void registerLocale(String locale, AsyncCallback<ActionResponse> callback);

    void createAccount(User newUser, String password, String resetQuestion, String resetAnswer, String givenName,
            String surname, List<String> organizationIds, String defaultOrg,
            AsyncCallback<ActionResponse> callback);

    void getLoggedInUser(AsyncCallback<DatumResponse<User>> callback);

    void getUser(String handle, AsyncCallback<DatumResponse<User>> callback);

    void updateAccount(User user, AsyncCallback<DatumResponse<User>> callback);

    void updatePassword(String currentPassword, String newPassword, AsyncCallback<ActionResponse> callback);

    void updateUserAndPassword(String newPassword, CredentialedUser user, String givenName, String surname,
            List<String> organizations,
            String defaultOrg, AsyncCallback<ActionResponse> callback);

    void getUser(int id, AsyncCallback<DatumResponse<User>> callback);

    void checkHandle(String username, AsyncCallback<ActionResponse> callback);

    void checkAssignableHandle(String username, AsyncCallback<ActionResponse> callback);

    void getUsersByPermissionMask(int start, int length, SortField sortField, boolean ascending, int permissionMask,
            AsyncCallback<PagedDataResponse<CredentialedUser>> callback);

    void changeAccountStatus(CredentialedUser user, AsyncCallback<DatumResponse<CredentialedUser>> callback);
}
