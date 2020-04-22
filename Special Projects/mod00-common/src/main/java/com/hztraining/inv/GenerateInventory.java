package com.hztraining.inv;

import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

import java.util.concurrent.CompletableFuture;

/** Used to fill the InventoryDB with test data.  You should not need to run this unless
 * you have changed the database schema and want to generate new data, in which case you
 * need to update the Inventory and InventoryTable classes to reflect your changes and
 * then re-run this class to create new data.  Then use the 'dump' target in the
 * Makefile to create a backup of your new data, so that new database starts will load
 * the new test data.
 */
public class GenerateInventory {

    private final static ILogger log = Logger.getLogger(GenerateInventory.class);

    public static void main(String[] args) {
        InventoryDB database = new InventoryDB();
        database.establishConnection(); // connects to server, in a non-db-specific way
        // Database now pre-exists in the Docker image
        //database.createDatabase();

        /////////////// Merchants
        InventoryTable inventoryTable = new InventoryTable();
        inventoryTable.establishConnection();

        log.info("Generating inventory");
        CompletableFuture<Void> inventoryFuture = CompletableFuture.runAsync(() -> {
            inventoryTable.generateSampleData();
            log.info("Generated inventory sample data");
        });


        log.info("All launched, waiting on completion");
        CompletableFuture<Void> all = CompletableFuture.allOf(inventoryFuture);
        try {
            all.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("All complete.");
        System.exit(0);
    }
}
