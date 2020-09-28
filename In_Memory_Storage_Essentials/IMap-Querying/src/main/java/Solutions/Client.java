package Solutions;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import java.util.Collection;
import java.util.Random;
import hazelcast.Employee;

import com.hazelcast.query.impl.predicates.SqlPredicate;

import static com.hazelcast.client.properties.ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN;
import static com.hazelcast.client.properties.ClientProperty.STATISTICS_ENABLED;

public class Client {
    public static void main(String[] args) {
        // If you are using the cloud to host your cluster, make sure you add the client credentials!
        //Setting up cloud configuration
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLOUD_DISCOVERY_TOKEN");
        config.setClusterName("YOUR_CLUSTER_NAME");

        // Making hazelcast.Employee class available at the Cloud side through User Code Deployment
        ClientUserCodeDeploymentConfig clientUserCodeDeploymentConfig = new ClientUserCodeDeploymentConfig();
        clientUserCodeDeploymentConfig.addClass(hazelcast.Employee.class);
        clientUserCodeDeploymentConfig.setEnabled(true);
        config.setUserCodeDeploymentConfig(clientUserCodeDeploymentConfig);

        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        // Create a Hazelcast backed map
        IMap<Integer, Employee> map = client.getMap("training-queries");

        // Write elements to the map
        System.out.print("Pushing data... ");
        long start1 = System.currentTimeMillis();
        for (int i=0; i<100; i++) {
            Employee emp = new Employee(20 + new Random().nextInt(30), new Random().nextInt(5000));
            map.put(i, emp);
        }
        long delta1 = System.currentTimeMillis() - start1;
        System.out.println("done."+ delta1 + " ms");

        System.out.print("Querying data... ");
        long start2 = System.currentTimeMillis();
        // Use a predicate to retrieve the employees with a salary between 0 and 2000
        /**
         * SQL Predicate
         * */
        SqlPredicate p1 = new SqlPredicate("salary between 0 and 2000");
        /**
         * Boolean predicate
         * */
        PredicateBuilder.EntryObject e = Predicates.newPredicateBuilder().getEntryObject();
        Predicate p2 = e.get("salary").between(0, 2000);

        // Either predicate here should return same result
        Collection<Employee> matches = map.values(p2);

        System.out.println("done.");

        //  Print out the results
        for (Employee emp : matches) {
            System.out.println(emp+" with a salary of: "+ emp.getSalary());
        }
        System.out.println("Total matches: " + matches.size() + " out of " + map.size());
        System.out.println("Elapsed time for query " + (System.currentTimeMillis() - start2) + "ms");


        // shut down the client
        client.shutdown();
    }
}