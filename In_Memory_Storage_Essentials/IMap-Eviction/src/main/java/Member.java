import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;

public class Member {

    public static void main(String[] args){
        Config config = new Config();

        MapConfig mapConfig = config.getMapConfig("training-eviction"); //<---- changed line to getter
        EvictionConfig evictionConfig = new EvictionConfig();

        /*
         * Set the eviction config
         */

        mapConfig.setEvictionConfig(evictionConfig);

        Hazelcast.newHazelcastInstance(config);
    }
}
