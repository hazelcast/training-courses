import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class MapWriter {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        // add your cloud token and cluster name. If using a local cluster, comment these linees out
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLOUD_TOKEN");
        config.setClusterName("YOUR_CLUSTER_NAME");

        HazelcastInstance hz = HazelcastClient.newHazelcastClient(config);

        //create map, add entry, update entry, delete entry
        IMap<String, String> map = hz.getMap("someMap");
        String key = "" + System.nanoTime();
        String value = "1";
        map.put(key, value);
        map.put(key, "2");
        map.delete(key);

        System.exit(0);
    }
}