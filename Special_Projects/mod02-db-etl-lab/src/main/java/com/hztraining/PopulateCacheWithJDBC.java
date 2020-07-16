package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.config.SerializationConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hztraining.inv.IDSFactory;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;
import com.hztraining.inv.InventoryTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopulateCacheWithJDBC {

    public static void main(String[] args) {

        // ConfigUtil is in the common module
        String configname = ConfigUtil.findConfigNameInArgs(args);
        ClientConfig clientConfig = ConfigUtil.getClientConfigForCluster(configname);

        // This isn't needed for the non-indexed case; but when building indexes
        // the cluster member must be able to deserialize the data in order
        // to extract field values that are to be indexed.
        // (This will work for on-premise as well as cloud; but for on-premise
        //  an alternative is simply to have the classes on the classpath of the
        //  members)
        ClientUserCodeDeploymentConfig ucd = clientConfig.getUserCodeDeploymentConfig();
        ucd.setEnabled(true);
        ucd.addClass(Inventory.class);
        //ucd.addClass(InventoryKey.class);
        //ucd.addClass(IDSFactory.class);

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

//        System.out.println("Configuring indexed version of map");
//        MapConfig indexedConfig = new MapConfig();
//        indexedConfig.setName("invmap_indexed");
//        List<MapIndexConfig> indexConfigs = new ArrayList<>();
//        indexConfigs.add(new MapIndexConfig("SKU", false));
//        indexConfigs.add(new MapIndexConfig("location", false));
//        indexedConfig.setMapIndexConfigs(indexConfigs);

        // TODO: Get a reference to the invmap map
        IMap<InventoryKey, Inventory> invmap = client.getMap("invmap");
        // TODO: Get a reference to the invmap_indexed map
        IMap<InventoryKey, Inventory> invmapi = client.getMap("invmap_indexed");

        long start = System.nanoTime();
        int counter=0;
        InventoryTable table = new InventoryTable();
        List<Inventory> items = table.readAllFromDatabase(); // Less than 2 seconds to do this
        Map<InventoryKey, Inventory> localMap = new HashMap<>();
        for (Inventory item : items) {
            InventoryKey key = new InventoryKey(item.getSKU(), item.getLocation());
            localMap.put(key, item);
            //invmap.put(key, item); // NO - will take 30 minutes to load one-at-a-time this way!
            counter++;
        }
        try {
            System.out.println("Starting putAll");
            // TODO: put data into the invmap map
            invmap.putAll(localMap);
            long finish = System.nanoTime();
            long elapsedNanos = finish - start;
            double elapsedSeconds = (double) elapsedNanos / 1_000_000_000D;
            System.out.printf("Finished unindexed map in %3.3f seconds [includes db fetch]\n", elapsedSeconds);
            // Note above includes retrieval from DB so not a straight comparison with put to
            // indexed map -- but even making the first measurement include db access it is still
            // significantly faster than when adding the indexes.

            start = System.nanoTime();
            // TODO: put data into the invmap_indexed map
            invmapi.putAll(localMap);
            finish = System.nanoTime();
            elapsedNanos = finish - start;
            elapsedSeconds = (double) elapsedNanos / 1_000_000_000D;
            System.out.printf("Finished indexed map in %3.3f seconds\n", elapsedSeconds);
            System.out.println("Final entry count " + counter);
        } finally {
            client.shutdown();
        }
    }
}
