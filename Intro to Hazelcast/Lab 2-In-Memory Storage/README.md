# Lab 2: In-Memory Storage

## Objectives

- Use SQL Shell to create a map, add data, and retrieve data 
- Add a second cluster member
- Use Management Center to monitor cluster operations
- Connect a client application to the Hazelcast cluster


## Part 1: SQL Shell

1. Use the following command to open the SQL shell.

- Linux/Mac
```
bin/hz-cli sql
```

- Windows
```
bin\hz-cli.bat sql
```

2. Complete the SQL Maps tutorial located here: https://docs.hazelcast.com/hazelcast/latest/sql/get-started-sql

## Part 2: Add A Cluster Member

1. Open a new terminal window and start a second instance of Hazelcast.
```
hz start
```
2. Open Management Center and observe as the second instance starts, the cluster forms, and distributes the stored data across both members.

3. In Management Center, go to Storage > Maps. Note that the entries from the maps you created in Part 1 are distributed across both members. 

## Part 3 (Optional): Connect a Client

1. Go to https://docs.hazelcast.com/hazelcast/latest/clients/hazelcast-clients and click on the link for instructions on setting up the client for your preferred language. 


