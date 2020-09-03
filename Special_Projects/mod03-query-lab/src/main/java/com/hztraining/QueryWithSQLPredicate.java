package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

import java.util.Collection;

public class QueryWithSQLPredicate {

    private HazelcastInstance client;
    private static long start;

    public Collection<Inventory> queryNearbyStores(IMap<InventoryKey, Inventory> invmap, String item, String[] locations) {
        String locnKeys = String.join(", ", locations);
        Predicate predicate = Predicates.sql("sku=" + item +
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

        ClientUserCodeDeploymentConfig ucd = config.getUserCodeDeploymentConfig();
        ucd.setEnabled(true);
        ucd.addClass(Inventory.class);
        //ucd.addClass(InventoryTable.class); // local only, because defined as MapLoader

        // Query using SQLPredicate
        QueryWithSQLPredicate main = new QueryWithSQLPredicate();
        main.client = HazelcastClient.newHazelcastClient(config);

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
            for (Inventory i : nearby)
                System.out.println(i);

            // With index
            IMap<InventoryKey, Inventory> invmapi = main.client.getMap("invmap_indexed");
            nearby = main.queryNearbyStores(invmapi, item, stores);
            for (Inventory i : nearby)
                System.out.println(i);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           main.client.shutdown();
        }

    }
}
