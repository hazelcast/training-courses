package com.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.inv.Inventory;
import com.hazelcast.inv.InventoryKey;
import com.hazelcast.query.SqlPredicate;

import java.util.Collection;


public class QueryWithSQLPredicate {

    private HazelcastInstance hazelcast;

    public Collection<Inventory> queryNearbyStores(String item, String[] locations) {
        IMap<InventoryKey, Inventory> invmap = hazelcast.getMap("invmap");
        String locnKeys = String.join(",", locations);
        SqlPredicate predicate = new SqlPredicate("location in (" + locnKeys +
                ") AND quantity > 0");
        System.out.println("Query: " + predicate.toString());
        Collection<Inventory> results = invmap.values(predicate);
        return results;
    }

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
        QueryWithSQLPredicate main = new QueryWithSQLPredicate();
        main.hazelcast = HazelcastClient.newHazelcastClient(config);

        // We want to query whether an item is available at nearby stores
        // Items are in format Item + six digit item number, 0-999
        // Locations are 4 digit numeric, warehouses numbered 1-5, stores 101-150.
        String item = "Item000037";
        String[] stores = new String[] { "0121", "0132", "0106" };
        Collection<Inventory> nearby = main.queryNearbyStores(item, stores);
        for (Inventory i : nearby)
            System.out.println(i);


        main.hazelcast.shutdown();

    }
}
