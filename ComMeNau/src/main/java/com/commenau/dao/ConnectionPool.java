package com.commenau.dao;

import com.commenau.util.DBProperties;
import org.jdbi.v3.core.ConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ConnectionPool implements ConnectionFactory {
    private static ConnectionPool pool;
    private List<Connection> connectionPool;
    private int usingConnectionNumber;

    private ConnectionPool(List<Connection> connectionPool) {
        this.connectionPool = connectionPool;
        this.usingConnectionNumber = 0;
    }

    public synchronized static ConnectionPool getInstance() {
        if (pool == null)
            pool = createPool();
        return pool;
    }

    private static ConnectionPool createPool() {
        int poolSize = Integer.parseInt(DBProperties.initialPoolSize);
        List<Connection> pool = new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            pool.add(createConnection());
        }
        return new ConnectionPool(pool);
    }

    private static Connection createConnection() {

        String url = "jdbc:mysql://" + DBProperties.host + ":" + DBProperties.port + "/" + DBProperties.dbName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, DBProperties.username, DBProperties.password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Connection getConnection() {
        int maxSize = Integer.parseInt(DBProperties.maxConnections);
        if (connectionPool.isEmpty()) {
            if (usingConnectionNumber < maxSize) {
                connectionPool.add(createConnection());
            } else {
                while (connectionPool.isEmpty()) {
                    // Wait for an existing connection to be freed up.
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usingConnectionNumber++;
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        try {
            if (connection.isClosed()) {
                connection = createConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connectionPool.add(connection);
        usingConnectionNumber--;
        notifyAll();
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder()
                .append("Max=" + DBProperties.maxConnections)
                .append(" | Available=" + connectionPool.size())
                .append(" | Busy=" + usingConnectionNumber);
        return sb.toString();
    }

    @Override
    public Connection openConnection() {
//        System.out.println("openConnection");
        return getConnection();
    }

    @Override
    public void closeConnection(Connection connection) {
        if (connection != null) {
//            System.out.println("closeConnection");
            releaseConnection(connection);
        }
    }

}

//class ConnectionPool2 implements ConnectionFactory {
//    private static ConnectionPool2 pool;
//    private BlockingQueue<Connection> availableConnections;
//    private int usingConnectionNumber;
//
//    private ConnectionPool2(BlockingQueue<Connection> connectionPool) {
//        this.availableConnections = connectionPool;
//        this.usingConnectionNumber = 0;
//    }
//
//    public synchronized static ConnectionPool2 getInstance() {
//        if (pool == null)
//            pool = createPool();
//        return pool;
//    }
//
//    private static ConnectionPool2 createPool() {
//        int initPoolSize = Integer.parseInt(DBProperties.initialPoolSize);
//        BlockingQueue<Connection> pool = new ArrayBlockingQueue<>(initPoolSize);
//        for (int i = 0; i < initPoolSize; i++) {
//            pool.add(createConnection());
//        }
//        return new ConnectionPool2(pool);
//    }
//
//    private static Connection createConnection() {
//        String url = "jdbc:mysql://" + DBProperties.host + ":" + DBProperties.port + "/" + DBProperties.dbName;
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            return DriverManager.getConnection(url, DBProperties.username, DBProperties.password);
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private Connection getConnection() {
//        int maxConnections = Integer.parseInt(DBProperties.maxConnections);
//        if (availableConnections.isEmpty() && usingConnectionNumber < maxConnections) {
//            availableConnections.add(createConnection());
//        }
//        try {
//            Connection connection = availableConnections.take();
//            usingConnectionNumber++;
//            return connection;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private void releaseConnection(Connection connection) {
//        try {
//            if (connection.isClosed()) {
//                connection = createConnection();
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            availableConnections.put(connection);
//            usingConnectionNumber--;
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public synchronized String toString() {
//        StringBuilder sb = new StringBuilder()
//                .append("Max=" + DBProperties.maxConnections)
//                .append(" | Available=" + availableConnections.size())
//                .append(" | Busy=" + usingConnectionNumber);
//        return sb.toString();
//    }
//
//    @Override
//    public Connection openConnection() {
////        System.out.println("openConnection");
//        return getConnection();
//    }
//
//    @Override
//    public void closeConnection(Connection connection) {
//        if (connection != null) {
////            System.out.println("closeConnection");
//            releaseConnection(connection);
//        }
//    }
//
//
//}
