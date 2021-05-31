package com.training.configuration.lab3;

import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class DynamicConfigurationExample {

    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();

        IMap<Integer, Integer> map = hz.getMap("sampleMap");

        // TODO: change dynamically a data structure's configuration
//        MapConfig mapConfig = new MapConfig("sampleMap").setBackupCount(0);
//        hz.getConfig().addMapConfig(mapConfig);

    }
}
