package com.hztraining;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.impl.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.properties.ClientProperty;

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
    private static String defaultClusterName;

    static class ConfigInfo {
        String defaultConfig;
        Map<String, Map> clusterConfiguration;
        public ConfigInfo() {}
        public void setDefaultConfig(String config) { this.defaultConfig = config; }
        public void setClusterConfiguration(Map<String, Map> configs) {
            this.clusterConfiguration = configs;
        }
    }

    static {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        //Map<String, Map<String, Map>> properties = null;
        ConfigInfo configInfo;
        try {
            URL yamlFile = ConfigUtil.class.getClassLoader().getResource("properties.yaml");
            System.out.println("Reading config info from " + yamlFile.toExternalForm());
            configInfo = mapper.readValue(yamlFile, ConfigInfo.class);
            defaultClusterName = configInfo.defaultConfig;
            configs = configInfo.clusterConfiguration;
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

    public static String getDefaultConfigName() { return defaultClusterName; }
    public static ClientConfig getDefaultConfig() {
        return getClientConfigForCluster(defaultClusterName);
    }

    public static ClientConfig getClientConfigForCluster(String configname) {
        if (configname == null) {
            System.out.println("No command line argument for cluster, properties.yaml default is " + defaultClusterName);
            switch (defaultClusterName) {
                case "on-prem-cluster": configname = "onprem"; break;
                case "personal-cluster": configname = "personal"; break;
                case "shared-cluster": configname = "shared"; break;
                case "enterprise-cluster": configname = "enterprise"; break;
                default: configname = "onprem";
            }
        }
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
        config.setClusterName(cloudConfig.name);

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

    public static void main(String[] args) {
        System.out.println("Default config is " + defaultClusterName);
    }
}
