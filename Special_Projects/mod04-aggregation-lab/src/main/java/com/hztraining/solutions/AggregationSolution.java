package com.hztraining.solutions;

import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.impl.predicates.SqlPredicate;
import com.hztraining.ConfigUtil;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;

public class AggregationSolution {

    private HazelcastInstance hazelcastClient;


    public static void main(String[] args) {

        String configname = ConfigUtil.findConfigNameInArgs(args);
        ClientConfig config = ConfigUtil.getClientConfigForCluster(configname);

        AggregationSolution main = new AggregationSolution();
        main.hazelcastClient = HazelcastClient.newHazelcastClient(config);

        IMap<InventoryKey, Inventory> invmap = main.hazelcastClient.getMap("invmap");
        IMap<InventoryKey, Inventory> invmap_idx = main.hazelcastClient.getMap("invmap_indexed");

        // Aggregate total on hand across all locations
        String sku = "Item000123";
        // Note: Index is case-sensitive
        SqlPredicate predicate = new SqlPredicate("sku=" + sku);

        // TODO: accumulate the total quantity for value "quantity" for the selected item at all locations
        // See https://docs.hazelcast.org/docs/latest/manual/html-single/#built-in-aggregations
        long start = System.currentTimeMillis();
        long totalOnHand = invmap.aggregate(Aggregators.integerSum("quantity"), predicate);
        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("Total on hand for %s = %d (elapsed %d ms)\n", sku, totalOnHand, elapsed);

        start = System.currentTimeMillis();
        totalOnHand = invmap_idx.aggregate(Aggregators.integerSum("quantity"), predicate);
        elapsed = System.currentTimeMillis() - start;
        System.out.printf("Total on hand for %s = %d (elapsed %d ms using indexed map)\n", sku, totalOnHand, elapsed);

        main.hazelcastClient.shutdown();
    }
}
