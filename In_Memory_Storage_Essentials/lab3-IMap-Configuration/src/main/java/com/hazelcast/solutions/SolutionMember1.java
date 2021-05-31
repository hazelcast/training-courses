package com.hazelcast.solutions;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class SolutionMember1 {
    public static void main(String[] args) {
        // Start two Hazelcast nodes with programmatic configuration
        Config config = new Config();

        JoinConfig joinConfig = config.getNetworkConfig().getJoin();
        joinConfig.getTcpIpConfig().setEnabled(true).addMember("127.0.0.1");

        HazelcastInstance node1 = Hazelcast.newHazelcastInstance(config);
        HazelcastInstance node2 = Hazelcast.newHazelcastInstance(config);

        // Create a Hazelcast backed map
        Map<Integer, String> map = node1.getMap("map");

        // Write the 1000 elements to the map
        for (int i = 0; i < 1000; i++) {
            int key = i;
            String value = "value-" + i;

            // Add the given key and value to the map
            map.put(key, value);

        }

        // Get key 42 from the map and store the value
        String result = map.get(42);

        // Print the result to the console
        System.out.println(result);
    }
}
