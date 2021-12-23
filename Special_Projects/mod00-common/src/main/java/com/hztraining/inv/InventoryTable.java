package com.hztraining.inv;

import com.hazelcast.map.MapLoader;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Represents the Inventory table in the InventoryDB database */
public class InventoryTable
        implements MapLoader<InventoryKey, Inventory> {

    private Connection conn;

    private final static ILogger log = Logger.getLogger(InventoryTable.class);

    private static final DecimalFormat skuFormat = new DecimalFormat("000000"); // 6 digit
    private static final DecimalFormat locationFormat  = new DecimalFormat( "0000");    // 4 digit

    // Index positions
    private static final int SKU = 1;
    private static final int DESCRIPTION = 2;
    private static final int LOCATION = 3;
    private static final int LOCATION_TYPE = 4;
    private static final int QUANTITY = 5;

    private static final String insertTemplate =
            "insert into inventory (sku, description, location, loc_type, qty) " +
                    " values (?, ?, ?, ?, ?)";

    private static final String selectTemplate =
            "select sku, description, location, loc_type, qty from inventory where sku = ? and location = ?";

    private static final String selectAll =
            "select all sku, description, location, loc_type, qty from inventory";

    private PreparedStatement insertStatement;
    private PreparedStatement selectStatement;
    private PreparedStatement selectAllStatement;

    public synchronized void establishConnection()  {
        try {
            // Register the driver, we don't need to actually assign the class to anything
            Class.forName("org.mariadb.jdbc.Driver");
            String jdbcURL = "jdbc:mysql://localhost:3306/inventoryDB";
            //log.info("Attempting connection to " + jdbcURL + " for user " + BankInABoxProperties.JDBC_USER);
            conn = DriverManager.getConnection(
                    jdbcURL, "hzuser", "hzpass");
            log.info("Established connection to inventoryDB database");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void generateSampleData() {
        // Hard-coding some varlues here:
        // - There are 5 warehouse locations, numbered 1-5
        // - There are 50 store locations, number 101-150.
        // - Each location will get 1000 stock entries

        // Generate warehouse stock
        for (int i=1; i<=5; i++) {
            for (int j=0; j<1000; j++) {
                String SKU = "Item" + skuFormat.format(j);
                String location = locationFormat.format(i);
                Inventory item = new Inventory(SKU, "W", location);
                writeToDatabase(item);
            }
        }

        // Generate store stock
        for (int i=101; i<=150; i++) {
            for (int j=0; j<1000; j++) {
                String SKU = "Item" + skuFormat.format(j);
                String location = locationFormat.format(i);
                Inventory item = new Inventory(SKU, "S", location);
                writeToDatabase(item);
            }
        }
    }

    public synchronized void writeToDatabase(Inventory item) {
        try {
            if (insertStatement == null) {
                insertStatement = conn.prepareStatement(insertTemplate);
            }
            insertStatement.setString(SKU, item.getSku());
            insertStatement.setString(DESCRIPTION, item.getDescription());
            insertStatement.setString(LOCATION, item.getLocation());
            insertStatement.setString(LOCATION_TYPE, item.getLocationType());
            insertStatement.setInt(QUANTITY, item.getQuantity());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public synchronized Inventory readFromDatabase(String skuKey, String location) {
        if (skuKey == null || location == null) {
            log.warning("InventoryTable.readFromDatabase(): Passed null key, returning null");
            return null;
        }
        try {
            if (selectStatement == null) {
                selectStatement = conn.prepareStatement(selectTemplate);
            }
            selectStatement.setString(1, skuKey);
            selectStatement.setString(2, location);
            //log.info("readFromDatabase: " + selectStatement.toString());
            ResultSet rs = selectStatement.executeQuery();
            Inventory item = new Inventory();
            if (rs == null) {
                log.warning("InventoryTable.readFromDatabase(): Null resultSet trying to read SKU " + skuKey + " Locn " + location);
                return null;
            }
            while (rs.next()) {
                item.setSku(rs.getString(SKU));
                item.setDescription(rs.getString(DESCRIPTION));
                item.setLocation(rs.getString(LOCATION));
                item.setLocationType(rs.getString(LOCATION_TYPE));
                item.setQuantity(rs.getInt(QUANTITY));
            }
            return item;
        } catch (SQLException e) {
            log.info("Error in " + selectStatement.toString() + " --> " + e.getMessage());
            //e.printStackTrace();
            //System.exit(-1);
            return null;
        }
    }

    public synchronized List<Inventory> readAllFromDatabase() {
        try {
            if (conn == null)
                establishConnection();

            if (selectAllStatement == null) {
                selectAllStatement = conn.prepareStatement(selectAll);
            }

            ResultSet rs = selectAllStatement.executeQuery();
            List<Inventory> results = new ArrayList<>();

            if (rs == null) {
                log.warning("InventoryTable.readAllFromDatabase(): Null resultSet trying to select all");
                return null;
            }
            while (rs.next()) {
                Inventory item = new Inventory();
                item.setSku(rs.getString(SKU));
                item.setDescription(rs.getString(DESCRIPTION));
                item.setLocation(rs.getString(LOCATION));
                item.setLocationType(rs.getString(LOCATION_TYPE));
                item.setQuantity(rs.getInt(QUANTITY));
                results.add(item);
                if ((results.size() % 5000) == 0) {
                    System.out.println("Processed " + results.size() + " JDBC ResultSet entries");
                }
            }
            //log.info("InventoryTable.readAll selected " + results.size() + " records");
            return results;
        } catch (SQLException e) {
            log.info("Error in " + selectAllStatement.toString() + " --> " + e.getMessage());
            //e.printStackTrace();
            //System.exit(-1);
            return null;
        }
    }

    // MapLoader interface

    @Override
    public synchronized Inventory load(InventoryKey key) {
        if (conn == null)
            establishConnection();
        return readFromDatabase(key.sku(), key.location());
    }

    @Override
    public synchronized Map<InventoryKey, Inventory> loadAll(Collection<InventoryKey> collection) {
        //log.info("InventoryTable.loadAll() with " + collection.size() + " keys");
        if (conn == null)
            establishConnection();
        Map<InventoryKey,Inventory> results = new HashMap<>(collection.size());
        // NOTE: parallelStream here leads to SQLException in read database, so drop back here until we
        // can make that threadsafe. (Trying to use shared PreparedStatement with different parameters)
        collection.stream().forEach((InventoryKey key) -> {
            Inventory item = load(key);
            results.put(key, item);
        });
        return results;
    }

    @Override
    public synchronized Iterable<InventoryKey> loadAllKeys() {
        //log.info("loadAllKeys() on inventoryTable");
        if (conn == null)
            establishConnection();

        // Note this must be kept in sync with GenerateSampleData method
        int size = 100000; // Actually current scheme will produce 55K items
        List<InventoryKey> allKeys = new ArrayList<>(size);

        // Generate warehouse stock
        for (int i=1; i<=5; i++) {
            for (int j=0; j<1000; j++) {
                String SKU = "Item" + skuFormat.format(j);
                String location = locationFormat.format(i);
                InventoryKey key = new InventoryKey(SKU, location);
                allKeys.add(key);
            }
        }

        // Generate store stock
        for (int i=101; i<=150; i++) {
            for (int j=0; j<1000; j++) {
                String SKU = "Item" + skuFormat.format(j);
                String location = locationFormat.format(i);
                InventoryKey key = new InventoryKey(SKU, location);
                allKeys.add(key);
            }
        }

        log.info("MapLoader.loadAllKeys() on inventory table returning " + allKeys.size() + " keys");
        return allKeys;
    }
}
