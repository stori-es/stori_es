package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.dto.AuthParam;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.ProfilePersister.CreateProfileFunc;
import org.consumersunion.stories.server.persistence.funcs.CreateFunc;
import org.consumersunion.stories.server.persistence.funcs.DeleteFunc;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.consumersunion.stories.server.persistence.funcs.UpdateFunc;
import org.consumersunion.stories.server.security.BaseAuthParam;
import org.consumersunion.stories.server.security.RelationalAuthorizationQueryUtil;
import org.consumersunion.stories.server.security.authentication.BCrypt;
import org.springframework.stereotype.Component;

import net.lightoze.gwt.i18n.server.LocaleProxy;

/**
 * Persister to handle the CredentialedUser info
 *
 * @author Machin
 */
@Component
public class CredentialedUserPersister implements Persister<CredentialedUser>, MineCallbackProvider {
    private static final String SELECT_USER = "SELECT e.id, e.version, u.active, u.localPassword, u.resetQuestion, " +
            "u.resetAnswer, u.handle, u.defaultProfile FROM systemEntity e JOIN user u ON e.id=u.id ";
    private static final String AND_ACTIVE_1 = " AND u.active=1";

    static {
        if ("true".equals(System.getProperty("org.consumersunion.testMode"))) {
            LocaleProxy.initialize();
        }
    }

    private final PersistenceService persistenceService;
    private final ProfilePersister profilePersister;

    @Inject
    CredentialedUserPersister(
            PersistenceService persistenceService,
            ProfilePersister profilePersister) {
        this.persistenceService = persistenceService;
        this.profilePersister = profilePersister;
    }

    CredentialedUserPersister() {
        this(null, null);
    }

    @Override
    public Class<CredentialedUser> getHandles() {
        return CredentialedUser.class;
    }

    @Override
    public CredentialedUser get(int id) {
        return persistenceService.process(new RetrieveCredentialedUserFunc(id));
    }

    @Override
    public CredentialedUser get(int id, Connection connection) {
        return persistenceService.process(connection, new RetrieveCredentialedUserFunc(id));
    }

    public CredentialedUser getByHandle(String username) {
        return getByHandle(username, true);
    }

    public CredentialedUser getByHandle(String username, boolean includeInactive) {
        return persistenceService.process(new SearchByHandleFunc(username, includeInactive));
    }

    public CredentialedUser update(CredentialedUser user) {
        return persistenceService.process(new UpdateCredentialedUserFunc(user));
    }

    public void update(CredentialedUser user, Connection conn) {
        persistenceService.process(conn, new UpdateCredentialedUserFunc(user));
    }

    public CredentialedUser getByEmail(String email) {
        return getByEmail(email, true);
    }

    public CredentialedUser getByEmail(String email, boolean includeInactive) {
        return persistenceService.process(new SearchByEmailFunc(email, includeInactive));
    }

    public CredentialedUser getByProfileId(int profileId) {
        return persistenceService.process(new SearchByProfileIdFunc(profileId));
    }

    public UserProfileStruct createUserProfile(UserProfileStruct userProfileStruct) {
        return persistenceService.process(new CreateUserProfileFunc(userProfileStruct, this));
    }

    public UserProfileStruct createUserProfile(Connection conn, UserProfileStruct userProfileStruct) {
        return persistenceService.process(conn, new CreateUserProfileFunc(userProfileStruct, this));
    }

    public CredentialedUser createUser(CredentialedUser credentialedUser) {
        return persistenceService.process(new CreateUserFunc(credentialedUser));
    }

    public static class UserProfileStruct {
        public final CredentialedUser credentialedUser;
        public final Profile initialProfile;

        public UserProfileStruct(CredentialedUser credentialedUser, Profile initialProfile) {
            this.credentialedUser = credentialedUser;
            this.initialProfile = initialProfile;
        }
    }

    public static class CreateUserProfileFunc extends ProcessFunc<UserProfileStruct, UserProfileStruct> {
        private final ProfilePersister profilePersister;

        CreateUserProfileFunc(
                UserProfileStruct input,
                CredentialedUserPersister credentialedUserPersister) {
            super(input);

            profilePersister = credentialedUserPersister.profilePersister;
        }

        @Override
        public UserProfileStruct process() {
            CredentialedUser credentialedUser = input.credentialedUser;
            credentialedUser.getUser().setDefaultProfile(0);
            Profile profile = input.initialProfile;

            try {
                Statement stmt = conn.createStatement();
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
                stmt.close();
                credentialedUser = PersistenceUtil.process(conn, new CreateUserFunc(credentialedUser));
                profile.setUserId(credentialedUser.getId());
                profile = PersistenceUtil.process(conn, new CreateProfileFunc(profile, profilePersister));
                credentialedUser.getUser().setDefaultProfile(profile.getId());
                PersistenceUtil.process(conn, new UserPersister.UpdateUserFunc(credentialedUser.getUser()));

                return new UserProfileStruct(credentialedUser, profile);
            } catch (SQLException e) {
                throw new GeneralException(e);
            } finally {
                try {
                    Statement stmt = conn.createStatement();
                    stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
                    stmt.close();
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        }
    }

    private static class CreateUserFunc extends CreateFunc<CredentialedUser> {
        public CreateUserFunc(CredentialedUser input) {
            super(input);
        }

        @Override
        protected CredentialedUser createConcrete() throws SQLException {
            PreparedStatement insertUser = conn
                    .prepareStatement(
                            "INSERT INTO user (id, active, localPassword, resetQuestion, resetAnswer, handle, " +
                                    "defaultProfile) VALUES (?,?,?,?,?,?,?)");
            insertUser.setInt(1, input.getId());
            insertUser.setBoolean(2, input.getUser().isActive());

            String password = "";
            if (input.getPasswordClearText() != null) {
                password = BCrypt.hashpw(input.getPasswordClearText(), BCrypt.gensalt(12));
            }
            insertUser.setString(3, password);
            insertUser.setString(4, input.getResetQuestion());
            insertUser.setString(5, input.getResetAnswer());
            insertUser.setString(6, input.getUser().getHandle());
            insertUser.setInt(7, input.getUser().getDefaultProfile());

            int insertCount = insertUser.executeUpdate();
            if (insertCount != 1) {
                throw new GeneralException("Unexpected insert CredentialedUser (user) count: " + insertCount);
            }

            input.setPasswordHash(password);

            return input;
        }
    }

    public static class RetrieveCredentialedUserFunc extends RetrieveFunc<CredentialedUser> {
        protected RetrieveCredentialedUserFunc(Integer id) {
            super(id);
        }

        @Override
        protected CredentialedUser retrieveConcrete() throws SQLException {
            PreparedStatement insert = conn.prepareStatement(
                    "SELECT e.id, e.version, u.handle, u.active, u.localPassword, u.resetQuestion, u" +
                            ".resetAnswer, u.defaultProfile FROM systemEntity e LEFT JOIN user u ON e.id=u.id" +
                            " WHERE e.id=?");
            insert.setInt(1, input);

            ResultSet results = insert.executeQuery();
            if (results.next()) {
                CredentialedUser user = new CredentialedUser();

                user.getUser().setId(results.getInt(1));
                user.getUser().setVersion(results.getInt(2));
                user.getUser().setHandle(results.getString(3));
                user.getUser().setActive(results.getBoolean(4));
                user.setPasswordHash(results.getString(5));
                user.setResetQuestion(results.getString(6));
                user.setResetAnswer(results.getString(7));
                user.getUser().setDefaultProfile(results.getInt(8));

                return user;
            }

            return null;
        }
    }

    public static class UpdateCredentialedUserFunc extends UpdateFunc<CredentialedUser> {
        public UpdateCredentialedUserFunc(CredentialedUser input) {
            super(input);
        }

        @Override
        protected CredentialedUser updateConcrete() throws SQLException {
            PreparedStatement updateUser = conn.prepareStatement(
                    "UPDATE user SET active=?, localPassword=?, resetQuestion=?, resetAnswer=?,"
                            + "defaultProfile=? WHERE id=?");
            updateUser.setBoolean(1, input.getUser().isActive());
            String password = "";
            if (input.getPasswordClearText() != null) {
                if ("".equals(input.getPasswordClearText())) {
                    password = input.getPasswordHash();
                } else {
                    password = BCrypt.hashpw(input.getPasswordClearText(), BCrypt.gensalt(12));
                }
            }
            updateUser.setString(2, password);
            updateUser.setString(3, input.getResetQuestion());
            updateUser.setString(4, input.getResetAnswer());
            updateUser.setInt(5, input.getUser().getDefaultProfile());
            updateUser.setInt(6, input.getUser().getId());

            int updateCount = updateUser.executeUpdate();
            if (updateCount != 1) {
                throw new GeneralException("Unexpected update CredentialedUser (user) count: " + updateCount);
            }

            List<ProfileSummary> profiles = PersistenceUtil.process(conn,
                    new ProfilePersister.RetrieveProfilesFunc(input.getId()));
            input.getUser().setProfiles(profiles);

            return input;
        }
    }

    public static class DeleteCredentialedUserFunc extends DeleteFunc<CredentialedUser> {
        public DeleteCredentialedUserFunc(CredentialedUser input) {
            super(input);
        }

        @Override
        public CredentialedUser process() {
            try {
                PreparedStatement delete = conn.prepareStatement("DELETE FROM user WHERE id=?");
                delete.setInt(1, input.getUser().getId());

                int updateCount = delete.executeUpdate();
                if (updateCount != 1) {
                    throw new GeneralException("Unexpected insert count: " + updateCount);
                }

                return input;
            } catch (final SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class SearchByEmailFunc extends ProcessFunc<String, CredentialedUser> {
        private final boolean includeInactive;

        public SearchByEmailFunc(String email, boolean includeInactive) {
            super(email);

            this.includeInactive = includeInactive;
        }

        @Override
        public CredentialedUser process() {
            try {
                PreparedStatement select = conn.prepareStatement(SELECT_USER +
                        "JOIN contact c ON c.entityId = u.id AND c.medium='EMAIL' " +
                        "WHERE c.value=?" + (includeInactive ? "" : AND_ACTIVE_1) + " LIMIT 1");
                select.setString(1, input);

                return instantiateCredentialedUser(conn, select);
            } catch (final SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    private static class SearchByProfileIdFunc extends ProcessFunc<Integer, CredentialedUser> {
        public SearchByProfileIdFunc(int profileId) {
            super(profileId);
        }

        @Override
        public CredentialedUser process() {
            try {
                PreparedStatement select = conn.prepareStatement(SELECT_USER
                        + "JOIN profile p ON u.id = p.user "
                        + "WHERE p.id=? and u.active=1");
                select.setInt(1, input);

                return instantiateCredentialedUser(conn, select);
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    private static CredentialedUser instantiateCredentialedUser(Connection conn, PreparedStatement select)
            throws SQLException {
        ResultSet results = select.executeQuery();

        if (!results.next()) {
            return null;
        }

        CredentialedUser user = new CredentialedUser();

        user.getUser().setId(results.getInt(1));
        user.getUser().setVersion(results.getInt(2));
        user.getUser().setActive(results.getBoolean(3));
        user.setPasswordHash(results.getString(4));
        user.setResetQuestion(results.getString(5));
        user.setResetAnswer(results.getString(6));
        user.getUser().setHandle(results.getString(7));
        user.getUser().setDefaultProfile(results.getInt(8));

        List<ProfileSummary> profiles =
                PersistenceUtil.process(conn, new ProfilePersister.RetrieveProfilesFunc(user.getId()));
        user.getUser().setProfiles(profiles);
        return user;
    }

    static class SearchByHandleFunc extends ProcessFunc<String, CredentialedUser> {
        private final boolean includeInactive;

        public SearchByHandleFunc(String handle, boolean includeInactive) {
            super(handle);

            this.includeInactive = includeInactive;
        }

        @Override
        public CredentialedUser process() {
            try {
                PreparedStatement select = conn.prepareStatement(
                        SELECT_USER + "WHERE u.handleLowerCase=?" + (includeInactive ? "" : AND_ACTIVE_1));
                select.setString(1, input.toLowerCase());

                return instantiateCredentialedUser(conn, select);
            } catch (final SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public EncryptPasswordFunc getPasswordCrypted(CredentialedUser user) {
        return new EncryptPasswordFunc(user);
    }

    public class EncryptPasswordFunc extends ProcessFunc<CredentialedUser, String> {
        EncryptPasswordFunc(CredentialedUser user) {
            super(user);
        }

        @Override
        public String process() {
            if (input.getPasswordClearText() != null && !"".equals(input.getPasswordClearText())) {
                return BCrypt.hashpw(input.getPasswordClearText(), BCrypt.gensalt(12));
            }

            return null;
        }
    }

    public static class RetrieveUsersFunc extends ProcessFunc<BaseAuthParam, List<CredentialedUser>> {
        private final int permissionsMask;

        public RetrieveUsersFunc(int permissionsMask, BaseAuthParam input) {
            super(input);

            this.permissionsMask = permissionsMask;
        }

        @Override
        public List<CredentialedUser> process() {
            List<CredentialedUser> list = new ArrayList<CredentialedUser>();
            try {

                RelationalAuthorizationQueryUtil util =
                        new RelationalAuthorizationQueryUtil(new CredentialedUserPersister());
                String select = "SELECT DISTINCT e.id, e.version, u.active, u.localPassword, u.resetQuestion, u" +
                        ".resetAnswer, u.handle, u.defaultProfile";
                String from = "FROM systemEntity e JOIN user u ON e.id=u.id JOIN profile p ON p.user=u.id";

                PreparedStatement ps = util.prepareAuthorizationSelect(select, from, null, new Object[0],
                        input, conn, true, permissionsMask);

                ResultSet results = ps.executeQuery();
                while (results.next()) {
                    CredentialedUser user = new CredentialedUser();
                    user.getUser().setId(results.getInt(1));
                    user.getUser().setVersion(results.getInt(2));
                    user.getUser().setActive(results.getBoolean(3));
                    user.setPasswordHash(results.getString(4));
                    user.setResetQuestion(results.getString(5));
                    user.setResetAnswer(results.getString(6));
                    user.getUser().setHandle(results.getString(7));
                    user.getUser().setDefaultProfile(results.getInt(8));
                    list.add(user);

                    List<ProfileSummary> profiles = PersistenceUtil.process(conn,
                            new ProfilePersister.RetrieveProfilesFunc(user.getId()));
                    user.getUser().setProfiles(profiles);
                }
            } catch (Exception e) {
                throw new GeneralException(e);
            }
            return list;
        }
    }

    public static class GetUserCount extends ProcessFunc<BaseAuthParam, Integer> {
        private final int permissionMask;

        public GetUserCount(int permissionMask, BaseAuthParam input) {
            super(input);

            this.permissionMask = permissionMask;
        }

        @Override
        public Integer process() {
            try {
                // TODO : This will only work as ROOT
                String select = "SELECT COUNT(*)";
                String from = "FROM (SELECT DISTINCT u.id FROM user u JOIN systemEntity e ON e.id = u.id JOIN " +
                        "profile p ON p.user=u.id) U";

                RelationalAuthorizationQueryUtil util = new RelationalAuthorizationQueryUtil(
                        new CredentialedUserPersister());

                PreparedStatement ps = util.prepareAuthorizationSelect(select, from, null, new Object[0],
                        input.noLimit(), conn, true, permissionMask);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (Exception e) {
                throw new GeneralException(e);
            }
            return 0;
        }
    }

    @Override
    public String getMineWhereClause(AuthParam<?> authParam) {
        String mineClause = "";
        if (authParam.getSubject() != null) {
            mineClause += "p.organization = " + authParam.getSubject();
        } else if (authParam.getEffectiveId() != null) {
            mineClause += "p.id = " + authParam.getEffectiveId();
        }
        return mineClause;
    }

    @Override
    public String getMineJoinClause(AuthParam<?> authParam) {
        return MineCallbackProvider.DEFAULT_JOIN_CLAUSE;
    }

    @Override
    public boolean hasMineClause() {
        return true;
    }

    @Override
    public String getSortClause(SortField sortField, boolean ascending) {
        return MineCallbackProvider.DEFAULT_SORT_CLAUSE;
    }
}
