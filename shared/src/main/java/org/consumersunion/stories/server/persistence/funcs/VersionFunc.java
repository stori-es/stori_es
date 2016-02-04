package org.consumersunion.stories.server.persistence.funcs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.service.GeneralException;

public abstract class VersionFunc<Input extends SystemEntity> extends ProcessFunc<Input, Input> {
    protected VersionFunc(Input input) {
        super(input);
    }

    protected void checkVersion() {
        try {
            PreparedStatement test = conn.prepareStatement("SELECT version FROM systemEntity WHERE id=?");
            test.setInt(1, input.getId());

            ResultSet results = test.executeQuery();
            if (!results.next()) {
                throw new GeneralException("No results found for ID " + input.getId());
            } else if (results.getInt(1) != input.getVersion()) {
                throw new GeneralException("Version mismatch. (" + results.getInt(1) + "/" + input.getVersion() + ")");
            }
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }
}
