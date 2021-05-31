package com.hazelcast.solutions;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

import java.util.HashMap;
import java.util.Map;

public class SolutionClient2 {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Change the initialization and create a Hazelcast backed map
        Map<Integer, String> map = client.getMap("map");

        // Get key 42 from the map and store the value
        String result = map.get(42);

        // Print the result to the console
        System.out.println(result);
    }
}
