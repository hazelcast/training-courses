package com.hazelcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.net.URL;
import java.util.Map;

public class ConfigUtil {

    public static final String PERSONAL_CLUSTER = "personal-cluster";
    public static final String TRAINING_CLUSTER = "shared-training-cluster";
    public static final String ENTERPRISE_CLUSTER = "enterprise-cluster";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String DISCOVERY_TOKEN = "discovery-token";
    public static final String URL_BASE = "url-base";

    private static Map<String, Map> configs;

    static {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map<String, Map<String, Map>> properties = null;
        try {
            URL yamlFile = ConfigUtil.class.getClassLoader().getResource("properties.yaml");
            System.out.println("Reading config info from " + yamlFile.toExternalForm());
            properties = mapper.readValue(yamlFile, Map.class);
            configs = properties.get("cluster-configuration");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static CloudConfig getPersonalClusterConfig() {
        Map<String,String> personal = configs.get(PERSONAL_CLUSTER);
        CloudConfig cc = new CloudConfig(personal.get(NAME), personal.get(PASSWORD),
                personal.get(DISCOVERY_TOKEN), personal.get(URL_BASE));
        return cc;
    }

    public static CloudConfig getTrainingClusterConfig() {
        Map<String,String> training = configs.get(TRAINING_CLUSTER);
        CloudConfig cc = new CloudConfig(training.get(NAME), training.get(PASSWORD),
                training.get(DISCOVERY_TOKEN), training.get(URL_BASE));
        return cc;
    }

    public static CloudConfig getEnterpriseClusterConfig() {
        Map<String,String> enterprise = configs.get(ENTERPRISE_CLUSTER);
        CloudConfig cc = new CloudConfig(enterprise.get(NAME), enterprise.get(PASSWORD),
                enterprise.get(DISCOVERY_TOKEN), enterprise.get(URL_BASE));
        return cc;
    }


}
