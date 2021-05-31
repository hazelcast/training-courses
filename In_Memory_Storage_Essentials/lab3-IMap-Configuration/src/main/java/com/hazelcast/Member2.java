package com.hazelcast;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Member2 {
    public static void main(String[] args) {
        // Start two Hazelcast nodes by specifying another declarative configuration file - hazelcast-backup.xml

        // Replace ConcurrentHashMap with a Hazelcast backed map - named `isbank`
        Map<Integer, String> map = new ConcurrentHashMap<>();
        // Write the 1000 elements to the map
        for (int i = 0; i < 1000; i++) {
            int key = i;
            String value = "value-" + i;

            // Add the given key and value to the map
            map.put(key, value);
        }

        // Get key 42 from the map and store the value
        String result = null;//remove null

        // Print the result to the console
        System.out.println(result);
    }
}
