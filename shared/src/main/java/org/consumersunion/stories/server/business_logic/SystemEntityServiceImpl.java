package org.consumersunion.stories.server.business_logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.PersistenceService;
import org.consumersunion.stories.server.persistence.Persister;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class SystemEntityServiceImpl implements SystemEntityService {
    private final TableToEntity[] typeMap = {
            new TableToEntity(User.class, "0", "user"),
            new TableToEntity(Profile.class, "0", "profile"),
            new TableToEntity(Story.class, "0", "story"),
            new TableToEntity(QuestionnaireI15d.class, "0", "questionnaire"),
            new TableToEntity(Collection.class, "1", "collection"),
            new TableToEntity(Organization.class, "0", "organization")};
    private final PersistenceService persistenceService;

    @Inject
    public SystemEntityServiceImpl(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public SystemEntity getSystemEntity(int entityId) {
        Persister<? extends SystemEntity> persister = getPersister(entityId, null);

        if (persister == null) {
            return null;
        } else {
            return persister.get(entityId);
        }
    }

    @Override
    public SystemEntity getSystemEntity(int entityId, Connection connection) {
        Persister<? extends SystemEntity> persister = getPersister(entityId, connection);

        if (persister == null) {
            return null;
        } else {
            return persister.get(entityId, connection);
        }
    }

    @Override
    public List<SystemEntity> getSystemEntities(Set<Integer> entityIds) {
        Connection connection = persistenceService.getConnection();
        try {
            List<SystemEntity> entities = Lists.newArrayList();

            for (Integer entityId : entityIds) {
                SystemEntity systemEntity = getSystemEntity(entityId);
                entities.add(systemEntity);
            }

            return entities;
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

    private Persister<? extends SystemEntity> getPersister(int entityId, Connection conn) {
        Class<? extends SystemEntity> classType = getEntityType(entityId, conn);
        if (classType == null) {
            return null;
        }

        return persistenceService.getPersister(classType);
    }

    private Class<? extends SystemEntity> getEntityType(int entityId, Connection conn) {
        String query = "SELECT class_name FROM (";

        for (int i = 0; i < typeMap.length; i += 1) {
            if (i > 0) {
                query += " UNION ";
            }
            query += "(SELECT '" + typeMap[i].modelClass
                    + "'  AS class_name, "
                    + typeMap[i].precedence + " AS precedence FROM "
                    + typeMap[i].tableName + " WHERE id=?)";
        }
        query += ") AS tmp ORDER BY precedence ASC LIMIT 1";

        if (conn == null) {
            conn = persistenceService.getConnection();
        }

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            for (int i = 0; i < typeMap.length; i += 1) {
                statement.setInt(i + 1, entityId);
            }

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                try {
                    return (Class<? extends SystemEntity>) Class.forName(results.getString(1));
                } catch (ClassNotFoundException e) {
                    throw new GeneralException(e);
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new GeneralException(e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    private static class TableToEntity {
        final String modelClass;
        final String precedence;
        final String tableName;

        private TableToEntity(Class modelClass, String precedence, String tableName) {
            this.modelClass = modelClass.getName();
            this.precedence = precedence;
            this.tableName = tableName;
        }
    }
}
