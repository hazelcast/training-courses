package Solutions;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.MapEvent;
import com.hazelcast.map.listener.*;

import static com.hazelcast.client.properties.ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN;
import static com.hazelcast.client.properties.ClientProperty.STATISTICS_ENABLED;

public class ListeningMember {

    public static void main( String[] args ) {
        // If you are using the cloud to host your cluster, make sure you add the client credentials!
        //Setting up cloud configuration
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLOUD_DISCOVERY_TOKEN");
        config.setClusterName("YOUR_CLUSTER_NAME");
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        // Create a Hazelcast backed map
        IMap<String, String> map = client.getMap( "training-l");

        /**
         * Add an EntryListener object to the IMap
         * */
        map.addEntryListener( new MyEntryListener(), true );
        System.out.println( "EntryListener registered" );
    }

    static class MyEntryListener implements
            EntryAddedListener<String, String>,
            EntryRemovedListener<String, String>,
            EntryUpdatedListener<String, String>,
            EntryEvictedListener<String, String>,
            EntryLoadedListener<String,String>,
            MapEvictedListener,
            MapClearedListener   {
        @Override
        public void entryAdded( EntryEvent<String, String> event ) {

            System.out.println( "Entry Added:" + event );
        }

        @Override
        public void entryRemoved( EntryEvent<String, String> event ) {

            System.out.println( "Entry Removed:" + event );
        }

        @Override
        public void entryUpdated( EntryEvent<String, String> event ) {

            System.out.println( "Entry Updated:" + event );
        }

        @Override
        public void entryEvicted( EntryEvent<String, String> event ) {

            System.out.println( "Entry Evicted:" + event );
        }

        @Override
        public void entryLoaded( EntryEvent<String, String> event ) {

            System.out.println( "Entry Loaded:" + event );
        }

        @Override
        public void mapEvicted( MapEvent event ) {

            System.out.println( "Map Evicted:" + event );
        }

        @Override
        public void mapCleared( MapEvent event ) {

            System.out.println( "Map Cleared:" + event );
        }
    }
}