package com.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.inv.Inventory;
import com.hazelcast.inv.InventoryKey;


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
        client.shutdown();
    }
}
