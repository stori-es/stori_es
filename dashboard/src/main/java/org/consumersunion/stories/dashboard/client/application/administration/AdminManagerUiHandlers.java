package org.consumersunion.stories.dashboard.client.application.administration;

import java.util.List;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.SortField;

import com.gwtplatform.mvp.client.UiHandlers;

public interface AdminManagerUiHandlers extends UiHandlers {
    void loadUsers(int start, int length, SortField sort, boolean ascending);

    void loadOrganizationThemes(Organization organization);

    void loadAdminOrganizations(int start, int length, SortField sort, boolean ascending);

    void checkOrganizationDisponibility(String organizationName);

    void checkUsernameExists(String userName);

    void updateUserAccount(String newPassword, CredentialedUser user, String givenName, String surname,
            List<String> organizations,
            String value);

    void saveNewAccount(User user, String userPassword, String userResetQuestion, String userResetAnswer,
            String givenName, String surname, List<String> organizations, String defaultOrg);

    void createOrganization(Organization org, List<Contact> contacts, List<Address> addresses,
            List<String> adminUserNames,
            List<Integer> themes);

    void updateOrganization(Organization org, List<Contact> contacts, List<Address> addresses, List<Integer> themes,
            List<String> adminUsers);

    void checkAdminNameExists(String userName);

    void updateAccountStatus(CredentialedUser user);
}
