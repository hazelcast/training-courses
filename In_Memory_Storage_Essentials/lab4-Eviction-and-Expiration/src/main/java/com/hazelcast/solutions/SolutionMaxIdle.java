package com.hazelcast.solutions;

import com.hazelcast.core.EntryView;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.concurrent.TimeUnit;

public class SolutionMaxIdle {
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
        map.put(100, "persistent_session100", 0L, TimeUnit.SECONDS, 1L, TimeUnit.MINUTES);

        // Show the entry statistics of the last entry having higher max-idle-seconds
        EntryView entry = hz.getMap( "sessions" ).getEntryView( 100);
        System.out.println ( "size in memory  : " + entry.getCost() );
        System.out.println ( "creationTime    : " + entry.getCreationTime() );
        System.out.println ( "expirationTime  : " + entry.getExpirationTime() );
        System.out.println ( "number of hits  : " + entry.getHits() );
        System.out.println ( "lastAccessedTime: " + entry.getLastAccessTime() );
        System.out.println ( "lastUpdateTime  : " + entry.getLastUpdateTime() );
        System.out.println ( "version         : " + entry.getVersion() );
        System.out.println ( "key             : " + entry.getKey() );
        System.out.println ( "value           : " + entry.getValue() );

        // Check the size of the map every 5 seconds
        for (int i = 0; i < 15; i++) {
            Thread.sleep(5000L);
            System.out.println("Sessions map inde bulunan kayit sayisi: " + map.size());
        }

        Hazelcast.shutdownAll();
    }
}
