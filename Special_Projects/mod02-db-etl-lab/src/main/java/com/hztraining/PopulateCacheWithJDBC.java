package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
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

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        // TODO: Get a reference to the invmap map
        IMap<InventoryKey, Inventory> invmap = null;
        // TODO: Get a reference to the invmap_indexed map
        IMap<InventoryKey, Inventory> invmapi = null;

        // Load database data into a Java HashMap
        InventoryTable table = new InventoryTable();
        List<Inventory> items = table.readAllFromDatabase(); // Less than 2 seconds to do this
        Map<InventoryKey, Inventory> localMap = new HashMap<>();
        for (Inventory item : items) {
            InventoryKey key = new InventoryKey(item.getSKU(), item.getLocation());
            localMap.put(key, item);
            //invmap.put(key, item); // NO - will take 30 minutes to load one-at-a-time this way!
        }
        try {
            // TODO: put data into the invmap map

            // TODO: put the same data into the invmap_indexed map

        } finally {
            client.shutdown();
        }
    }
}
