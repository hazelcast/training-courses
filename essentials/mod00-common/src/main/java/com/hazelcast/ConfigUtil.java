package com.hazelcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;

import java.net.URL;
import java.util.Map;

public class ConfigUtil {

    public static final String ONPREM_CLUSTER = "on-prem-cluster";
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

    public static CloudConfig getOnPremClusterConfig() {
        Map<String,String> onprem = configs.get(ONPREM_CLUSTER);
        CloudConfig cc = new CloudConfig(onprem.get(NAME), onprem.get(PASSWORD),
                null,null);
        return cc;
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

    public static ClientConfig getClientConfigForCluster(String configname) {
        if (configname == null) configname = "onprem"; // default
        System.out.println("Looking up config for " + configname);
        CloudConfig cloudConfig;
        switch (configname) {
            case "onprem": cloudConfig = getOnPremClusterConfig(); break;
            case "personal": cloudConfig = getPersonalClusterConfig(); break;
            case "shared": cloudConfig = getTrainingClusterConfig(); break;
            case "enterprise": cloudConfig = getEnterpriseClusterConfig(); break;
            default:
                throw new IllegalArgumentException(("Bad cluster config name: " + configname));
        }

        ClientConfig config = new ClientConfig();
        config.setGroupConfig(new GroupConfig(cloudConfig.name, cloudConfig.password));
        config.setProperty("hazelcast.client.statistics.enabled", "true");
        if (cloudConfig.discoveryToken != null)
            config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), cloudConfig.discoveryToken);
        if (cloudConfig.urlBase != null)
            config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), cloudConfig.urlBase);

        return config;
    }

    public static String findConfigNameInArgs(String[] args) {
        for (String arg : args) {
            if (arg.equals("-onprem")) return "onprem";
            if (arg.equals("-personal")) return "personal";
            if (arg.equals("-shared")) return "shared";
            if (arg.equals("-enterprise")) return "enterprise";
        }
        return null;
    }
}
