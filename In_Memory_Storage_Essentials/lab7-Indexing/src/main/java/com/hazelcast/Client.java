package com.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.impl.predicates.SqlPredicate;

import java.util.Collection;

public class Client {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Create a Hazelcast backed map
        IMap<Integer, UserModel> map = client.getMap("training");

        // Write the 10000 elements to the map
        System.out.print("Pushing data... ");
        for (int i = 0; i < 10000; i++) {
            int key = i;
            UserModel value = UserModel.fake();

            // Put the entry into the map
            map.set(key, value);
        }
        System.out.println("done.");

        System.out.print("Querying data... ");
        long start = System.currentTimeMillis();

        // Querying all users with an age between 18 and 21 and store
        // result in a collection called users
        SqlPredicate predicate = new SqlPredicate("age between 18 and 21");
        Collection<UserModel> users = map.values(predicate);

        long delta = System.currentTimeMillis() - start;
        System.out.println("done. " + delta + " ms");

        // Printing out the result size
        System.out.println("Number of found results: " + users.size());
        client.shutdown();
    }
}
