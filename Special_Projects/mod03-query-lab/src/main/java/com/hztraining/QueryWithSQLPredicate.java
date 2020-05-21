package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;
import com.hazelcast.query.SqlPredicate;
import com.hztraining.inv.InventoryTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class QueryWithSQLPredicate {

    private HazelcastInstance client;
    private static long start;

    public Collection<Inventory> queryNearbyStores(IMap<InventoryKey, Inventory> invmap, String item, String[] locations) {
        String locnKeys = String.join(", ", locations);
        SqlPredicate predicate = new SqlPredicate("sku=" + item +
                " AND location in (" + locnKeys + ") " +
                " AND quantity > 0");
        System.out.println("Query: " + predicate.toString());
        start = System.currentTimeMillis();
        Collection<Inventory> results = invmap.values(predicate);
        return results;
    }

    public static void main(String[] args) {

        String configname = ConfigUtil.findConfigNameInArgs(args);
        ClientConfig config = ConfigUtil.getClientConfigForCluster(configname);

//        // We can instantiate here with no problem, but UCD fails to find the class
//        Inventory dummy = new Inventory();
//        System.out.println("Inventory " + dummy);

        // UCD working on Cloud now - but 55 seconds to query with no indexes!
        // UCD failes on-prem, ClassNotFoundException on InventoryTable
        //  - questionable why InventoryTable is even needed ?
        ClientUserCodeDeploymentConfig ucd = config.getUserCodeDeploymentConfig();
        ucd.setEnabled(true);
        ucd.addClass(Inventory.class);
        //ucd.addClass(InventoryTable.class); // local only, because defined as MapLoader

        // Query using SQLPredicate
        QueryWithSQLPredicate main = new QueryWithSQLPredicate();
        main.client = HazelcastClient.newHazelcastClient(config);

//        boolean useIndex = false;  // TODO: set via cli arg
//        if (useIndex) {
//            long start = System.currentTimeMillis();
//            // UnsupportedOperationException - client config only supports adding new data structures
//            // TODO: have separate map that is indexed
//            MapConfig invConfig = main.client.getConfig().getMapConfig("invmap");
//            List<MapIndexConfig> indexes = new ArrayList<>();
//            boolean ordered = false;
//            indexes.add(new MapIndexConfig("SKU", ordered));
//            indexes.add(new MapIndexConfig("location", ordered));
//            System.out.print((System.currentTimeMillis() - start) + " ms to add indexes");
//        }

        try {
            long start = System.currentTimeMillis();
            // We want to query whether an item is available at nearby stores
            // Items are in format Item + six digit item number, 0-999
            // Locations are 4 digit numeric, warehouses numbered 1-5, stores 101-150.
            String item = "Item000037";
            String[] stores = new String[]{"0121", "0132", "0106"};

            // Without index
            IMap<InventoryKey, Inventory> invmap = main.client.getMap("invmap");
            Collection<Inventory> nearby = main.queryNearbyStores(invmap, item, stores);
            long elapsed = System.currentTimeMillis() - start;
            System.out.printf("%d items matched query in %dms\n", nearby.size(), elapsed);
            for (Inventory i : nearby)
                System.out.println(i);

            // With index
            start = System.currentTimeMillis();
            IMap<InventoryKey, Inventory> invmapi = main.client.getMap("invmap_indexed");
            nearby = main.queryNearbyStores(invmapi, item, stores);
            elapsed = System.currentTimeMillis() - start;
            System.out.printf("%d items matched indexed query in %dms\n", nearby.size(), elapsed);
            for (Inventory i : nearby)
                System.out.println(i);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           main.client.shutdown();
        }

    }
}
