import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;

public class Listener {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        // Add your cloud token and cluster name below. If using a local cluster, comment these lines out
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLUSTER_TOKEN");
        config.setClusterName("YOUR_CLUSTER_NAME");

        HazelcastInstance hz = HazelcastClient.newHazelcastClient(config);

        // Create map and add listener
        IMap<String, String> map = hz.getMap("someMap");
        map.addEntryListener(new MyEntryListener(), true);
        System.out.println("EntryListener registered");
    }

    // create listener - print event when entry added, updated, or deleted
    private static class MyEntryListener implements EntryAddedListener<String, String>,
            EntryRemovedListener<String, String>, EntryUpdatedListener<String, String> {
        @Override
        public void entryAdded(EntryEvent<String, String> event) {
            System.out.println("entryAdded: " + event);
        }

        @Override
        public void entryRemoved(EntryEvent<String, String> event) {
            System.out.println("entryRemoved: " + event);
        }

        @Override
        public void entryUpdated(EntryEvent<String, String> event) {
            System.out.println("entryUpdated: " + event);
        }
    }
}