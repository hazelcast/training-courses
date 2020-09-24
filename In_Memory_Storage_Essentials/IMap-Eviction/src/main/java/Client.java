import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import static com.hazelcast.client.impl.spi.impl.discovery.HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY;
import static com.hazelcast.client.properties.ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN;
import static com.hazelcast.client.properties.ClientProperty.STATISTICS_ENABLED;

public class Client {
    public static void main(String[] args) {
        // Create Hazelcast instance which is backed by a client
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLOUD_DISCOVERY_TOKEN");
        config.setClusterName("YOUR_CLUSTER_NAME");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        // Create a Hazelcast backed map
        IMap<Integer, String> map = client.getMap("training-eviction");


        // Write elements to the map
        for (int i = 0; i < 25000; i++) {
            int key = i;
            String value = String.valueOf(i);

            // Put the entry into the map
            map.put(key, value);
        }

        // Checking map size to observe eviction
        System.out.println("Map size after eviction: "+map.size());
    }



}

