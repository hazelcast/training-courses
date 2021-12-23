package hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class Member {

    public static void main(String[] args) {
        Config config = new Config();
        config.getUserCodeDeploymentConfig().setEnabled(true);

        HazelcastInstance member = Hazelcast.newHazelcastInstance(config);
    }
}

//for commit purposes only