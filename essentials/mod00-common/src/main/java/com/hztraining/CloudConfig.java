package com.hztraining;

public class CloudConfig {
    public String name;
    public String password;
    public String discoveryToken;
    public String urlBase;  // May be null, and should not set property when that is the case

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
