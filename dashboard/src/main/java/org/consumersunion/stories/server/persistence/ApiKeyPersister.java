package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.api.ApiKey;
import org.consumersunion.stories.common.shared.api.ApiKey.Builder;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyPersister implements Persister<ApiKey> {
    public static class RetrieveApiKey extends ProcessFunc<String, ApiKey> {
        public RetrieveApiKey(String uuid) {
            super(uuid);
        }

        @Override
        public ApiKey process() {
            ApiKey key = null;

            try {
                PreparedStatement select = conn.prepareStatement(
                        "SELECT user, uuid, uuid_bin " +
                                "FROM api_keys " +
                                "WHERE uuid_bin = RPAD(UNHEX(REPLACE(?, '-', '')), 16, '\\0')");
                select.setString(1, input);

                ResultSet results = select.executeQuery();
                if (results.next()) {
                    key = new Builder()
                            .user(results.getInt(1))
                            .uuid(results.getString(2))
                            .uuidBin(results.getString(3))
                            .create();
                }
            } catch (SQLException e) {
                throw new GeneralException("Could not retrieve API key", e);
            }

            return key;
        }
    }

    private final PersistenceService persistenceService;

    @Inject
    ApiKeyPersister(
            PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public ApiKey getByUuid(String uuid) {
        return persistenceService.process(new RetrieveApiKey(uuid));
    }

    @Override
    public ApiKey get(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ApiKey get(int id, Connection connection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<ApiKey> getHandles() {
        return ApiKey.class;
    }
}
