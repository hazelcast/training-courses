package hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class NaiveProcessingClient {

    public static void main(String[] args) {
        // If you are using the cloud to host your cluster, make sure you add the client credentials!
        //Setting up cloud configuration
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLOUD_TOKEN");
        config.setClusterName("YOUR_CLUSTER_NAME");

        // Making Employee class available through User Code Deployment
        ClientUserCodeDeploymentConfig clientUserCodeDeploymentConfig = new ClientUserCodeDeploymentConfig()
                .addClass(hazelcast.Employee.class)
                .setEnabled(true);
        config.setUserCodeDeploymentConfig(clientUserCodeDeploymentConfig);

        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        // Create a Hazelcast backed map
        IMap<String, Employee> employees = client.getMap("employees");

        // Add several Employees with unique keys and different salaries to the map
        employees.put("John", new Employee(1000));
        employees.put("Mark", new Employee(1000));
        employees.put("Spencer", new Employee(1000));
        employees.put("Fred", new Employee(1000));
        employees.put("Alan", new Employee(1000));
        employees.put("Brad", new Employee(1000));
        employees.put("Carter", new Employee(1000));
        employees.put("Dougal", new Employee(1000));
        employees.put("Eric", new Employee(1000));
        employees.put("Finlay", new Employee(1000));
        employees.put("Greg", new Employee(1000));
        employees.put("Henry", new Employee(1000));
        employees.put("Ian", new Employee(1000));
        employees.put("Jacob", new Employee(1000));
        employees.put("Kaleb", new Employee(1000));
        employees.put("Lars", new Employee(1000));
        employees.put("Matt", new Employee(1000));
        employees.put("Nathan", new Employee(1000));
        employees.put("Oliver", new Employee(1000));
        employees.put("Paul", new Employee(1000));
        employees.put("Quinn", new Employee(1000));
        employees.put("Roger", new Employee(1000));
        employees.put("Sam", new Employee(1000));
        employees.put("Tyson", new Employee(1000));
        employees.put("Uther", new Employee(1000));
        employees.put("Victor", new Employee(1000));
        employees.put("Wayne", new Employee(1000));
        employees.put("Xavier", new Employee(1000));
        employees.put("Yves", new Employee(1000));
        employees.put("Zander", new Employee(1000));
        employees.put("Annika", new Employee(1000));
        employees.put("Beth", new Employee(1000));
        employees.put("Christine", new Employee(1000));
        employees.put("Debra", new Employee(1000));
        employees.put("Emily", new Employee(1000));
        employees.put("Fiona", new Employee(1000));
        employees.put("Gloria", new Employee(1000));
        employees.put("Helen", new Employee(1000));
        employees.put("Iona", new Employee(1000));
        employees.put("Janelle", new Employee(1000));
        employees.put("Kayla", new Employee(1000));
        employees.put("Laura", new Employee(1000));
        employees.put("Michelle", new Employee(1000));
        employees.put("Natalie", new Employee(1000));
        employees.put("Ophelia", new Employee(1000));
        employees.put("Patrice", new Employee(1000));
        employees.put("Queen", new Employee(1000));
        employees.put("Renee", new Employee(1000));
        employees.put("Sarah", new Employee(1000));
        employees.put("Tabitha", new Employee(1000));
        employees.put("Ursula", new Employee(1000));
        employees.put("Valerie", new Employee(1000));
        employees.put("Winona", new Employee(1000));
        employees.put("Xenia", new Employee(1000));
        employees.put("Yolanda", new Employee(1000));
        employees.put("Zelda", new Employee(1000));

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