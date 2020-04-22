package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;
import com.hztraining.inv.InventoryTable;

public class PopulateCacheWithJDBC {

    public static void main(String[] args) {

        // ConfigUtil is in the common module
        String configname = ConfigUtil.findConfigNameInArgs(args);
        ClientConfig config = ConfigUtil.getClientConfigForCluster(configname);

        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        IMap<InventoryKey, Inventory> invmap = client.getMap("invmap");

        long start = System.nanoTime();
        int counter=0;
        InventoryTable table = new InventoryTable();
        Iterable<InventoryKey> keys = table.loadAllKeys();
        for (InventoryKey key : keys) {
            Inventory item = table.load(key);
            invmap.put(key, item);
            counter++;
            if ((counter % 5000) == 0) {
                System.out.println("Loaded " + counter);
                System.out.println("   " + key + "=" + item);
            }

        }
        long finish = System.nanoTime();
        long elapsedNanos = finish - start;
        double elapsedSeconds = (double) elapsedNanos / 1_000_000_000D;

        System.out.printf("Finished in %3.3f seconds\n", elapsedSeconds);
        System.out.println("Final count " + counter);
        client.shutdown();
    }
}
