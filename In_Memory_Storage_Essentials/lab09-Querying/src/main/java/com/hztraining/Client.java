package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

public class Client {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Create a Hazelcast backed map
        Map<Integer, String> map = client.getMap("training");

        // Generate 10,000 UsersModel instances of varying ages (see UserModel.fake)

        // Use a predicate to retrieve the UserModel instances with ages between 18 and 21
        //  Print out the results


        // shut down the client
        client.shutdown();
    }


}
