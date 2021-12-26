# In Memory Storage Essentials 

## Requirements

* Java Developer Kit version 8 or higher
* An IDE, preferrably one that supports Maven

## Before You Begin

Determine whether you will use Hazelcast installed locally on your computer or Hazelcast Cloud. 

For local installation, go to the [Hazelcast Platform Documentation](https://docs.hazelcast.com/hazelcast/5.0/). Follow the instructions to install Hazelcast, install Management Center, and connect a Java client. Before you start the cluster, add the following to your hazelcast.xml configuration:
```
    <map name="training-eviction">
        <eviction eviction-policy="LFU" max-size-policy="USED_HEAP_SIZE" size="1"/>
     </map>
```
For Hazelcast Cloud, go to the [Hazelcast Cloud Documentation](https://docs.hazelcast.com/cloud/getting-started) and follow the instructions to set up a free instance of Hazelcast. Add the following parameters to your configuration before launching your free instance:
* Eviction policy: LFU
* Max size policy: Used heap size
* Size: 1

Once your cluster has started, connect a Java client. 

Individual lab instructions are in the README files under each lab. 






