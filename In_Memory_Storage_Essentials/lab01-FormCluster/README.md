#Lab 1: Forming a cluster

The point of this exercise is to demonstrate the ability of Hazelcast IMDG 
to automatically form a cluster when multiple instances of Hazelcast are
started. 

In order to prevent different student's instances from connecting to each
other, multicast has been disabled, and the hazelcast.xml configuration
allows only nodes running on the localhost (127.0.0.1) to cluster up.

