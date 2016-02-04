package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class PersistenceUtil {
    private final static Map<Class<?>, Persister<?>> handlerMap = new HashMap<Class<?>, Persister<?>>();
    private final static PoolingDataSource poolingDataSource;

    public static void setPersisters(List<Persister> persisters) {
        handlerMap.clear();
        for (Persister persister : persisters) {
            register(persister);
        }
    }

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String jdbcUrl = getJdbcUrl();

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(jdbcUrl, "cuAdmin", null);
        GenericObjectPool.Config config = getPoolConfig();

        ObjectPool<Connection> connectionPool = new GenericObjectPool<Connection>(null, config);

        new PoolableConnectionFactory(connectionFactory, connectionPool, null, "SELECT 1 ", false, false);

        poolingDataSource = new PoolingDataSource(connectionPool);
    }

    public static String getJdbcUrl() {
        String jdbcUrl = System.getProperty("JDBC_CONNECTION_STRING");

        if (Strings.isNullOrEmpty(jdbcUrl)) {
            jdbcUrl = "jdbc:mysql://localhost/stories";
        }

        return jdbcUrl;
    }

    public static GenericObjectPool.Config getPoolConfig() {
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        if ("true".equals(System.getProperty("org.consumersunion.constraintConnectionPool"))) {
            // Need two in order to open 1 connection in the test and a second connection in the service stack.
            config.maxActive = 2;
            config.maxIdle = 1;
        } else {
            // See PRODUCT-1776 for explanation and limitations of this setting.
            config.maxActive = 20;
            if (System.getProperty("aws.connectionPoolCount") != null) {
                config.maxActive = getAwsConnectionPoolCount(config.maxActive);
            }
            config.maxIdle = 5;
        }
        config.minIdle = 1;
        config.maxWait = 1000;
        config.testWhileIdle = true;
        config.timeBetweenEvictionRunsMillis = 500;
        config.testOnBorrow = true;
        config.testOnReturn = true;

        return config;
    }

    public static Connection getConnection() {
        try {
            Connection conn = poolingDataSource.getConnection();
            // not sure why this is necessary... we set the 'autocommit' to
            // false in the pool settings, but it's
            // empirically not working so we explicitly set here to fix
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    public static <I, O> O process(ProcessFunc<I, O> func, ProcessFunc<?, ?>... funcs) {
        Connection conn = getConnection();
        try {
            try {
                return process(conn, func, funcs);
            } finally {
                if (!conn.isClosed()) {
                    conn.close();
                }
            }
        } catch (SQLException e) { // from the setSavepoint
            throw new GeneralException(e);
        }
    }

    public static <I, O> O process(Connection conn, ProcessFunc<I, O> resultFunc, ProcessFunc<?, ?>... funcs) {
        try {
            Object result;
            conn.setSavepoint();

            try {
                resultFunc.setConnection(conn);
                result = resultFunc.process();
                for (ProcessFunc<?, ?> func : funcs) {
                    func.setConnection(conn);
                    func.process();
                }

                conn.commit();

                return (O) result;
            } catch (RuntimeException e) {
                rollbackAndClose(conn);
                throw e;
            } catch (Exception e) {
                rollbackAndClose(conn);
                throw new GeneralException(e);
            }
        } catch (SQLException e) { // from the setSavepoint
            throw new GeneralException(e);
        }
    }

    public static <T extends SystemEntity> Persister<T> getPersisterFor(Class<T> c) {
        Persister p = handlerMap.get(c);
        if (p == null) {
            throw new GeneralException("Could not find Persister for class: " + c.getCanonicalName());
        }

        return (Persister<T>) p;
    }

    private static void register(Persister<?> p) {
        if (handlerMap.containsKey(p.getHandles())) {
            throw new GeneralException("Attempt to register multiple handlers for type: " + p.getHandles());
        }
        handlerMap.put(p.getHandles(), p);
    }

    private static void rollbackAndClose(Connection conn) throws SQLException {
        if (!conn.isClosed()) {
            conn.rollback();
            conn.close();
        }
    }

    private static int getAwsConnectionPoolCount(int defaultCount) {
        try {
            return Integer.parseInt(System.getProperty("aws.connectionPoolCount"));
        } catch (NumberFormatException ignored) {
            return defaultCount;
        }
    }
}
