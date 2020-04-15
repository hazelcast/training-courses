package com.hazelcast;

import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.inv.Inventory;
import com.hazelcast.inv.InventoryKey;

public class Aggregation {

    private HazelcastInstance hazelcast;


    public static void main(String[] args) {

        // CloudConfig and ConfigUtil are in the common module
        CloudConfig starterConfig = ConfigUtil.getPersonalClusterConfig();

        // IMDG configuration will be used within the pipeline to access Map remotely
        ClientConfig config = new ClientConfig();
        config.setGroupConfig(new GroupConfig(starterConfig.name, starterConfig.password));
        config.setProperty("hazelcast.client.statistics.enabled", "true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), starterConfig.discoveryToken);
        config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), starterConfig.urlBase);

        // Query using SQLPredicate
        Aggregation main = new Aggregation();
        main.hazelcast = HazelcastClient.newHazelcastClient(config);

        IMap<InventoryKey, Inventory> invmap = main.hazelcast.getMap("invmap");

        // Aggregate total on hand for an item
        // - todo: create a predicate, pass as second parameter to aggregate function
        // - might use
        long totalOnHand = invmap.aggregate(Aggregators.integerSum("quantity"));
        // Aggregate average on hand for stores that have inventory
        // Aggregate
    }
}
