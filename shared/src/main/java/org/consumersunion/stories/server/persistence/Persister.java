package org.consumersunion.stories.server.persistence;

import java.sql.Connection;

import org.consumersunion.stories.common.shared.model.SystemEntity;

public interface Persister<T extends SystemEntity> {
    Class<T> getHandles();

    T get(int id);

    T get(int id, Connection connection);
}
