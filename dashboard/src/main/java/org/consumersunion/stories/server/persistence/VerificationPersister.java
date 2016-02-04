package org.consumersunion.stories.server.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.inject.Inject;

import org.consumersunion.stories.common.server.model.Verification;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.springframework.stereotype.Component;

@Component
public class VerificationPersister {
    private static final int DUPLICATE_KEY = 1062;

    private final PersistenceService persistenceService;

    @Inject
    VerificationPersister(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Verification create(Verification verification) {
        return persistenceService.process(new ProcessFunc<Verification, Verification>(verification) {
            @Override
            public Verification process() {
                try {
                    PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO verification_nonce (profile, email, nonce, created) VALUES (?,?,?,?)");

                    input.setCreated(new Date());
                    insert.setInt(1, input.getEntityId());
                    insert.setString(2, input.getEmail());
                    insert.setString(3, input.getNonce());
                    insert.setDate(4, new java.sql.Date(input.getCreated().getTime()));

                    insert.execute();
                } catch (SQLException e) {
                    if (e.getErrorCode() == DUPLICATE_KEY) {
                        return get(input.getEntityId(), input.getEmail());
                    } else {
                        throw new GeneralException(e);
                    }
                }

                return input;
            }
        });
    }

    public Verification get(int profileId, final String email) {
        return persistenceService.process(new ProcessFunc<Integer, Verification>(profileId) {
            @Override
            public Verification process() {
                try {
                    String query = "SELECT nonce, created"
                            + " FROM verification_nonce WHERE profile=? AND email=?";

                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, input);
                    statement.setString(2, email);

                    ResultSet rs = statement.executeQuery();

                    if (rs.next()) {
                        return new Verification(input, email, rs.getString(1), rs.getDate(2));
                    } else {
                        return null;
                    }
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public Verification get(String code) {
        return persistenceService.process(new ProcessFunc<String, Verification>(code) {
            @Override
            public Verification process() {
                try {
                    String query = "SELECT profile, email, created"
                            + " FROM verification_nonce WHERE nonce=?";

                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, input);

                    ResultSet rs = statement.executeQuery();

                    if (rs.next()) {
                        return new Verification(rs.getInt(1), rs.getString(2), input, rs.getDate(3));
                    } else {
                        return null;
                    }
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public void delete(String code) {
        persistenceService.process(new ProcessFunc<String, Void>(code) {
            @Override
            public Void process() {
                try {
                    PreparedStatement delete = conn.prepareStatement("DELETE FROM verification_nonce WHERE nonce=?");
                    delete.setString(1, input);
                    delete.executeUpdate();

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }
}
