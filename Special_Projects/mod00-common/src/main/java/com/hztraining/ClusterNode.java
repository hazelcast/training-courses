package com.hztraining;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Properties;

/** Use for testing, isn't intended to be the way cluster is started when solutions complete */
public class ClusterNode {
    private static HazelcastInstance hazelcast;

    public static void main(String[] args) {
        hazelcast = Hazelcast.newHazelcastInstance();
        Properties properties = System.getProperties();
        // Debugging why class doesn't load in cluster ... it works fine here!
        String cp = properties.getProperty(("java.class.path"));
        System.out.println("CP = " + cp);
        try {
            Class clazz = Class.forName("com.hztraining.inv.Inventory");
            System.out.println("Loaded " + clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
