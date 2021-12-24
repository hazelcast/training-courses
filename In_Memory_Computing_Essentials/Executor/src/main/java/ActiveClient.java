import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

public class ActiveClient {

    public static void main(String[] args) throws Exception {
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "xI8q1l7UomJsr5yrtITZXa9Kpok9QmK06uXeeBjxUSLDIvE4u3");
        config.setClusterName("pr-2830");

        // Pushing user code to cluster
        config.getUserCodeDeploymentConfig()
                .setEnabled(true)
                .addClass(EchoTask.class);

        HazelcastInstance hz = HazelcastClient.newHazelcastClient(config);

        IExecutorService executor = hz.getExecutorService("executor");

        for (int i = 1; i <= 1; i++) {
            Thread.sleep(1000);
            System.out.println("Producing echo task: " + i);
            executor.execute(new EchoTask("" + i));
        }
        System.out.println("MasterMember finished!");

        executor.execute(new EchoTask("foo"));
        executor.shutdown();
    }
}