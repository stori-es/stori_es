package org.consumersunion.stories.server.persistence.funcs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.entity.Entity;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.util.StringUtil;

public abstract class CreateFunc<Input> extends ProcessFunc<Input, Input> {
    protected CreateFunc(Input input) {
        super(input);
    }

    @Override
    public Input process() {
        try {
            if (input instanceof SystemEntity) {
                SystemEntity systemEntity = (SystemEntity) this.input;
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO systemEntity (version, public, owner, creator) VALUES (1, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);

                insert.setBoolean(1, systemEntity.isPublic());
                if (systemEntity.getOwner() == null) {
                    insert.setNull(2, Types.INTEGER);
                } else {
                    insert.setInt(2, systemEntity.getOwner());
                }

                if (systemEntity.getCreator() == null) {
                    insert.setNull(3, Types.INTEGER);
                } else {
                    insert.setInt(3, systemEntity.getOwner());
                }

                insert.executeUpdate();
                ResultSet keys = insert.getGeneratedKeys();
                if (!keys.next()) {
                    throw new GeneralException("Error while creating SystemEntity for " + this.input);
                }

                int id = keys.getInt(1);
                systemEntity.setId(id);
                systemEntity.setVersion(1);

                PreparedStatement getCreated = conn.prepareStatement("SELECT created FROM systemEntity WHERE id=?");
                getCreated.setInt(1, id);

                ResultSet result = getCreated.executeQuery();
                if (result.next()) {
                    systemEntity.setCreated(result.getTimestamp(1));
                }
            }

            if (input instanceof Entity) {
                Entity entity = (Entity) this.input;
                PreparedStatement insert =
                        // the DB will set the created and lastModified itself
                        conn.prepareStatement("INSERT INTO entity (id,permalink,profile) VALUES (?,?,?)");

                insert.setInt(1, entity.getId());
                insert.setString(2, StringUtil.cleanPermalink(entity.getPermalink()));
                if (entity.getProfile() == null) {
                    insert.setNull(3, Types.INTEGER);
                } else {
                    insert.setInt(3, entity.getProfile());
                }

                insert.executeUpdate();
            }

            return createConcrete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException(e);
        }
    }

    protected abstract Input createConcrete() throws SQLException;
}
