package com.training.introduction.lab1;

import com.hazelcast.core.Hazelcast;

public class StartHazelcastMember {

    // TODO: Run N instance of Hazelcast
    public static void main(String[] args) {
        Hazelcast.newHazelcastInstance();
    }
}
