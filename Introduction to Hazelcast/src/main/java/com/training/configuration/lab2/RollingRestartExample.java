package com.training.configuration.lab2;

import com.hazelcast.core.Hazelcast;

public class RollingRestartExample {

    public static void main(String[] args) {
        System.setProperty("hazelcast.config", "classpath:production-config.xml");
        // TODO: Update data structure configuration with a rolling restart
        Hazelcast.newHazelcastInstance();

    }
}
