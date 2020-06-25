package com.hztraining.solutions;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import com.hztraining.UserModel;

import java.util.Collection;

public class Client {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Create a Hazelcast backed map
        IMap<Integer, UserModel> map = client.getMap("training");

        // Generate 10,000 UsersModel instances of varying ages (see UserModel.fake)
        for (int i=0; i<10000; i++) {
            UserModel model = UserModel.fake();
            map.put(i, model);
        }

        // Use a predicate to retrieve the UserModel instances with ages between 18 and 21
        // SQL Predicate
        Predicate p1 = Predicates.sql("age between 18 and 21");
        // Boolean predicate
        PredicateBuilder.EntryObject e = Predicates.newPredicateBuilder().getEntryObject();
        Predicate p2 = e.get("age").between(18, 21);

        // Either predicate here should return same result
        long start = System.currentTimeMillis();
        Collection<UserModel> matches = map.values(p2);

        //  Print out the results
        for (UserModel model : matches) {
            System.out.println(model);
        }
        System.out.println("Total matches: " + matches.size() + " out of " + map.size());
        System.out.println("Elapsed time for query " + (System.currentTimeMillis() - start) + "ms");


        // shut down the client
        client.shutdown();
    }
}
