package com.hztraining;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapLoader;
import com.hztraining.inv.Inventory;
import com.hztraining.inv.InventoryKey;
import com.hztraining.inv.InventoryTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *   UNDER DEVELOPMENT -- this is not yet a functional piece of code
 */
public class PopulateCacheWithMapLoader {

    public static void main(String[] args) {

        // ConfigUtil is in the common module
        String configname = ConfigUtil.findConfigNameInArgs(args);
        ClientConfig config = ConfigUtil.getClientConfigForCluster(configname);
        ClientUserCodeDeploymentConfig cucdc = new ClientUserCodeDeploymentConfig();

        // Work out path to Maven-downloaded MariaDB jar file
        StringBuilder maria = new StringBuilder();
        maria.append(System.getProperty("user.home"));
        maria.append("/.m2/repository");
        maria.append("/org/mariadb/jdbc/mariadb-java-client");
        maria.append("/2.4.4");
        maria.append("/mariadb-java-client-2.4.4.jar");
        String mariaJar = maria.toString();

        System.out.println("Maria Jar " + mariaJar);
        cucdc.setEnabled(true);

        // Without mariaJar we fail to load org.mariadb.jdbc.Driver
        // with mariaJar, we get further, but fail on another file (RowProtocol)
        // that is in the same jar
        cucdc.addJar(mariaJar); // finds JDBC driver but not RowProtocol ?
        cucdc.addClass("com.hztraining.inv.InventoryTable"); // working
        config.setUserCodeDeploymentConfig(cucdc);


        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

       IMap<InventoryKey, Inventory> invmap = client.getMap("invmap");

        long start = System.nanoTime();

        // If executed in cloud, get IllegalArgumentException: First you should configure a map store.
        // This will be addressed in next release of Cloud Enterprise.  (Not sure about for Starter)

        // If executed on prem, fails with CNFE: Failed to load class from other members

        try {
            invmap.loadAll(true); // parameter = replaceExistingValues

            long finish = System.nanoTime();
            long elapsedNanos = finish - start;
            double elapsedSeconds = (double) elapsedNanos / 1_000_000_000D;

            System.out.printf("Finished in %3.3f seconds\n", elapsedSeconds);
            System.out.println("Final count " + invmap.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.shutdown();
        }
    }
}
