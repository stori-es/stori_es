package org.consumersunion.stories.server.persistence;

import java.sql.Connection;

import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;

public interface PersistenceService {
    Object process(ProcessFunc<?, ?>... funcs);

    <I, O> O process(ProcessFunc<I, O> func);

    Connection getConnection();

    <T extends SystemEntity> Persister<T> getPersister(Class<T> clazz);

    <I, O> O process(Connection conn, ProcessFunc<I, O> func);
}
