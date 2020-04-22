# Module 00 - Common

This module is shared infrastructure for all of the training labs.

Note that the labs are attempting to be environmentally agnostic, so that they can run 
locally (on-premise), in a non-managed cloud, in Hazelcast's managed cloud
service (either Starter or Enterprise edition), or some combination.

This hasn't been fully realized but as additional capabilities are added to the
managed service products, it should be achievable by mid- to late-2020.

## MariaDB

In Module 2, we load content into the IMDG grid from a database.  Scripts to build the
database are here in mod00-common/mariadb.  It should not be necessary to touch
these unless you are modifying the database schema.  If you need to change the
database schema, you'll need to update scripts/inv-table, the InventoryTable class, and the
buildPipeline method in mod02/PopulateCacheWithJet. 

## Inventory model objects

Used mostly in Module 2, as once content is loaded from the database to the Map, other
labs access the map exclusively.   (Current labs assume the entire database is made 
resident in the Map; future labs may trigger loads on a cache miss and would then rely
on these classes)

* Inventory - represents an Inventory item (a row in the table, as well as an entry in the Map)
* InventoryKey - represents the compound DB an Map key of SKU + Location
* InventoryDB - represents the database, establishes JDBC connection
* InventoryTable - represents the inventory table, JDBC inserts and selects are here

Note that we could do more interesting queries if we had price info in the database, so when
it becomes necessary to update the database schema for any reason, that would be a useful
addition. 

## Configuration objects

* CloudConfig - simple value type of fields needed to connect to a Hazelcast Managed Service.  Currently
lacking the SSL related fields needed to connect to Hazelcast Cloud Enterprise. 
* ConfigUtil - utility to provide configuration flexibility; all the labs take a command line parameter
that identifies the target (-onprem, -personal, -shared, -enterprise); based on that target this
utility fetches corresponding values from properties.yaml and builds the ClientConfig object.

## IMDG run script 

* runImdg.sh - Used to start a local (on-premise) cluster node when not using a cloud-based
cluster. 

TODO: Need to work on runImdg.sh and see if I can make it work with maven-based repo rather than 
forcing download of a hazelcast zip installation. 
