package Solution;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;

public class Member {

    public static void main(String[] args){
        Config config = new Config();

        MapConfig mapConfig = config.getMapConfig("training-eviction"); //<---- changed line to getter
        EvictionConfig evictionConfig = new EvictionConfig()
                /*
                 * Set the eviction config
                 */
                .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                .setSize(1000)
                .setEvictionPolicy(EvictionPolicy.LRU);

        mapConfig.setEvictionConfig(evictionConfig);

        Hazelcast.newHazelcastInstance(config);
    }
}
