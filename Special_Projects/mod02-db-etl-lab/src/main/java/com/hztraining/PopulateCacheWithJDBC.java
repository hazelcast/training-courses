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
import com.hztraining.inv.InventoryTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopulateCacheWithJDBC {

    public static void main(String[] args) {

        // ConfigUtil is in the common module
        String configname = ConfigUtil.findConfigNameInArgs(args);
        ClientConfig config = ConfigUtil.getClientConfigForCluster(configname);

        StringBuilder maria = new StringBuilder();
        maria.append(System.getProperty("user.home"));
        maria.append("/.m2/repository");
        maria.append("/org/mariadb/jdbc/mariadb-java-client");
        maria.append("/2.4.4");
        maria.append("/mariadb-java-client-2.4.4.jar");
        String mariaJar = maria.toString();

        // Without mariaJar we fail to load org.mariadb.jdbc.Driver
        // with mariaJar, we get further, but fail on another file (RowProtocol)
        // that is in the same jar

        // TODO: have to solve UCD inconsistency, mariaJar and InventoryTable will cause
        // failure on cloud.
        ClientUserCodeDeploymentConfig ucd = config.getUserCodeDeploymentConfig();
        ucd.setEnabled(true);
        //ucd.addJar(mariaJar);
        ucd.addClass(Inventory.class);
        //ucd.addClass(InventoryTable.class); // local only, because defined as MapLoader

        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        System.out.println("Configuring indexed version of map");
        MapConfig indexedConfig = new MapConfig();
        indexedConfig.setName("invmap_indexed");
        List<MapIndexConfig> indexConfigs = new ArrayList<>();
        indexConfigs.add(new MapIndexConfig("SKU", false));
        indexConfigs.add(new MapIndexConfig("location", false));
        indexedConfig.setMapIndexConfigs(indexConfigs);


        IMap<InventoryKey, Inventory> invmap = client.getMap("invmap");
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
        }
        try {
            System.out.println("Starting putAll");
            invmap.putAll(localMap);
            System.out.println("Finished with non-indexed map");
            invmapi.putAll(localMap);
            System.out.println("Done with putAll");
            long finish = System.nanoTime();
            long elapsedNanos = finish - start;
            double elapsedSeconds = (double) elapsedNanos / 1_000_000_000D;

            System.out.printf("Finished in %3.3f seconds\n", elapsedSeconds);
            System.out.println("Final count " + counter);
        } finally {
            client.shutdown();
        }
    }
}
