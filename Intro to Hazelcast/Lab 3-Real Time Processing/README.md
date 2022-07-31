
# Lab 3: Real Time Processig

## Objectives

- Create a simple pipeline in Java and submit it to the Hazelcast Platform
- Use SQL to get data from a Kafka topic and display the results of a query 
- Use SQL to create a job that writes data from a Kafka topic to an in-memory map

## Part 1: Building and Submitting a Pipeline

Go to https://docs.hazelcast.com/hazelcast/latest/pipelines/stream-processing-client and complete the tutorial. 

Note: If you already have your cluster and Management Center running, you can skip those startup steps. 

Not a Java programmer? No problem. Download the file EvenNumbers.jar and enter the following command:

hz-cli submit --class org.example.EvenNumberStream EvenNumbers.jar

This submits the already-compiled pipeline to Hazelcast. From here, you can proceed with using Management Center to monitor the job. 


## Part 2: SQL with Streaming

1. Go to https://docs.hazelcast.com/hazelcast/latest/sql/learn-sql and complete the tutorial. 
