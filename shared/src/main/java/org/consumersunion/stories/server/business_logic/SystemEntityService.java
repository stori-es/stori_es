package org.consumersunion.stories.server.business_logic;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.shared.model.SystemEntity;

public interface SystemEntityService {
    SystemEntity getSystemEntity(int entityId);

    SystemEntity getSystemEntity(int entityId, Connection connection);

    List<SystemEntity> getSystemEntities(Set<Integer> entityIds);
}
