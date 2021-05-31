package com.training.introduction.lab3;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class HazelcastEntryProcessorExample {

    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();

        IMap<Integer, Integer> map = hz.getMap("sampleMap");

        for (int i = 0; i < 10; i++) {
            map.put(i, i);
        }

        printMap("Current sampleMap's entries :", map);

        map.executeOnEntries(new IncrementingEntryProcessor());

        printMap("Processed with IncrementingEntryProcessor :", map);
    }

    public static void printMap(String s, IMap<Integer, Integer> map) {
        System.out.println(s + new ArrayList<>(map.values()).stream().sorted().collect(Collectors.toList()));
    }
}
