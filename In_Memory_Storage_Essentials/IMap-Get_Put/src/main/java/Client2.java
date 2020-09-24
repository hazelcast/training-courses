import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import static com.hazelcast.client.properties.ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN;
import static com.hazelcast.client.properties.ClientProperty.STATISTICS_ENABLED;

public class Client2 {
    public static void main(String[] args) {
        // If you are using the cloud to host your cluster, make sure you add the client credentials!
        //Setting up cloud configuration
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLOUD_DISCOVERY_TOKEN");
        config.setClusterName("YOUR_CLUSTER_NAME");
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        /**
         * Create a Hazelcast backed map named "training"
         * */

        /**
         * Get key 42 from the map and store the value
         * */

        /**
         * Print the result to the console
         * */
    }
}
