package com.hazelcast;

public class Member1 {
    public static void main(String[] args) {
        // Start two Hazelcast nodes with programmatic configuration

        // Create a Hazelcast backed map

        // Write the 1000 elements to the map
        for (int i = 0; i < 1000; i++) {
            int key = i;
            String value = "value-" + i;

            // Add the given key and value to the map

            // Get key 42 from the map and store the value
            String result = null;//remove null

            // Print the result to the console
            System.out.println(result);
        }
    }
}
