package com.hztraining;

import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
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

        // Aggregate total on hand across all locations
        String sku = "Item000123";
        Predicate predicate = Predicates.sql("sku=" + sku);
        long totalOnHand = invmap.aggregate(Aggregators.integerSum("quantity"), predicate);
        System.out.printf("Total on hand for %s = %d\n", sku, totalOnHand);

        // TODO: Aggregate average on hand for stores that have inventory
    }
}
