import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.Random;

public class FillMapClient {
    public static void main(String[] args) throws Exception {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress("127.0.0.1");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        System.out.println("Connection Successful!");
        System.out.println("--------------------");
        mapExample(client);
    }

        private static void mapExample(HazelcastInstance client) {
            System.out.println("Now the map named 'map' will be filled with random entries.");

            IMap<String, String> map = client.getMap("RandomNumbers");
            Random random = new Random();
            int iterationCounter = 0;
            while (true) {
                int randomKey = random.nextInt(100_000);
                map.put("key-" + randomKey, "value-" + randomKey);
                map.get("key-" + random.nextInt(100_000));
                if (++iterationCounter == 10) {
                    iterationCounter = 0;
                    System.out.println("Current map size: " + map.size());
                }
            }
        }
    }
