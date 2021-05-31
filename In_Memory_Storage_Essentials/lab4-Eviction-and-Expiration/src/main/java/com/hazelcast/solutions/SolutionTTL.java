package com.hazelcast.solutions;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.concurrent.TimeUnit;

public class SolutionTTL {
    public static void main(String[] args) throws InterruptedException {
        // Start two Hazelcast nodes
        Hazelcast.newHazelcastInstance();
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Thread.sleep(2000L);
        System.out.println("Hazelcast cluster i baslatildi. Toplam member sayisi: " + hz.getCluster().getMembers().size());

        // Create a Hazelcast backed map named cities
        IMap<String, String> map = hz.getMap("cities");

        // Write some entries to the map
        map.put("Nerede yasamali","Izmir");
        map.put("Nerede kebap yemeli", "Adana");
        map.put("Nerede pide yemeli", "Trabzon");

        // Write an entry by setting a higher TTL than 10 seconds
        map.put("Neresi gecilmez", "Canakkale", 30L, TimeUnit.SECONDS);

        // Check the size of the map every 2 seconds

        for (int i = 0; i < 15; i++) {
            Thread.sleep(2000L);
            System.out.println("Cities map inde bulunan kayit sayisi: " + map.size());
        }

        Hazelcast.shutdownAll();
    }
}
