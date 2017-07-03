package org.consumersunion.stories.server.api.gwt_rpc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcUserService;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.business_logic.AuthorizationService;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister.UserProfileStruct;
import org.consumersunion.stories.server.persistence.OrganizationPersister;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.consumersunion.stories.server.persistence.UserPersister;
import org.consumersunion.stories.server.security.BaseAuthParam;
import org.consumersunion.stories.server.security.authentication.BCrypt;
import org.consumersunion.stories.server.security.authentication.CustomUserAuthentication;
import org.consumersunion.stories.server.util.HttpSessionProvider;
import org.consumersunion.stories.server.util.SecurityContextProvider;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

import net.lightoze.gwt.i18n.server.LocaleFactory;
import net.lightoze.gwt.i18n.server.LocaleProxy;
import net.lightoze.gwt.i18n.server.ThreadLocalLocaleProvider;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ROOT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.server.persistence.CredentialedUserPersister.GetUserCount;
import static org.consumersunion.stories.server.persistence.CredentialedUserPersister.RetrieveUsersFunc;
import static org.consumersunion.stories.server.persistence.ProfilePersister.RetrieveProfileForOrganizationFunc;

/**
 * The service side implementation of the RPC service.
 */
@Service("userService")
public class RpcUserServiceImpl extends RpcBaseServiceImpl implements RpcUserService {
    static {
        if ("true".equals(System.getProperty("org.consumersunion.testMode"))) {
            LocaleProxy.initialize();
        }
    }

    @Inject
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Inject
    private HttpSessionProvider sessionProvider;
    @Inject
    private CredentialedUserPersister credentialedUserPersister;
    @Inject
    private UserPersister userPersister;
    @Inject
    private SecurityContextProvider securityContextProvider;
    @Inject
    private ProfilePersister profilePersister;

    @VisibleForTesting
    @Override
    public DatumResponse<User> login(String handle, String password) {
        CredentialedUser user = credentialedUserPersister.getByHandle(handle, false);
        DatumResponse<User> response = new DatumResponse<User>();
        if (BCrypt.checkpw(password, user.getPasswordHash())) {
            SecurityContext securityContext = securityContextProvider.get();
            securityContext.setAuthentication(
                    new UsernamePasswordAuthenticationToken(handle, "password"));

            Authentication customAuthentication =
                    new CustomUserAuthentication(securityContext.getAuthentication());
            customAuthentication.setAuthenticated(true);
            securityContext.setAuthentication(customAuthentication);

            sessionProvider.get().setAttribute("profileId", user.getUser().getDefaultProfile());

            response.setDatum(user.getUser());
        } else {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }

        return response;
    }

    @VisibleForTesting
    @Override
    public ActionResponse logout() {
        SecurityContextHolder.clearContext();
        return new ActionResponse();
    }

    @VisibleForTesting
    @Override
    public ActionResponse registerLocale(String localeCode) {
        Locale locale = new Locale(localeCode);
        sessionProvider.get().setAttribute("locale", locale);
        ThreadLocalLocaleProvider.pushLocale(locale);

        return new ActionResponse();
    }

    @Override
    public ActionResponse createAccount(
            final User newUser,
            final String password,
            final String resetQuestion,
            final String resetAnswer,
            final String givenName,
            final String surname,
            final List<String> stringOrganizationIds,
            final String defaultOrg) {
        ActionResponse response = new ActionResponse();

        try {
            Connection[] connection = new Connection[1];
            try {
                final Connection conn = persistenceService.getConnection();
                connection[0] = conn;

                final AuthorizationService authService = this.authService.withConnection(conn);
                final Integer organizationId = Integer.valueOf(defaultOrg);
                Organization targetObj = persistenceService.process(conn,
                        new OrganizationPersister.RetrieveOrganizationFunc(organizationId));

                if (!authService.isUserAuthorized(ROLE_ADMIN, targetObj)) {
                    String message = LocaleFactory.get(CommonI18nErrorMessages.class)
                            .noAdministrativeRights() + " for those organizations";
                    response.addGlobalErrorMessage(message);
                    return response;
                }

                if (newUser != null) {
                    Runnable backgroundTask = new Runnable() {
                        @Override
                        public void run() {
                            // The default org is granted in the create, so we remove that from our list of 'other orgs'.
                            try {
                                stringOrganizationIds.remove(defaultOrg);
                                List<Integer> organizationIds = FluentIterable.from(stringOrganizationIds)
                                        .transform(Ints.stringConverter()).toList();

                                newUser.setActive(false);
                                UserProfileStruct savedUser = createAccountInternal(conn, newUser, password, resetQuestion,
                                        resetAnswer, givenName, surname, organizationId);
                                for (Integer orgId : organizationIds) {
                                    Profile profile = new Profile();
                                    profile.setGivenName(givenName);
                                    profile.setSurname(surname);
                                    profile.setOrganizationId(orgId);
                                    profile.setUserId(savedUser.credentialedUser.getId());
                                    profile = profilePersister.createProfile(profile, conn);

                                    authService.grantAtLeast(profile.getId(), ROLE_READER, orgId);
                                    authService.grantAtLeast(orgId, ROLE_ADMIN, profile.getId());
                                }

                                User user = savedUser.credentialedUser.getUser();
                                user.setActive(true);
                                persistenceService.process(conn, new UserPersister.UpdateUserFunc(user));
                                conn.commit();
                            } catch (Exception e) {
                                closeConnection(conn);
                            }
                        }
                    };

                    threadPoolTaskExecutor.execute(backgroundTask);
                } else {
                    response.addGlobalErrorMessage(
                            LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
                }
            } catch (Exception e) {
                closeConnection(connection[0]);
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).notLoggedIn());
            response.setLoggedIn(false);
        }
        return response;
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.rollback();
                    connection.close();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public UserProfileStruct createAccountInternal(
            Connection connection,
            User newUser,
            String password,
            String resetQuestion,
            String resetAnswer,
            String givenName,
            String surname,
            int orgId) {
        CredentialedUser user = new CredentialedUser();
        user.setUser(newUser);
        user.setPasswordClearText(password);
        user.setResetQuestion(resetQuestion);
        user.setResetAnswer(resetAnswer);
        Profile initialProfile = new Profile();
        initialProfile.setGivenName(givenName);
        initialProfile.setSurname(surname);
        initialProfile.setOrganizationId(orgId);

        // This next call takes a really long time.
        UserProfileStruct userProfile =
                credentialedUserPersister.createUserProfile(connection, new UserProfileStruct(user, initialProfile));
        initialProfile = userProfile.initialProfile;

        grantNewProfilePermissions(orgId, initialProfile.getId(), connection);

        return userProfile;
    }

    @Override
    public DatumResponse<User> updateAccount(User user) {
        DatumResponse<User> response = new DatumResponse<User>();
        try {
            User targetUser = user == null ? null : userPersister.get(user.getId());

            if (user != null && authService.isUserAuthorized(ROLE_CURATOR, targetUser)) {
                try {
                    User savedUser = persistenceService.process(new UserPersister.UpdateUserFunc(user));

                    SecurityContext securityContext = securityContextProvider.get();
                    Authentication authentication = securityContext.getAuthentication();
                    UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                            savedUser.getHandle(), authentication.getCredentials());

                    Authentication customAuthentication = new CustomUserAuthentication(newAuth);
                    customAuthentication.setAuthenticated(true);
                    securityContext.setAuthentication(customAuthentication);

                    response.setDatum(savedUser);
                } catch (Exception e) {
                    response.addGlobalErrorMessage(e.getMessage());
                }
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
    public DatumResponse<CredentialedUser> changeAccountStatus(CredentialedUser credentialedUser) {
        DatumResponse<CredentialedUser> response = new DatumResponse<CredentialedUser>();
        try {
            if (authService.isUserAuthorized(ROLE_ADMIN, credentialedUser.getUser())) {
                User user = persistenceService.process(new UserPersister.UpdateUserFunc(credentialedUser.getUser()));
                credentialedUser.setUser(user);
                response.setDatum(credentialedUser);
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
    public DatumResponse<User> getLoggedInUser() {
        DatumResponse<User> response = new DatumResponse<User>();

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
                    response.setDatum(user.getUser());
                }
            }
        }

        return response;
    }

    @Override
    public DatumResponse<User> getUser(String handle) {
        DatumResponse<User> response = new DatumResponse<User>();
        try {
            CredentialedUser user = credentialedUserPersister.getByHandle(handle);
            if (authService.isUserAuthorized(ROLE_READER, user)) {
                response.setDatum(user.getUser());
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
    public DatumResponse<User> getUser(int id) {
        DatumResponse<User> response = new DatumResponse<User>();
        try {
            User targetUser = userPersister.get(id);

            if (authService.isUserAuthorized(ROLE_READER, targetUser)) {
                CredentialedUser user = credentialedUserPersister.get(id);
                response.setDatum(user.getUser());
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
    public ActionResponse updatePassword(String currentPassword, String newPassword) {
        ActionResponse response = new ActionResponse();
        try {
            User plainUser = userService.getLoggedInUser(true);
            CredentialedUser user = credentialedUserPersister.getByHandle(plainUser.getHandle());
            if (user != null) {
                if (BCrypt.checkpw(currentPassword, user.getPasswordHash())) {
                    user.setPasswordClearText(newPassword);
                    persistenceService.process(new CredentialedUserPersister.UpdateCredentialedUserFunc(user));
                } else {
                    response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class)
                            .currentPasswordInvalid());
                }
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).userNotFound());
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public ActionResponse updateUserAndPassword(String newPassword,
            final CredentialedUser user,
            final String givenName,
            final String surname,
            final List<String> organizations,
            final String defaultOrg) {
        ActionResponse response = new ActionResponse();
        try {
            Connection[] connection = new Connection[1];
            try {
                final Connection conn = persistenceService.getConnection();
                connection[0] = conn;

                AuthorizationService authService = this.authService.withConnection(conn);
                User targetUser = persistenceService.process(conn, new UserPersister.UpdateUserFunc(user.getUser()));
                if (!authService.isUserAuthorized(ROLE_ADMIN, targetUser)) {
                    response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
                    return response;
                }

                if (!Strings.isNullOrEmpty(newPassword)) {
                    user.setPasswordClearText(newPassword);
                } else {
                    user.setPasswordClearText("");
                }

                Runnable backgroundTask = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int userId = user.getId();
                            List<Integer> organizationIds = Lists.transform(organizations, Ints.stringConverter());

                            removeOldProfiles(userId, organizationIds, conn);

                            Integer defaultOrgId = Integer.valueOf(defaultOrg);
                            organizationIds.remove(defaultOrgId);

                            for (Integer orgId : organizationIds) {
                                createProfileIfNotExist(conn, givenName, surname, userId, orgId);
                            }

                            int defaultProfileId = createProfileIfNotExist(conn, givenName, surname, userId, defaultOrgId);
                            user.getUser().setDefaultProfile(defaultProfileId);

                            persistenceService.process(conn, new CredentialedUserPersister.UpdateCredentialedUserFunc(user));
                        } catch (Exception e) {
                            closeConnection(conn);
                        }
                    }
                };

                threadPoolTaskExecutor.execute(backgroundTask);
            } catch (Exception e) {
                closeConnection(connection[0]);
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    private int createProfileIfNotExist(Connection conn, String givenName, String surname, int userId, Integer orgId) {
        Profile profile;
        ProfileSummary profileSummary =
                persistenceService.process(conn, new RetrieveProfileForOrganizationFunc(orgId, userId));
        if (profileSummary == null) {
            profile = new Profile();
            profile.setGivenName(givenName);
            profile.setSurname(surname);
            profile.setOrganizationId(orgId);
            profile.setUserId(userId);
            profile = profilePersister.createProfile(profile);

            grantNewProfilePermissions(orgId, profile.getId(), conn);
        } else {
            profile = profileSummary.getProfile();
        }

        return profile.getId();
    }

    @Override
    public ActionResponse checkHandle(String username) {
        ActionResponse response = new ActionResponse();
        // this is a temporally, we must move this reserved words to other place
        if (username == null || "read".equals(username) || "collection".equals(username) || "query".equals(username)
                || "share".equals(username) || "search".equals(username) || "tag".equals(username)
                || "entity".equals(username) || "admin".equals(username) || "".equals(username)) {
            response.addGlobalErrorMessage("Invalid username");
        } else {
            if (userPersister.exists(username)) {
                response.addGlobalErrorMessage("Invalid username");
            }
        }
        return response;
    }

    @Override
    public ActionResponse checkAssignableHandle(String username) {
        ActionResponse response = new ActionResponse();
        // this is a temporally, we must move this reserved words to other place
        if (username == null || "read".equals(username) || "collection".equals(username) || "query".equals(username)
                || "share".equals(username) || "search".equals(username) || "tag".equals(username)
                || "entity".equals(username) || "admin".equals(username) || "".equals(username)) {
            response.addGlobalErrorMessage("Invalid username");
        } else {
            if (!userPersister.exists(username)) {
                response.addGlobalErrorMessage("Invalid username");
                response.setShowErrorMessages(true);
            }
        }
        return response;
    }

    @Override
    public PagedDataResponse<CredentialedUser> getUsersByPermissionMask(
            int start,
            int length,
            SortField sortField,
            boolean ascending,
            int permissionMask) {
        PagedDataResponse<CredentialedUser> response = new PagedDataResponse<CredentialedUser>();
        try {
            response.setStart(start);
            BaseAuthParam params;

            User user = userService.getLoggedInUser(true);

            Integer accessMode = ACCESS_MODE_EXPLICIT;
            if (authService.isSuperUser(user)) {
                accessMode = ACCESS_MODE_ROOT;
            }
            params = new BaseAuthParam(start, length, sortField, ascending, accessMode, getEffectiveSubject(user));
            params.setSubject(userService.getContextOrganizationId());

            int count = persistenceService.process(new GetUserCount(permissionMask, params));
            List<CredentialedUser> users = persistenceService.process(new RetrieveUsersFunc(permissionMask, params));
            response.setData(users);
            if (count > 0 || authService.isSuperUser(user)) {
                response.setTotalCount(count);
            } else {
                response.setTotalCount(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        }
        return response;
    }

    private void removeOldProfiles(int userId, Iterable<Integer> organizationIds, Connection conn) {
        List<ProfileSummary> profiles = persistenceService.process(conn,
                new ProfilePersister.RetrieveProfilesFunc(userId));

        Set<Integer> oldOrganizationIds = FluentIterable.from(profiles)
                .transform(new Function<ProfileSummary, Integer>() {
                    @Override
                    public Integer apply(ProfileSummary input) {
                        return input.getProfile().getOrganizationId();
                    }
                }).toSet();

        final Set<Integer> removedOrganizations = Sets.difference(oldOrganizationIds, Sets.newHashSet(organizationIds));

        ProfilePersister.DeleteProfileFunc[] profilesToRemove = FluentIterable.from(profiles)
                .filter(new Predicate<ProfileSummary>() {
                    @Override
                    public boolean apply(ProfileSummary input) {
                        return removedOrganizations.contains(input.getProfile().getOrganizationId());
                    }
                }).transform(new Function<ProfileSummary, ProfilePersister.DeleteProfileFunc>() {
                    @Override
                    public ProfilePersister.DeleteProfileFunc apply(ProfileSummary input) {
                        return new ProfilePersister.DeleteProfileFunc(input.getProfile());
                    }
                }).toArray(ProfilePersister.DeleteProfileFunc.class);

        persistenceService.process(conn, profilesToRemove);
    }

    private void grantNewProfilePermissions(int orgId, int profileId, Connection connection) {
        AuthorizationService authService = this.authService.withConnection(connection);
        authService.grantAtLeast(profileId, ROLE_CURATOR, orgId);
        authService.grantAtLeast(orgId, ROLE_ADMIN, profileId);
    }
}
