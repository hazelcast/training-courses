package hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.impl.predicates.SqlPredicate;

import java.util.Collection;
import java.util.Random;

public class Client {

    public static void main(String[] args) {
        // If you are using the cloud to host your cluster, make sure you add the client credentials!
        // Setting up cloud configuration
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLOUD_DISCOVERY_TOKEN");
        config.setClusterName("YOUR_CLUSTER_NAME");

        //adding Employee factory to populate map
        config.getSerializationConfig().addPortableFactoryClass(Employee.FACTORY_ID, Employee.EmployeeFactory.class);
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        // Create a Hazelcast backed map
        IMap<Integer, Employee> map = client.getMap("training-index");

        /**
         * Add a sorted index for salary to the map
         */

        // Write elements to the map
        System.out.print("Pushing data... ");

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Employee emp = new Employee(20 + new Random().nextInt(30), new Random().nextInt(5000));
            map.put(i, emp);
        }
        long delta1 = System.currentTimeMillis() - start1;
        System.out.println("done. " + delta1 + " ms");

        System.out.print("Querying data... ");
        long start2 = System.currentTimeMillis();

        // Querying all employees with a salary between 0 and 2000 and store
        // result in a collection called users
        SqlPredicate predicate = new SqlPredicate("salary between 0 and 2000");
        Collection<Employee> users = map.values(predicate);

        System.out.println("done.");

        // Printing out the result size
        for (Employee emp : users) {
            System.out.println(emp);
        }

        System.out.println("Total matches: " + users.size() + " out of " + map.size());
        System.out.println("Elapsed time for query " + (System.currentTimeMillis() - start2) + " ms");

        client.shutdown();
    }
}