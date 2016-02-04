package org.consumersunion.stories.server.business_logic;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.persistence.OrganizationPersister;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationPersister organizationPersister;
    private final AuthorizationService authorizationService;
    private final ContactService contactService;
    private final UserService userService;
    private final ProfileService profileService;
    private final ThemeService themeService;

    @Inject
    OrganizationServiceImpl(
            OrganizationPersister organizationPersister,
            AuthorizationService authorizationService,
            ContactService contactService,
            UserService userService,
            ProfileService profileService,
            ThemeService themeService) {
        this.organizationPersister = organizationPersister;
        this.authorizationService = authorizationService;
        this.contactService = contactService;
        this.userService = userService;
        this.profileService = profileService;
        this.themeService = themeService;
    }

    @Override
    public Organization get(int id) {
        Organization organization = organizationPersister.get(id);

        if (!authorizationService.canRead(organization)) {
            throw new NotAuthorizedException();
        }

        return organization;
    }

    @Override
    public void delete(int id) {
        Organization organization = organizationPersister.get(id);

        if (!authorizationService.isUserAuthorized(ROLE_ADMIN, organization)) {
            throw new NotAuthorizedException();
        }

        organizationPersister.delete(organization);
    }

    @Override
    public Organization update(
            Organization organization,
            List<Contact> contacts,
            List<Address> addresses,
            List<Integer> themes) {
        if (!authorizationService.isUserAuthorized(AuthConstants.ROLE_ADMIN, organization)) {
            throw new NotAuthorizedException();
        }

        Organization updated = organizationPersister.update(organization);
        if (contacts != null && !contacts.isEmpty()) {
            for (Contact contact : contacts) {
                contact.setEntityId(updated.getId());
            }
            contactService.saveContacts(updated.getId(), contacts);
        }

        if (addresses != null && !addresses.isEmpty()) {
            for (Address address : addresses) {
                address.setEntity(updated.getId());
            }
            contactService.saveAddresses(updated, addresses);
        }

        if (themes != null && !themes.isEmpty()) {
            themeService.associateOrganisation(organization, themes);
        }

        updated = organizationPersister.get(organization.getId());

        return updated;
    }

    @Override
    public Organization create(
            Organization organization,
            List<Contact> contacts,
            List<Address> addresses,
            List<Integer> themeIds,
            List<String> adminUsers) {
        User user = userService.getLoggedInUser(true);

        if (organization != null && authorizationService.isSuperUser(user)) {
            Organization savedOrganization = organizationPersister.create(organization);

            if (contacts != null && !contacts.isEmpty()) {
                contactService.saveContacts(savedOrganization, contacts);
            }

            if (addresses != null && !addresses.isEmpty()) {
                contactService.saveAddresses(savedOrganization, addresses);
            }

            if (adminUsers != null) {
                for (String adminUser : adminUsers) {
                    grantAdminRole(savedOrganization, adminUser);
                }
            }

            if (themeIds != null && !themeIds.isEmpty()) {
                themeService.associateOrganisation(savedOrganization, themeIds);
            }

            return savedOrganization;
        }

        throw new NotAuthorizedException();
    }

    private void grantAdminRole(Organization organization, String adminHandle) {
        if (!Strings.isNullOrEmpty(adminHandle)) {
            int organizationId = organization.getId();
            CredentialedUser admin = userService.getCredentialedUser(adminHandle);
            ProfileSummary profileSummary = profileService.getForOrganization(admin, organization);

            if (profileSummary == null) {
                User user = admin.getUser();

                Profile profile = new Profile();
                profile.setOrganizationId(organizationId);
                profile.setUserId(user.getId());

                ProfileSummary defaultProfile = profileService.get(user.getDefaultProfile());
                if (defaultProfile != null) {
                    profile.setGivenName(defaultProfile.getProfile().getGivenName());
                    profile.setSurname(defaultProfile.getProfile().getSurname());
                }

                profileSummary = profileService.createProfile(profile);
            }

            int profileId = profileSummary.getProfile().getId();
            authorizationService.grant(profileId, ROLE_ADMIN, organizationId);
        }
    }
}
