package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;


public class ClearInventoryMap {

    public static void main(String[] args) {

        // ConfigUtil is in the common module
        String configname = ConfigUtil.findConfigNameInArgs(args);
        ClientConfig config = ConfigUtil.getClientConfigForCluster(configname);

        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        IMap<InventoryKey, Inventory> invMap = client.getMap("invmap");
        System.out.println("Map contained " + invMap.size() + " entries");
        invMap.clear();
        System.out.println("Map cleared, size now " + invMap.size());

        invMap = client.getMap("invmap");
        System.out.println("Indexed Map contained " + invMap.size() + " entries");
        invMap.clear();
        System.out.println("Indexed Map cleared, size now " + invMap.size());

        client.shutdown();
    }
}
