package com.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class Main {

    public static void main(String[] args) {
        // CloudConfig and ConfigUtil are in the common module
        CloudConfig starterConfig = ConfigUtil.getPersonalClusterConfig();

        ClientConfig config = new ClientConfig();
        config.setGroupConfig(new GroupConfig(starterConfig.name, starterConfig.password));
        config.setProperty("hazelcast.client.statistics.enabled", "true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), starterConfig.discoveryToken);
        config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), starterConfig.urlBase);

        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        // Get or create a Hazelcast Map named "map".
        // Both the key type and value type will be Strings
        IMap<String, String> map = client.getMap("map");

        // Write an entry to the map
        map.set("key", "my-value");

        // Read the entry just written, display it to confirm it matches.
        System.out.println("Got value " + map.get("key"));

        // Shut down the client
        client.shutdown();
    }
}
