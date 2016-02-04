package org.consumersunion.stories.server.persistence.funcs;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.entity.Entity;
import org.consumersunion.stories.common.shared.service.GeneralException;

public abstract class DeleteFunc<Input extends SystemEntity> extends VersionFunc<Input> {
    protected DeleteFunc(Input i) {
        super(i);
    }

    protected void deleteEntityRecordAndUpdateInput() {
        try {
            if (input instanceof Entity) {
                PreparedStatement deleteEntity = conn.prepareStatement("DELETE FROM entity WHERE id=?");
                deleteEntity.setInt(1, input.getId());

                int deleteCount = deleteEntity.executeUpdate();
                if (deleteCount != 1) {
                    throw new GeneralException("Unexpected insert count: " + deleteCount);
                }
            }

            PreparedStatement deleteSystemEntity = conn.prepareStatement("DELETE FROM systemEntity WHERE id=?");
            deleteSystemEntity.setInt(1, input.getId());

            int deleteCount = deleteSystemEntity.executeUpdate();
            if (deleteCount != 1) {
                throw new GeneralException("Unexpected insert count: " + deleteCount);
            }

            input.setId(-1);
            input.setVersion(-1);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }
}
