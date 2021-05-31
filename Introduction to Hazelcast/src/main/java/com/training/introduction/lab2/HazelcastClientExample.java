package com.training.introduction.lab2;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastClientExample {

    public static void main(String[] args) {
        // TODO: add client codes
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        IMap<Integer, Integer> map = client.getMap("sampleMap");

        for (int i = 10; i < 50; i++) {
            map.put(i, i);
        }
    }
}
