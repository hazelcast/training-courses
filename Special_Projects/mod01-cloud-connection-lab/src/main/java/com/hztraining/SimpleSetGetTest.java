package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

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

        // student uncomments and fixes up this part ...
//        config = new ClientConfig();
//        config.setClusterName("TODO");
//        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "TODO");


        // You can also look at ConfigUtil.getClientConfigForCluster for hints

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
