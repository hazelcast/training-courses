package com.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.inv.Inventory;
import com.hazelcast.inv.InventoryKey;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.SqlPredicate;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class QueryWithSQLPredicate {

    private HazelcastInstance hazelcast;

    public Collection<Inventory> queryNearbyStores(String item, String[] locations) {
        IMap<InventoryKey, Inventory> invmap = hazelcast.getMap("invmap");
        SqlPredicate predicate = new SqlPredicate("location in a, b, c AND quantity > 0");
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
        String item = "Item0037";
        String[] stores = new String[] { "00021", "00032", "00077" };
        Collection<Inventory> nearby = main.queryNearbyStores(item, stores);
        for (Inventory i : nearby)
            System.out.println(i);


        main.hazelcast.shutdown();

    }
}
