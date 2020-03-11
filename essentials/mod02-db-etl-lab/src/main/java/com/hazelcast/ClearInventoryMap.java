package com.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.inv.Inventory;
import com.hazelcast.inv.InventoryDB;
import com.hazelcast.inv.InventoryKey;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.pipeline.BatchStage;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;

import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static com.hazelcast.jet.Util.entry;
import static java.util.Map.Entry;

public class ClearInventoryMap {

    public static void main(String[] args) {

        // CloudConfig and ConfigUtil are in the common module
        CloudConfig starterConfig = ConfigUtil.getPersonalClusterConfig();

        // IMDG configuration will be used within the pipeline to access Map remotely
        ClientConfig config = new ClientConfig();
        config.setGroupConfig(new GroupConfig(starterConfig.name, starterConfig.password));
        config.setProperty("hazelcast.client.statistics.enabled", "true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), starterConfig.discoveryToken);
        config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), starterConfig.urlBase);

        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        IMap<InventoryKey, Inventory> invMap = client.getMap("invmap");
        System.out.println("Map contained " + invMap.size() + " entries");
        invMap.clear();
        System.out.println("Map cleared, size now " + invMap.size());
        client.shutdown();
    }
}
