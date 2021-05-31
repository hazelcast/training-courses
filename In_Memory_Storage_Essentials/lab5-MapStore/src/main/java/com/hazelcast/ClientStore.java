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

        System.out.println("MapStore un loadAllKeys i ilk cagrildiktan sonraki kayit sayisi: " + training.size());

        training.set(20, "value20");
        training.set(21, "value21");
        training.set(22, "value22");

        System.out.println("Yeni kayitlar eklendikten sonraki kayit sayisi: " + training.size());

        training.evictAll();

        System.out.println("Eviction sonrasi kayit sayisi: " + training.size());

        training.loadAll(true);

        System.out.println("Database e write-through yazilan kayitlar loadAll yapildiginda: " + training.size());
    }
}