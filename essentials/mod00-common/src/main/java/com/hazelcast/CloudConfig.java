package com.hazelcast;

public class CloudConfig {
    public String name;
    public String password;
    public String discoveryToken;
    public String urlBase;

    public CloudConfig(String name, String password, String discoveryToken, String urlBase) {
        this.name = name;
        this.password = password;
        this.discoveryToken = discoveryToken;
        this.urlBase = urlBase;
    }

    public String toString() {
        return name + " " + password + " " + discoveryToken + " " + urlBase;
    }

}
