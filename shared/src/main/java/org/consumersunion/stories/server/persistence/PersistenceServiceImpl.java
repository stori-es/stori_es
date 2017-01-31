package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.util.Arrays;

import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.springframework.stereotype.Component;

@Component
public class PersistenceServiceImpl implements PersistenceService {
    @Override
    public <I, O> O process(ProcessFunc<I, O> func) {
        return org.consumersunion.stories.server.persistence.PersistenceUtil.process(func);
    }

    @Override
    public <I, O> O process(Connection conn, ProcessFunc<I, O> func) {
        return org.consumersunion.stories.server.persistence.PersistenceUtil.process(conn, func);
    }

    @Override
    public <I, O> O process(Connection conn, ProcessFunc<I, O>[] funcs) {
        if (funcs.length == 1) {
            return process(conn, funcs[0]);
        } else if (funcs.length > 1) {
            return org.consumersunion.stories.server.persistence.PersistenceUtil
                    .process(conn, funcs[0], Arrays.copyOfRange(funcs, 1, funcs.length));
        } else {
            return null;
        }
    }

    @Override
    public Object process(ProcessFunc<?, ?>... funcs) {
        if (funcs.length == 1) {
            return process(funcs[0]);
        } else if (funcs.length > 1) {
            return org.consumersunion.stories.server.persistence.PersistenceUtil
                    .process(funcs[0], Arrays.copyOfRange(funcs, 1, funcs.length));
        } else {
            return null;
        }
    }

    @Override
    public Connection getConnection() {
        return org.consumersunion.stories.server.persistence.PersistenceUtil.getConnection();
    }

    @Override
    public <T extends SystemEntity> Persister<T> getPersister(Class<T> clazz) {
        return org.consumersunion.stories.server.persistence.PersistenceUtil.getPersisterFor(clazz);
    }
}
