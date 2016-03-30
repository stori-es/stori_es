package org.consumersunion.stories.server.persistence.funcs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.entity.Entity;
import org.consumersunion.stories.common.shared.service.GeneralException;

public abstract class RetrieveFunc<Output> extends ProcessFunc<Integer, Output> {
    protected RetrieveFunc(Integer id) {
        super(id);
    }

    @Override
    public Output process() {
        try {
            Output concreteItem = retrieveConcrete();
            boolean isFullEntity = true;

            if (concreteItem instanceof Entity) {
                Entity entity = (Entity) concreteItem;
                PreparedStatement insert =
                        conn.prepareStatement(
                                "SELECT se.id, se.created, se.lastModified, se.owner, se.version, se.public, " +
                                        "e.profile, e.permalink, se.creator FROM entity e " +
                                        "JOIN systemEntity se ON e.id=se.id " +
                                        "WHERE se.id=?");
                insert.setInt(1, input);

                ResultSet results = insert.executeQuery();
                if (!results.next()) {
                    isFullEntity = false;
                } else {
                    entity.setId(results.getInt(1));
                    entity.setCreated(results.getTimestamp(2));
                    entity.setUpdated(results.getTimestamp(3));
                    entity.setOwner(results.getInt(4));
                    entity.setVersion(results.getInt(5));
                    entity.setPublic(results.getBoolean(6));
                    entity.setProfile(results.getInt(7));
                    if (results.wasNull()) {
                        entity.setProfile(null);
                    }
                    entity.setPermalink(results.getString(8));
                    if (results.wasNull()) {
                        entity.setPermalink(null);
                    }
                    entity.setCreator(results.getInt(9));
                }
            }

            if (concreteItem instanceof SystemEntity && !isFullEntity) {
                PreparedStatement insert =
                        conn.prepareStatement(
                                "SELECT se.id, se.created, se.lastModified, se.owner, se.version, se.public, se.creator"
                                        + " FROM systemEntity se WHERE se.id=?");
                insert.setInt(1, input);

                ResultSet results = insert.executeQuery();
                if (!results.next()) {
                    throw new GeneralException("Did not find result for " + input + ".");
                }

                SystemEntity systemEntity = (SystemEntity) concreteItem;
                systemEntity.setId(results.getInt(1));
                systemEntity.setCreated(results.getTimestamp(2));
                systemEntity.setUpdated(results.getTimestamp(3));
                systemEntity.setOwner(results.getInt(4));
                systemEntity.setVersion(results.getInt(5));
                systemEntity.setPublic(results.getBoolean(6));
                systemEntity.setCreator(results.getInt(7));
            }

            return concreteItem;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GeneralException(e);
        }
    }

    protected abstract Output retrieveConcrete() throws SQLException;
}
