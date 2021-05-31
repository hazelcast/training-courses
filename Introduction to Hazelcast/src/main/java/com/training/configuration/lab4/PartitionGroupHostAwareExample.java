package com.training.configuration.lab4;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class PartitionGroupHostAwareExample {

    public static void main(String[] args) {
        System.setProperty("hazelcast.config", "classpath:partition-host-aware-example-config.xml");
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Hazelcast.newHazelcastInstance();
        Hazelcast.newHazelcastInstance();
        Hazelcast.newHazelcastInstance();
        Hazelcast.newHazelcastInstance();

        IMap<Integer, Integer> map = hz.getMap("sampleMap");

        for (int i = 0; i < 10; i++) {
            map.put(i, i);
        }
    }
}
