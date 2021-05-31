package com.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class MemberMaxIdle {
    public static void main(String[] args) throws InterruptedException {
        // Start two Hazelcast nodes
        Hazelcast.newHazelcastInstance();
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Thread.sleep( 2000L);
        System.out.println("Hazelcast cluster was started. Number of members: " + hz.getCluster().getMembers().size());

        // Create a Hazelcast backed map named sessions
        IMap<Integer, String> map = hz.getMap("sessions");

        // Write some entries to the map
        for (int i = 0; i < 5; i++) {
            map.put(i, "session" + i);
        }

        // Write an entry by setting a higher max-idle-seconds than 30 seconds


        // Show the entry statistics of the last entry having higher max-idle-seconds


        // Check the size of the map every 5 seconds
        for (int i = 0; i < 15; i++) {
            Thread.sleep(5000L);
            System.out.println("Sessions map inde bulunan kayit sayisi: " + map.size());
        }

        Hazelcast.shutdownAll();
    }
}
