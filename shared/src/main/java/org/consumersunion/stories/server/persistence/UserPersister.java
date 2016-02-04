package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.persistence.funcs.DeleteFunc;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.persistence.funcs.RetrieveFunc;
import org.consumersunion.stories.server.persistence.funcs.UpdateFunc;
import org.springframework.stereotype.Component;

import net.lightoze.gwt.i18n.server.LocaleFactory;

@Component
public class UserPersister implements Persister<User> {
    private final PersistenceService persistenceService;

    @Inject
    UserPersister(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Class<User> getHandles() {
        return User.class;
    }

    @Override
    public User get(int id) {
        return persistenceService.process(new RetrieveUserFunc(id));
    }

    @Override
    public User get(int id, Connection connection) {
        return persistenceService.process(connection, new RetrieveUserFunc(id));
    }

    public boolean exists(String handle) {
        return persistenceService.process(new CheckHandle(handle));
    }

    public static class RetrieveUserFunc extends RetrieveFunc<User> {
        protected RetrieveUserFunc(Integer id) {
            super(id);
        }

        @Override
        protected User retrieveConcrete() throws SQLException {
            PreparedStatement select = conn
                    .prepareStatement(
                            "SELECT e.id, e.version, u.handle, u.active FROM systemEntity e JOIN user u ON e.id=u.id " +
                                    "WHERE e.id=?");

            select.setInt(1, input);

            return retrieveUser(select);
        }
    }

    public static class UpdateUserFunc extends UpdateFunc<User> {
        public UpdateUserFunc(User input) {
            super(input);
        }

        @Override
        protected User updateConcrete() throws SQLException {
            PreparedStatement updateUser =
                    conn.prepareStatement("UPDATE user SET active=?, handle=?, defaultProfile=? WHERE id=?");
            updateUser.setBoolean(1, input.isActive());
            updateUser.setString(2, input.getHandle());
            updateUser.setInt(3, input.getDefaultProfile());
            updateUser.setInt(4, input.getId());

            int updateCount = updateUser.executeUpdate();
            if (updateCount != 1) {
                throw new GeneralException("Unexpected update (user) count: " + updateCount);
            }

            return input;
        }
    }

    public static class DeleteUserFunc extends DeleteFunc<User> {
        public DeleteUserFunc(User input) {
            super(input);
        }

        @Override
        public User process() {
            checkVersion();
            try {
                PreparedStatement deleteUser = conn.prepareStatement("DELETE FROM user WHERE id=?");
                deleteUser.setInt(1, input.getId());

                int updateCount = deleteUser.executeUpdate();
                if (updateCount != 1) {
                    throw new GeneralException("Unexpected delete (user) count: " + updateCount);
                }

                deleteEntityRecordAndUpdateInput();

                return input;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    private static User retrieveUser(PreparedStatement select) throws SQLException {
        ResultSet results = select.executeQuery();
        if (!results.next()) {
            String message = LocaleFactory.get(CommonI18nErrorMessages.class).userNotFound();
            throw new NotFoundException(message);
        }

        User user = new User(results.getInt(1), results.getInt(2));
        user.setHandle(results.getString(3));
        user.setActive(results.getBoolean(4));

        return user;
    }

    private static class CheckHandle extends ProcessFunc<String, Boolean> {
        public CheckHandle(String handle) {
            super(handle);
        }

        @Override
        public Boolean process() {
            try {
                PreparedStatement searchUser = conn.prepareStatement("SELECT COUNT(*) FROM user WHERE handle = ?");
                searchUser.setString(1, input);
                ResultSet rs = searchUser.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }

                PreparedStatement searchOrganization = conn
                        .prepareStatement("SELECT COUNT(*) FROM organization WHERE name = ?");
                searchOrganization.setString(1, input);
                rs = searchOrganization.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
            return false;
        }
    }
}
