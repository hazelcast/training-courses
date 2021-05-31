package com.hazelcast.solutions;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class SolutionMember2 {
    public static void main(String[] args) {
        // Starting a Hazelcast node by specifying a configuration file other than hazelcast.xml/yaml
        ClasspathXmlConfig config = new ClasspathXmlConfig("hazelcast-backup.xml");
        HazelcastInstance node1 = Hazelcast.newHazelcastInstance(config);
        HazelcastInstance node2 = Hazelcast.newHazelcastInstance(config);
        // Replace ConcurrentHashMap with a Hazelcast backed map
        Map<Integer, String> map = node1.getMap("isbank");
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
