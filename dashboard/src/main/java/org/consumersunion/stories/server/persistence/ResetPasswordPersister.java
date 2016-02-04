package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.ResetPassword;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordPersister {
    private final PersistenceService persistenceService;

    @Inject
    ResetPasswordPersister(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public ResetPassword create(ResetPassword resetPassword) {
        return persistenceService.process(new ProcessFunc<ResetPassword, ResetPassword>(resetPassword) {
            @Override
            public ResetPassword process() {
                try {
                    delete(input.getHandle(), conn);

                    PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO reset_password (handle, nonce, created) VALUES (?,?,?)");

                    input.setCreated(new Date());
                    insert.setString(1, input.getHandle());
                    insert.setString(2, input.getNonce());
                    insert.setTimestamp(3, new Timestamp(input.getCreated().getTime()));

                    insert.execute();
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }

                return input;
            }
        });
    }

    public ResetPassword getByCode(String code) {
        return persistenceService.process(new ProcessFunc<String, ResetPassword>(code) {
            @Override
            public ResetPassword process() {
                try {
                    String query = "SELECT handle, created FROM reset_password WHERE nonce=?";

                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, input);

                    ResultSet rs = statement.executeQuery();

                    if (rs.next()) {
                        return new ResetPassword(rs.getString(1), input, rs.getTimestamp(2));
                    } else {
                        return null;
                    }
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public void delete(String handle) {
        persistenceService.process(deleteFunc(handle));
    }

    public void delete(String handle, Connection conn) {
        persistenceService.process(conn, deleteFunc(handle));
    }

    private ProcessFunc<String, Void> deleteFunc(String handle) {
        return new ProcessFunc<String, Void>(handle) {
            @Override
            public Void process() {
                try {
                    PreparedStatement delete = conn.prepareStatement("DELETE FROM reset_password WHERE handle=?");
                    delete.setString(1, input);
                    delete.executeUpdate();

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        };
    }
}
