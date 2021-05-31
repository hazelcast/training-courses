package com.training.introduction.lab4;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.map.IMap;

import java.util.concurrent.Future;

public class HazelcastExecutorServiceExample {

    public static void main(String[] args) throws Exception {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();

        // Populating Map
        IMap<Integer, Integer> map = hz.getMap("sampleMap");
        for (int i = 0; i < 10; i++) {
            map.put(i, i);
        }

        // Creating an executor service on Hazelcast cluster
        IExecutorService executorService = hz.getExecutorService("sampleExecutorService");

        // Creating executor service task
        SumTask sumTask = new SumTask();
        sumTask.setHazelcastInstance(hz);

        // Submitting task to executor service
        Future<Integer> future = executorService.submit(sumTask);

        // TODO: run whatever you want while the task is executing

        // Getting results
        Integer result = future.get();
        System.out.println("Result: " + result);
    }

}
