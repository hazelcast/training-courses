package com.hztraining;

import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;

public class Aggregation {

    private HazelcastInstance hazelcast;


    public static void main(String[] args) {

        String configname = ConfigUtil.findConfigNameInArgs(args);
        ClientConfig config = ConfigUtil.getClientConfigForCluster(configname);

        Aggregation main = new Aggregation();
        main.hazelcast = HazelcastClient.newHazelcastClient(config);

        IMap<InventoryKey, Inventory> invmap = main.hazelcast.getMap("invmap");

        // Aggregate total on hand for an item
        // - todo: create a predicate, pass as second parameter to aggregate function
        // - might use
        long totalOnHand = invmap.aggregate(Aggregators.integerSum("quantity"));
        // Aggregate average on hand for stores that have inventory
        // Aggregate
        System.out.println("Totoal on hand " + totalOnHand);
    }
}
