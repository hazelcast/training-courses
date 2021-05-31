package com.hazelcast.solutions;

import com.hazelcast.EmployeeModel;
import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.impl.predicates.SqlPredicate;

import java.util.Map;

public class Client {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Create a Hazelcast backed employee
        IMap<Integer, EmployeeModel> employee = client.getMap("employee");

        // Generate 10,000 EmployeeModel instances of varying ages (see EmployeeModel.fake)
        for (int i = 0; i < 10000; i++) {
            EmployeeModel model = EmployeeModel.fake();
            employee.put(i, model);
        }

        // Use a built-in aggregator with a predicate to retrieve the average salary of EmployeeModel instances with age less than 30
        // SQL Predicate
        Predicate onlyLessThan30 = Predicates.lessThan("age", "30");
        double avgSalary1 = employee.aggregate(Aggregators.<Map.Entry<Integer, EmployeeModel>>integerAvg("salary"), new SqlPredicate("age < 30"));

        // Criteria API predicate
        double avgSalary2 = employee.aggregate(Aggregators.<Map.Entry<Integer, EmployeeModel>>integerAvg("salary"), onlyLessThan30);


        //  Print out the results
        int mapSize = employee.size();
        System.out.println("SQL Predicate average salary result: " + avgSalary1 + " out of " + mapSize);
        System.out.println("Criteria API average salary result: " + avgSalary2 + " out of " + mapSize);

        // shut down the client
        client.shutdown();
    }
}
