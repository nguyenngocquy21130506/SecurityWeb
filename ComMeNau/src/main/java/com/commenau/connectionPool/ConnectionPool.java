package com.commenau.connectionPool;

import com.commenau.util.DBProperties;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPool {
    int poolSize = Integer.parseInt(DBProperties.initialPoolSize);
    int maxSize = Integer.parseInt(DBProperties.maxConnections);
    AtomicInteger using = new AtomicInteger(0);
    private ArrayBlockingQueue<Connection> pool;
    private static ConnectionPool connectionPool = null;

    private ConnectionPool() {
        pool = new ArrayBlockingQueue<>(maxSize);
        for (int i = 0; i < poolSize; i++) {
            pool.add(createConection());
        }
    }
        
    private static Connection createConection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + DBProperties.host + ":" + DBProperties.port + "/" + DBProperties.dbName;
            return new Connection(Jdbi.create(url, DBProperties.username, DBProperties.password).open());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static Connection getConnection() {
        if (connectionPool == null) {
            connectionPool = new ConnectionPool();
        }
        try {
            if (connectionPool.pool.isEmpty() && connectionPool.using.get() < connectionPool.maxSize) {
                connectionPool.using.incrementAndGet();
                return createConection();
            }

            var con = connectionPool.pool.take();
            connectionPool.using.incrementAndGet();
            return con;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public  static void realseConnection(Connection connection) {
        connectionPool.using.decrementAndGet();
        connectionPool.pool.add(connection);
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder()
                .append("Max=" + DBProperties.maxConnections)
                .append(" | Available=" + pool.size())
                .append(" | Busy=" + using.get());
        return sb.toString();
    }
    public static ConnectionPool getInstance() {
        return connectionPool;
    }
}
