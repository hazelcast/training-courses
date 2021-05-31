package com.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class MemberTTL {
    public static void main(String[] args) throws InterruptedException {
        // Start two Hazelcast nodes
        Hazelcast.newHazelcastInstance();
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        Thread.sleep( 2000L);
        System.out.println("Hazelcast cluster was started. Number of members: " + hz.getCluster().getMembers().size());

        // Create a Hazelcast backed map named cities

        // Write some entries to the map

        // Write an entry by setting a higher TTL than 10 seconds

        // Check the size of the map every 2 seconds


        Hazelcast.shutdownAll();
    }
}
