# Indexing

## Objectives

*	Populate an indexed data store in the Hazelcast cluster.
*	Create a query that pulls data from the data store.  

## Steps

1.	Modify the configuration and code loading section of the code based on your lab environment.

    a.	If you are using a local cluster, comment out all the configuration lines and the user code deployment lines.

    b.	If you are using a cloud-based cluster, modify the config section to include your specific cloud credentials.

> Note: The UserCodeDeployment section is needed to load the Employee data to the cloud-based instance.

2.	We’re using the same Employee data as the previous lab, but we want this new map to sort the data based on salary. Use `map.addIndex` to dynamically add this configuration to the cluster.

    Why add the configuration here? Because we don’t want to take down our cluster to add it to the `hazelcast.xml` configuration. This index is specific to the map `training-index`, so adding it here does not affect any other operations.

3.	Create a query that retrieves records for all salaries between 0 and 2000. You can use either the Criteria API or SQL to create your query. The code will display the contents of the collection in the system output.

4.	Run your code. 

    a.	The code will first populate the Employee map and return a time for how long it took to push the data. Note the duration.

    b.	The code then runs your query and displays the output. Note how long the system took to complete your query. 


Why did populating the data take so much longer? Because the cluster had to rebuild the index each time an entry was added. The trade-off is that the query itself ran much faster.

Takeaway: Use indexes for mostly-stable data, or accept the additional latency introduced by building the index.

## Extra Practice

1. Use native SQL to run the same query. Compare performance.
