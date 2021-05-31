package com.hazelcast.solutions;

import com.hazelcast.UserModel;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

public class Client {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient();

        // Create a Hazelcast backed map
        IMap<Integer, UserModel> map = client.getMap("training");

        // Generate 10,000 UsersModel instances of varying ages (see UserModel.fake)
        for (int i = 0; i < 10000; i++) {
            UserModel model = UserModel.fake();
            map.put(i, model);
        }

        // Use a predicate to retrieve the UserModel instances with ages between 18 and 21
        // SQL Predicate
        Predicate p1 = Predicates.sql("age between 18 and 21");
        // Criteria API predicate
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

        // Bonus! + PagingPredicate Ordering
        Comparator<Map.Entry<Integer, UserModel>> ageComparator = new AgeComparator();

        PagingPredicate pagingPredicate = Predicates.pagingPredicate(p1, ageComparator, 10);

        Collection<UserModel> values = map.values(pagingPredicate);

        System.out.println("### PagingPredicate first ordered page ###");
        for (UserModel model : values) {
            System.out.println(model);
        }

        pagingPredicate.nextPage();

        System.out.println("### PagingPredicate second ordered page ###");
        for (UserModel model : values) {
            System.out.println(model);
        }

        // shut down the client
        client.shutdown();
    }

    // it's important for a comparator to be Serializable to use it in a multi-node cluster
    public static class AgeComparator implements Serializable, Comparator<Map.Entry<Integer, UserModel>> {

        @Override
        public int compare(Map.Entry<Integer, UserModel> o1, Map.Entry<Integer, UserModel> o2) {
            UserModel s1 = o1.getValue();
            UserModel s2 = o2.getValue();
            return s1.getAge() - s2.getAge();
        }

    }
}
