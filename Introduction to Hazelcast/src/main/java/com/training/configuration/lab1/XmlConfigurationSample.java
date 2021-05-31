package com.training.configuration.lab1;

import com.hazelcast.core.Hazelcast;

public class XmlConfigurationSample {

    public static void main(String[] args) {

        // TODO: import ClusterSettings.xml xml configuration in hazelcast.xml
        // TODO: try to set system property (eg: -Dhazelcast.diagnostics.enabled=true )
        Hazelcast.newHazelcastInstance();
    }
}
