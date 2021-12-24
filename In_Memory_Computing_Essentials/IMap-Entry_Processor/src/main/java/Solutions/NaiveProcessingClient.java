package Solutions;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import hazelcast.Employee;

public class NaiveProcessingClient {

    public static void main(String[] args) {
        // If you are using the cloud to host your cluster, make sure you add the client credentials!
        //Setting up cloud configuration
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLOUD_DISCOVERY_TOKEN");
        config.setClusterName("YOUR_CLUSTER_NAME");

        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        // Create a Hazelcast backed map
        IMap<String, Employee> employees = client.getMap("training-np");

        // Add several Employees with unique keys and different salaries to the map
        employees.put("John", new Employee(1000));
        employees.put("Mark", new Employee(1000));
        employees.put("Spencer", new Employee(1000));

        /**
         * Without using EP, increment the salary of each employee
         * by a fixed integer value*/
        for (IMap.Entry<String, Employee> entry : employees.entrySet()) {
            String id = entry.getKey();
            Employee employee = employees.get(id);

            employee.incSalary(10);
            employees.put(entry.getKey(), employee);
        }

        // Read the salaries of all employees to see the change
        for (IMap.Entry<String, Employee> entry : employees.entrySet()) {
            System.out.println(entry.getKey() + " salary: " + entry.getValue().getSalary());
        }

        client.shutdown();
    }

}
//for commit purposes only