package com.hztraining.inv;

import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

import java.sql.*;

/** Represents the MariaDB Inventory database */
public class InventoryDB {
    private final static ILogger log = Logger.getLogger(InventoryDB.class);

    private Connection conn;

    private static final String createDatabaseString = "create database inventory";
    private static final String dropDatabaseString   = "drop database if exists inventory";

    protected synchronized void establishConnection()  {
        try {
            // Register the driver, we don't need to actually assign the class to anything
            Class.forName("org.mariadb.jdbc.Driver");
            String jdbcURL = "jdbc:mysql://localhost:3306";
            System.out.println("JDBC URL is " + jdbcURL);
            conn = DriverManager.getConnection(jdbcURL, getUser(), getPassword());
            log.info("Established connection to MySQL/MariaDB server");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static String getJdbcURL() {
        return "jdbc:mysql://localhost:3306/inventoryDB";
    }

    public static String getUser() { return "hzuser"; }
    public static String getPassword() { return "hzpass"; }

    protected synchronized void createDatabase() {
        if (conn == null) {
            throw new IllegalStateException("Must establish connection before creating the database!");
        }
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(dropDatabaseString);
            log.info("Dropped (if exists) database InventoryDB");
            stmt.executeUpdate(createDatabaseString);
            log.info("Created database InventoryDB");

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // For testing purposes
    public static void main(String[] args) {
        InventoryDB main = new InventoryDB();
        main.establishConnection();
        main.createDatabase();
    }
}



