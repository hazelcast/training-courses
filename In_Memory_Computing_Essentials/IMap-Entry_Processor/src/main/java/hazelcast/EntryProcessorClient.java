package hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class EntryProcessorClient {

    public static void main(String[] args) {
        // If you are using the cloud to host your cluster, make sure you add the client credentials!
        // Setting up cloud configuration
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "xI8q1l7UomJsr5yrtITZXa9Kpok9QmK06uXeeBjxUSLDIvE4u3");
        config.setClusterName("pr-2830");

        // Pushing user code to cluster
        config.getUserCodeDeploymentConfig()
                .setEnabled(true)
                .addClass(Employee.class)
                .addClass(SalaryIncreaseEntryProcessor.class);


        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        // Create a Hazelcast backed map
        IMap<String, Employee> employees = client.getMap("employees");

        // Add several Employees with unique keys and different salaries to the map
        employees.put("John", new Employee(1000));
        employees.put("Mark", new Employee(1000));
        employees.put("Spencer", new Employee(1000));

        /**
         * Using EP, increment the salary of each employee by a fixed integer value
         * */
        employees.executeOnEntries(new SalaryIncreaseEntryProcessor());

        // Read the salaries of all employees to see the change
        for (IMap.Entry<String, Employee> entry : employees.entrySet()) {
            System.out.println(entry.getKey() + " salary: " + entry.getValue().getSalary());
        }

        client.shutdown();
    }

}
//for commit purposes only