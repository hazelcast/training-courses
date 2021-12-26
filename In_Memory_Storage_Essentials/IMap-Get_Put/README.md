# Basic Get/Put

## Objectives

* Use an instance of a Hazelcast client to add data to an IMap.
* Use a separate instance of a Hazelcast client to retrieve data from an IMap.

## Steps

1.	Open the files Client1 and Client2.

2.	Modify the configuration of both clients based on your lab environment.

     a.	If you are using a local cluster, comment out all the configuration lines
     
     b.	If you are using a cloud-based cluster, modify the config section to include your specific cloud credentials.

3.	In Client1, create a map in your cluster called `training`.

4.	Populate the map with integers. We’ve written the code to generate the integers for you; you just need to write the put statement to move the data into the map.

5.	Optional: Add a line that prints a message to the system output when the map is populated. 

6.	In Client2, reference the map called `training`.

>Note: you use the same syntax to create and to reference a data structure. If it doesn’t exist, Hazelcast will create it. If it does exist, Hazelcast will use the existing structure.

7.	Retrieve and store the entry with key value 42.

8.	Print the entry to the system output. 

## Extra Practice

1.	Overwrite a specific entry, then retrieve it to verify it was overwritten.

2.	Retrieve an entry, perform a transformation (a simple value + 1 works!), then put the entry back in the map. Verify that the transformed data was stored in the map.

3.	Write a loop that retrieves a selection of entries, performs a transformation, then puts the entries back in the map.

4.	Create and populate a new map with other data types (int, for example). Retrieve stored data. 



