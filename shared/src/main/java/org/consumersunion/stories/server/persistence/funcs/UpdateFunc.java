package org.consumersunion.stories.server.persistence.funcs;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.entity.Entity;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.util.StringUtil;

public abstract class UpdateFunc<Input extends SystemEntity> extends VersionFunc<Input> {
    protected UpdateFunc(Input input) {
        super(input);
    }

    @Override
    public Input process() {
        try {
            checkVersion();

            // notice that we let the DB handle the version itself; we trust the update is atomic
            int version = input.getVersion();
            PreparedStatement update =
                    conn.prepareStatement(
                            "UPDATE systemEntity SET lastModified=NOW(), version=version + 1, public=? " +
                                    "WHERE id=? AND version=?");
            update.setBoolean(1, input.isPublic());
            update.setInt(2, input.getId());
            update.setInt(3, version);

            int updateCount = update.executeUpdate();
            if (updateCount == 1) {
                input.setVersion(version + 1);
                input.setUpdated(new Date());
            } else {
                throw new GeneralException("Update failed; potential version mismatch.");
            }

            if (input instanceof Entity) {
                PreparedStatement entityUpdate =
                        conn.prepareStatement("UPDATE entity SET profile=?, permalink=? WHERE id=?");
                Integer profile = ((Entity) input).getProfile();
                if (profile == null) {
                    entityUpdate.setNull(1, Types.INTEGER);
                } else {
                    entityUpdate.setInt(1, profile);
                }
                entityUpdate.setString(2, StringUtil.cleanPermalink(((Entity) input).getPermalink()));
                entityUpdate.setInt(3, input.getId());
                entityUpdate.executeUpdate();
            }

            return updateConcrete();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GeneralException(e);
        }
    }

    protected abstract Input updateConcrete() throws SQLException;
}
