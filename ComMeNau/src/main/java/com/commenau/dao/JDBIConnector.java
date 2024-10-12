package com.commenau.dao;

import org.jdbi.v3.core.ConnectionFactory;
import org.jdbi.v3.core.Jdbi;

public class JDBIConnector {
    private static Jdbi jdbi;

    private static void makeConnect() {
        ConnectionFactory pool = ConnectionPool.getInstance();
        jdbi = Jdbi.create(pool);
    }

    public static Jdbi getInstance() {
        if (jdbi == null) makeConnect();
        return jdbi;
    }

}