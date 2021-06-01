package com.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.Map;

public class ClientStore {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Create a Hazelcast backed map
        IMap<Integer, String> training = client.getMap("training");

        System.out.println("Initial size of the map after loadAllKeys is invoked " + training.size());

        training.set(20, "value20");
        training.set(21, "value21");
        training.set(22, "value22");

        System.out.println("Size of the map after writing adding entries: " + training.size());

        training.evictAll();

        System.out.println("Size of the map after eviction: " + training.size());

        training.loadAll(true);

        System.out.println("Size of the map after the entries that are written to persistent data store (write-through) are loaded again (via loadAll(true)): " + training.size());
    }
}