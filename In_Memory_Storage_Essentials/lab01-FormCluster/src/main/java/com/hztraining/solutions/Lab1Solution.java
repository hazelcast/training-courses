package com.hztraining.solutions;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class Lab1Solution {
    public static void main(String[] args) {
        // Add necessary code to start up a Hazelcast instance and
        // run the application multiple times to cluster them up
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();

        // An embedded Hazelcast instance is now running and will continue until terminated

    }
}
