package com.commenau.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class DBProperties {
    private static Properties properties = new Properties();
    static {
        try {
            properties.load(DBProperties.class.getClassLoader().getResourceAsStream("DB.properties"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static final String host = properties.getProperty("db.host");
    public static final String port = properties.getProperty("db.port");
    public static final String username = properties.getProperty("db.username");
    public static final String password = properties.getProperty("db.password");
    public static final String wardCode = properties.getProperty("delivery.wardCode");
    public static final String distinctCode = properties.getProperty("delivery.distinctCode");

    public static final String dbName = properties.getProperty("db.name");
    public static final String initialPoolSize = properties.getProperty("db.initialPoolSize");
    public static final String maxConnections = properties.getProperty("db.maxConnections");



}
