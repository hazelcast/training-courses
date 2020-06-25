package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.impl.predicates.SqlPredicate;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Client {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Create a Hazelcast backed map
        IMap<Integer, UserModel> map = client.getMap("training");

        // Generate 10,000 UsersModel instances of varying ages (see UserModel.fake)

        // Use a predicate to retrieve the UserModel instances with ages between 18 and 21

        //  Print out the results


        // shut down the client
        client.shutdown();
    }
}
