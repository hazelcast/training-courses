# Querying Using Predicate API

## Objectives

*	Populate a data store in the Hazelcast cluster.
*	Create a query that pulls data from the data store.  

## Steps

1.	Modify the configuration and code loading section of the code based on your lab environment

    a.	If you are using a local cluster, comment out all the configuration lines and the user code deployment lines
    
    b.	If you are using a cloud-based cluster, modify the config section to include your specific cloud credentials.

> Note: The UserCodeDeployment section is needed to load the Employee data to the cloud-based instance.

2.	Create a query that retrieves records for all salaries between 0 and 2000. You can use either the Criteria API or SQL Predicate APIto create your query. The code will display the contents of the collection in the system output.

3.	Run your code. 

    a.	The code will first populate the Employee map and return a time for how long it took to push the data. Note the duration.

    b.	The code then runs your query and displays the output. Note how long the system took to complete your query. 

We will compare the load and query duration to an indexed map in the next lab. 
