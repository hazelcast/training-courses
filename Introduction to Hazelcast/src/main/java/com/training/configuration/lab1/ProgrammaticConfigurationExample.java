package com.training.configuration.lab1;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class ProgrammaticConfigurationExample {

    public static void main(String[] args) {

        Config config = new Config();
        config.setClusterName("development-cluster");

        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
    }
}
