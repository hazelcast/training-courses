package com.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class MemberMaxSizePerNode {
    public static void main(String[] args) throws InterruptedException {
        // Start two Hazelcast nodes
        Hazelcast.newHazelcastInstance();
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Thread.sleep( 2000L);
        System.out.println("Hazelcast cluster was started. Number of members: " + hz.getCluster().getMembers().size());

        // Create a Hazelcast backed map named apples
        IMap<Integer, String> map = hz.getMap("apples");

        // Write entries to the map higher than the eviction size.
        // Keep that in mind that the cluster has two members with max-size-policy PER_NODE


        // Check the size of the map
        System.out.println("Apples map inde bulunan kayit sayisi: " + map.size());

        Hazelcast.shutdownAll();
    }
}
