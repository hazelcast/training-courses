package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;
import com.hazelcast.sql.SqlService;
import com.hazelcast.sql.SqlStatement;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;

import java.util.ArrayList;
import java.util.Collection;

public class QueryWithNewSQLEngine {

    private HazelcastInstance client;
    private static long start;

    public Collection<Inventory> queryNearbyStores(IMap<InventoryKey, Inventory> invmap, String item, String[] locations) {
        String locnKeys = String.join(" OR location = ", locations); // No IN support in 4.1 SQL Engine
        // TODO: pass expression to match sku (passed in 'item' parameter),
        //  locations (in locnkeys string we just built), and with quantity > 0
        String predicate = "TODO"; // TODO
        String query = "SELECT * FROM " + invmap.getName() + " WHERE " + predicate;
        SqlStatement stmt = new SqlStatement(query);
        System.out.println("Query: " + query);

        start = System.currentTimeMillis();
        Collection<Inventory> results = new ArrayList<>();
        SqlService sqlEngine = client.getSql();
        try (SqlResult result = sqlEngine.execute(stmt)) {
            for (SqlRow row : result) {
                Inventory ritem = new Inventory();
                ritem.setSku(row.getObject("sku"));
                ritem.setDescription(row.getObject("description"));
                ritem.setLocation(row.getObject("location"));
                ritem.setLocationType(row.getObject("locationType"));
                ritem.setQuantity(row.getObject("quantity"));
                results.add(ritem);
            }
        }
        return results;
    }

    public static void main(String[] args) {

        String configname = ConfigUtil.findConfigNameInArgs(args);
        ClientConfig config = ConfigUtil.getClientConfigForCluster(configname);

        ClientUserCodeDeploymentConfig ucd = config.getUserCodeDeploymentConfig();
        ucd.setEnabled(true);
        ucd.addClass(Inventory.class);
        ucd.addClass(InventoryKey.class);

        // Query using SQL
        QueryWithNewSQLEngine main = new QueryWithNewSQLEngine();
        main.client = HazelcastClient.newHazelcastClient(config);

        try {
            long start = System.currentTimeMillis();
            // We want to query whether an item is available at nearby stores
            // Items are in format Item + six digit item number, 0-999
            // Locations are 4 digit numeric, warehouses numbered 1-5, stores 101-150.
            String item = "Item000037";
            String[] stores = new String[]{"0121", "0132", "0106"};

            // Currently - cannot query HD map Without index
//            IMap<InventoryKey, Inventory> invmap = main.client.getMap("invmap");
//            Collection<Inventory> nearby = main.queryNearbyStores(invmap, item, stores);
//            long elapsed = System.currentTimeMillis() - start;
//            System.out.printf("%d items matched unindexed query in %dms\n", nearby.size(), elapsed);
//            for (Inventory i : nearby)
//                System.out.println(i);

            // With index
            start = System.currentTimeMillis();
            IMap<InventoryKey, Inventory> invmapi = main.client.getMap("invmap_indexed");
            Collection<Inventory> nearby = main.queryNearbyStores(invmapi, item, stores);
            long elapsed = System.currentTimeMillis() - start;
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
