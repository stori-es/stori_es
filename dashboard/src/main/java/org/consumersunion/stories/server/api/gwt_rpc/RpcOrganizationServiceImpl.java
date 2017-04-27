package org.consumersunion.stories.server.api.gwt_rpc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcOrganizationService;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.business_logic.ContactService;
import org.consumersunion.stories.server.business_logic.OrganizationService;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister;
import org.consumersunion.stories.server.persistence.OrganizationPersister;
import org.consumersunion.stories.server.persistence.OrganizationPersister.RetrievePagedOrganizationsParams;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.UserPersister;
import org.consumersunion.stories.server.security.BaseAuthParam;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;

import net.lightoze.gwt.i18n.server.LocaleFactory;
import net.lightoze.gwt.i18n.server.LocaleProxy;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.common.shared.AuthConstants.ROOT_ID;
import static org.consumersunion.stories.server.persistence.OrganizationPersister.RetrieveByPermalinkFunc;
import static org.consumersunion.stories.server.persistence.OrganizationPersister.getOrganizationColumnsForRetrieval;
import static org.consumersunion.stories.server.persistence.ProfilePersister.ProfileIdByUserAndOrgFunc;
import static org.consumersunion.stories.server.persistence.ProfilePersister.RetrieveProfileForOrganizationFunc;
import static org.consumersunion.stories.server.persistence.ProfilePersister.RetrieveProfileSummaryFunc;
import static org.consumersunion.stories.server.persistence.ProfilePersister.UserOrgTuple;

/**
 * The service side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Service("organizationService")
public class RpcOrganizationServiceImpl extends RpcBaseServiceImpl implements RpcOrganizationService {
    static {
        if ("true".equals(System.getProperty("org.consumersunion.testMode"))) {
            LocaleProxy.initialize();
        }
    }

    @Inject
    private OrganizationPersister organizationPersister;
    @Inject
    private OrganizationService organizationService;
    @Inject
    private UserPersister userPersister;
    @Inject
    private CredentialedUserPersister credentialedUserPersister;
    @Inject
    private ContactService contactService;

    @Override
    public ActionResponse createOrganization(
            Organization organization,
            List<Contact> contacts,
            List<Address> addresses,
            List<String> adminUsers,
            List<Integer> themes) {
        ActionResponse response = new ActionResponse();

        try {
            organizationService.create(organization, contacts, addresses, themes, adminUsers);
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }
        return response;
    }

    @Override
    public DatumResponse<OrganizationSummary> updateOrganization(
            Organization organization,
            List<Contact> contacts,
            List<Address> addresses,
            List<Integer> themes,
            List<String> adminUsers) {
        DatumResponse<OrganizationSummary> response = new DatumResponse<OrganizationSummary>();
        try {
            try {
                Organization updated = organizationService.update(organization, contacts, addresses, themes);

                if (adminUsers != null) {
                    List<Integer> currentAdmins = grantCurrentAdmins(updated, adminUsers);
                    revokeOtherAdminGrants(updated, currentAdmins);
                }

                response.setDatum(processOrganization(updated));
            } catch (NotAuthorizedException ignored) {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            } catch (Exception e) {
                e.printStackTrace();
                response.addGlobalErrorMessage(
                        LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    private List<Integer> grantCurrentAdmins(Organization targetOrganization, List<String> adminUsers) {
        List<Integer> currentAdmins = new LinkedList<Integer>();
        for (String adminUser : adminUsers) {
            if (!Strings.isNullOrEmpty(adminUser)) {
                CredentialedUser admin = credentialedUserPersister.getByHandle(adminUser);
                ProfileSummary profileSummary = persistenceService.process(
                        new RetrieveProfileForOrganizationFunc(targetOrganization.getId(), admin.getId()));
                if (profileSummary != null) {
                    Profile profile = profileSummary.getProfile();
                    currentAdmins.add(profile.getId());
                    authService.grantAtLeast(profile.getId(), ROLE_ADMIN, targetOrganization.getId());
                }
            }
        }

        return currentAdmins;
    }

    /**
     * Clears all admins from the {@link Organization} (an admin being one with read, write, and
     *
     * @param myOrganization
     */
    private void revokeOtherAdminGrants(Organization myOrganization, List<Integer> currentAdmins) {
        // See: https://consumersunion.atlassian.net/browse/TASK-1421
        OrganizationSummary summary = processOrganization(myOrganization);

        for (User adminUser : summary.getAdmins().values()) {
            UserOrgTuple userOrgTuple = new UserOrgTuple(adminUser.getId(), summary.getOrganizationId());
            Integer adminProfileId = persistenceService.process(new ProfileIdByUserAndOrgFunc(userOrgTuple));
            if (adminProfileId != null) {
                if (!currentAdmins.contains(adminProfileId)) {
                    authService.grant(adminProfileId, ROLE_READER, myOrganization.getId());
                }
            }
        }
    }

    @Override
    public DatumResponse<Organization> retrieveOrganization(int id) {
        DatumResponse<Organization> response = new DatumResponse<Organization>();

        try {
            Organization organization = organizationService.get(id);
            if (organization != null) {
                response.setDatum(organization);
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            response.setLoggedIn(false);
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }

        return response;
    }

    @Override
    public DatumResponse<Organization> retrieveOrganizationByPermalink(String permalink) {
        DatumResponse<Organization> response = new DatumResponse<Organization>();
        Organization retrieved = persistenceService.process(new RetrieveByPermalinkFunc(permalink));
        try {
            if (authService.isUserAuthorized(ROLE_READER, retrieved)) {
                response.setDatum(retrieved);
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }
        return response;
    }

    @Override
    public PagedDataResponse<OrganizationSummary> getAdminOrganizations(
            int start,
            int length,
            SortField sortField,
            boolean ascending) {
        PagedDataResponse<OrganizationSummary> response = new PagedDataResponse<OrganizationSummary>();
        try {
            response.setStart(start);
            User user = userService.getLoggedInUser(true);
            if (getEffectiveSubject(user) == ROOT_ID) {
                response.setStart(0);
                Connection conn = PersistenceUtil.getConnection();
                try {
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT " + getOrganizationColumnsForRetrieval("o", "se", "e") + " " +
                                    "FROM systemEntity se JOIN entity e ON se.id=e.id JOIN organization o ON se.id=o" +
                                    ".id " +
                                    "LIMIT ? OFFSET ?");
                    ps.setInt(1, length);
                    ps.setInt(2, start);
                    ResultSet rs = ps.executeQuery();

                    List<Organization> organizations = new LinkedList<Organization>();
                    while (rs.next()) {
                        organizations.add(OrganizationPersister.processRetrieval(rs));
                    }

                    response.setStart(start);
                    response.setData(processOrganizations(organizations));

                    ResultSet countRs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM organization");
                    if (countRs.next()) {
                        response.setTotalCount(countRs.getInt(1));
                    }
                } finally {
                    conn.close();
                }
            } else {
                BaseAuthParam params;

                params = new BaseAuthParam(start, length, sortField, ascending,
                        ACCESS_MODE_EXPLICIT, getEffectiveSubject(user));

                int count = persistenceService.process(
                        organizationPersister.getOrganizationCount(params, BasePermission.ADMINISTRATION.getMask()));

                RetrievePagedOrganizationsParams orgParams = new RetrievePagedOrganizationsParams(start, length, null,
                        true, params.getAuthRelation(), params.getEffectiveId(),
                        userService.getContextOrganizationId());
                List<Organization> organizations =
                        persistenceService.process(organizationPersister.retrieveAdminOrganizationsFunc(orgParams));

                response.setData(processOrganizations(organizations));
                // Why is the count being set to '1' here? Noticed this while working on unrelated issue.
                if (count > 0 || authService.isSuperUser(user)) {
                    response.setTotalCount(count);
                } else {
                    response.setTotalCount(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }
        return response;
    }

    private List<OrganizationSummary> processOrganizations(List<Organization> organizations) {
        List<OrganizationSummary> organizationSummaries = new ArrayList<OrganizationSummary>();
        if (organizations != null) {
            for (Organization org : organizations) {
                organizationSummaries.add(processOrganization(org));
            }
        }

        return organizationSummaries;
    }

    private OrganizationSummary processOrganization(Organization organization) {
        OrganizationSummary organizationSummary = new OrganizationSummary(organization);
        loadOrganizationAdminProfiles(organizationSummary);

        List<Address> addresses = contactService.getAddresses(organization);
        if (addresses != null && !addresses.isEmpty()) {
            organizationSummary.setAddress(addresses.get(0));
        }

        List<Contact> contacts = contactService.getAllContacts(organization);

        if (contacts != null) {
            List<Contact> emails = getEmails(contacts);
            List<Contact> phones = getPhones(contacts);
            if (!emails.isEmpty()) {
                organizationSummary.setEmailContact(emails.get(0));
            }
            if (!phones.isEmpty()) {
                organizationSummary.setPhoneContact(phones.get(0));
            }
        }

        return organizationSummary;
    }

    private List<Contact> getEmails(List<Contact> contacts) {
        return filterContacts(contacts, Contact.MediumType.EMAIL);
    }

    private List<Contact> getPhones(List<Contact> contacts) {
        return filterContacts(contacts, Contact.MediumType.PHONE);
    }

    private List<Contact> filterContacts(List<Contact> contacts, final Contact.MediumType mediumType) {
        return FluentIterable.from(contacts).filter(new Predicate<Contact>() {
            @Override
            public boolean apply(Contact input) {
                return mediumType.name().equals(input.getMedium());
            }
        }).toList();
    }

    private void loadOrganizationAdminProfiles(OrganizationSummary org) {
        org.setAdmins(new HashMap<Integer, User>());
        List<SystemEntity> adminEntities =
                authService.getValidPrincipals(ROLE_ADMIN, org.getOrganizationId(), "profile");

        Connection conn = PersistenceUtil.getConnection();
        try {
            if (!adminEntities.isEmpty()) {
                Integer index = 0;
                for (SystemEntity adminEntity : adminEntities) {
                    ProfileSummary profileSummary =
                            persistenceService.process(new RetrieveProfileSummaryFunc(adminEntity.getId()));
                    // Skip root Profiles.
                    if (profileSummary.getProfile().getUserId() != ROOT_ID) {
                        User adminUser = userPersister.get(profileSummary.getProfile().getUserId());
                        // Why are be building a map with sequential keys? Why not just a list?
                        org.getAdmins().put(index, adminUser);
                        index++;
                    }
                }
            }
        } catch (Exception e) {
            throw new GeneralException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public ActionResponse checkOrganizationAvailability(String name) {
        ActionResponse response = new ActionResponse();
        try {
            User user = userService.getLoggedInUser(true);
            if (authService.isSuperUser(user) || userService.getLoggedInUser(true) != null) {
                if (Strings.isNullOrEmpty(name)
                        || !persistenceService.process(organizationPersister.checkOrganizationNameAvailability(name))) {
                    response.addGlobalErrorMessage(
                            LocaleFactory.get(CommonI18nErrorMessages.class).invalidOrganizationName());
                }
                response.setShowErrorMessages(true);
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (NotLoggedInException e) {
            response.setLoggedIn(false);
            throw new GeneralException(e);
        }

        return response;
    }
}
