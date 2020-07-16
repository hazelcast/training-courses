package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class SimpleSetGetTest {

    public static void main(String[] args) {

        String configname = ConfigUtil.findConfigNameInArgs(args);
        ClientConfig config = ConfigUtil.getClientConfigForCluster(configname);

        // TODO: Override the config read from ConfigUtil with a hand-coded version
        // Required attributes:
        //      GroupConfig - including name and password
        //      Discovery Token -
        // See ClientConfig javadoc:
        //  https://docs.hazelcast.org/docs/latest/javadoc/com/hazelcast/client/config/ClientConfig.html
        // See IMDG Reference Manual:
        //  https://docs.hazelcast.org/docs/latest-dev/manual/html-single/#configuring-java-client

        // TODO: student uncomments and fixes up this part ...
//        config = new ClientConfig();
//        GroupConfig gconfig = new GroupConfig("training", "PASSWORD");
//        config.setGroupConfig(gconfig);
//        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "TODO");

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
