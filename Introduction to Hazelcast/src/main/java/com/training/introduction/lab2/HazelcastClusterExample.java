package com.training.introduction.lab2;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastClusterExample {

    // TODO: Check backups via Hazelcast Management Center
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Hazelcast.newHazelcastInstance();
        Hazelcast.newHazelcastInstance();

        IMap<Integer, Integer> map = hz.getMap("sampleMap");

        for (int i = 0; i < 10; i++) {
            map.put(i, i);
        }
    }

}
