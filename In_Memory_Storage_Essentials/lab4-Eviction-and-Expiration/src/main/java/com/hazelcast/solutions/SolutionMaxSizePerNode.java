package com.hazelcast.solutions;

import com.hazelcast.core.EntryView;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.concurrent.TimeUnit;

public class SolutionMaxSizePerNode {
    public static void main(String[] args) throws InterruptedException {
        // Start two Hazelcast nodes
        Hazelcast.newHazelcastInstance();
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Thread.sleep( 2000L);
        System.out.println("Hazelcast cluster was started. Number of members: " + hz.getCluster().getMembers().size());

        // Create a Hazelcast backed map named apples
        IMap<Integer, String> map = hz.getMap("apples");

        // Write some entries to the map
        for (int i = 0; i < 10000; i++) {
            map.put(i, "apple" + i);
        }

        // Check the size of the map
        System.out.println("Apples map inde bulunan kayit sayisi: " + map.size());

        Hazelcast.shutdownAll();
    }
}
