package com.hztraining.solutions;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.impl.predicates.SqlPredicate;
import com.hztraining.ConfigUtil;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;

import java.util.Collection;


public class QueryWithSQLPredicateSolution {

    private HazelcastInstance client;
    private static long start;

    public Collection<Inventory> queryNearbyStores(IMap<InventoryKey, Inventory> invmap, String item, String[] locations) {
        String locnKeys = String.join(", ", locations);
        SqlPredicate predicate = new SqlPredicate("sku=" + item +
                " AND location in (" + locnKeys + ") " +
                " AND quantity > 0");
        System.out.println("Query: " + predicate.toString() + " on map: " + invmap.getName());
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

        // Query using SQLPredicate
        QueryWithSQLPredicateSolution main = new QueryWithSQLPredicateSolution();
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
            long elapsed = System.currentTimeMillis() - start;
            System.out.printf("%d items matched unindexed query in %dms\n", nearby.size(), elapsed);
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
