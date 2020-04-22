package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;
import com.hazelcast.query.SqlPredicate;

import java.util.Collection;


public class QueryWithSQLPredicate {

    private HazelcastInstance hazelcast;
    private static long start;

    public Collection<Inventory> queryNearbyStores(String item, String[] locations) {
        IMap<InventoryKey, Inventory> invmap = hazelcast.getMap("invmap");
        String locnKeys = String.join(", ", locations);
        // WORKS: SqlPredicate predicate = new SqlPredicate("sku=" + item);
        // WORKS: SqlPredicate predicate = new SqlPredicate("sku=" + item + " AND quantity > 0");
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

        ClientUserCodeDeploymentConfig ucd = config.getUserCodeDeploymentConfig();
        ucd.setEnabled(true);
        ucd.addClass(Inventory.class);
//        config.setUserCodeDeploymentConfig(ucd);

        // Query using SQLPredicate
        QueryWithSQLPredicate main = new QueryWithSQLPredicate();
        main.hazelcast = HazelcastClient.newHazelcastClient(config);

        try {
            // We want to query whether an item is available at nearby stores
            // Items are in format Item + six digit item number, 0-999
            // Locations are 4 digit numeric, warehouses numbered 1-5, stores 101-150.
            String item = "Item000037";
            String[] stores = new String[]{"0121", "0132", "0106"};
            Collection<Inventory> nearby = main.queryNearbyStores(item, stores);
            long elapsed = System.currentTimeMillis() - start;
            System.out.printf("%d items matched query in %dms\n", nearby.size(), elapsed);
            for (Inventory i : nearby)
                System.out.println(i);

        } finally {
            main.hazelcast.shutdown();
        }

    }
}
