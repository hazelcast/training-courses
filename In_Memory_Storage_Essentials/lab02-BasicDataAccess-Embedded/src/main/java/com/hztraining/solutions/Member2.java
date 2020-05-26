package com.hztraining.solutions;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class Member2 {
    public static void main(String[] args) {
        // Starting a Hazelcast node
        HazelcastInstance node = Hazelcast.newHazelcastInstance();

        // Create a Hazelcast backed map
        Map<Integer, String> map = node.getMap("map");


        // Get key 42 from the map and store the value
        String result = map.get(42);

        // Print the result to the console
        System.out.println(result);
    }
}
