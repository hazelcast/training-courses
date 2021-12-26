# In Memory Computing Essentials

## Before You Begin

Determine whether you will use Hazelcast installed locally on your computer or Hazelcast Cloud. 

For local installation, go to the [Hazelcast Platform Documentation](https://docs.hazelcast.com/hazelcast/5.0/). Follow the instructions to install Hazelcast and connect a Java client. (Java is required for this course.) 

For Hazelcast Cloud, go to the [Hazelcast Cloud Documentation](https://docs.hazelcast.com/cloud/getting-started) and follow the instructions to set up a free instance of Hazelcast, then connect a Java client.

## Entry Processor

Watch the demo. The code here is the same code. You will likely not see a significant difference in performance if you're running the Hazelcast cluster on the same system as the client, as there's no network delay.

You can use this example as a starting point for your own entry processor. Some suggestions:
* add a hire date to the Employee record. Set random hire dates for the employees. Give a percentage raise to employees based on length of service - under 2 years, 2%; 2-5 years, 3%, 5-10 years, 4%, over 10 years, 5%.

* add a location field to the Employee record. Set random locations for the employees. Use a predicate to only give employees at a certain location a raise e.g. all UK-based employees get a raise of 100.

## Entry Listener

Watch the demo. The code here is the same code. 

Use this example as a starting point for your own listener or interceptor. Some suggestions:

* Write code that takes fields from the event and displays a more human-friendly message e.g. "Value X was replaced by Value Y."

* When a new entry is added to the monitored map, launch an operation that prompts for additional information. For example, you can create a second map called AddOwner. When a new entry is added to the map, launch an operation that asks for the name of the user, then add the key and the username to the AddOwner file. 

* Write an interceptor that prints a "do you really want to do this?" message for an entry deletion. 

## Executor

Watch the demo. We've included the basic executor code here as a starting point for writing your own executor and submitting it to the cluster. You can see additional examples of executors at the [Hazecast Code Samples Repo](htttps://github.com/hazelcast/code-samples) on GitHub.