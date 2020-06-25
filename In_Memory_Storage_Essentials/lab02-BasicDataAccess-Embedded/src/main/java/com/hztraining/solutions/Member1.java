package com.hztraining.solutions;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class Member1 {
    public static void main(String[] args) {
        // Starting a Hazelcast node
        HazelcastInstance node = Hazelcast.newHazelcastInstance();

        // Create a Hazelcast backed map
        Map<Integer, String> map = node.getMap("map");


        // Write the 1000 elements to the map
        for (int i = 0; i < 1000; i++) {
            int key = i;
            String value = "value-" + String.valueOf(i);

            // Add the given key and value to the map
            map.put(key, value);

        }
    }
}
