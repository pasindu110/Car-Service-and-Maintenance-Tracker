package com.cartracker.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Properties props = loadProperties();
            String url      = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("[DB] Connected to MySQL successfully.");

        } catch (Exception e) {
            throw new RuntimeException("[DB] Failed to connect: " + e.getMessage(), e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("[DB] Connection closed. Reconnecting...");
                instance = null;
                return getInstance().getConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException("[DB] Error checking connection state: " + e.getMessage(), e);
        }
        return connection;
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("[DB] db.properties not found on classpath.");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("[DB] Could not read db.properties: " + e.getMessage(), e);
        }
        return props;
    }
}
