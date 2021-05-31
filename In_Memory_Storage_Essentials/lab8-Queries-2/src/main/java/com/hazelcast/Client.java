package com.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class Client {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Create a Hazelcast backed map
        IMap<Integer, EmployeeModel> map = client.getMap("employee");

        // Generate 10,000 EmployeeModel instances of varying ages (see EmployeeModel.fake)

        // Use a predicate to retrieve the average salary of EmployeeModel instances with age less than 30

        //  Print out the results


        // shut down the client
        client.shutdown();
    }
}
