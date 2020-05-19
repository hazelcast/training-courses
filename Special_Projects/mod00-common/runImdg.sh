#!/usr/bin/env bash
HZ_VERSION=3.12.6
#MARIADB_VERSION=2.4.4
MVN_ROOT=$HOME/.m2/repository
HZ_JAR=$MVN_ROOT/com/hazelcast/hazelcast/$HZ_VERSION/hazelcast-$HZ_VERSION.jar
#MARIADB_JAR=$MVN_ROOT/org/mariadb/jdbc/mariadb-java-client/$MARIADB_VERSION/mariadb-java-client-$MARIADB_VERSION.jar

JAVA_MODS="--add-modules java.se --add-exports java.base/jdk.internal.ref=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.management/sun.management=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED"
HZ_CONFIG="$(PWD)/target/classes/hazelcast.xml"
JAVA_ARGS="${JAVA_ARGS} -Xms8G -Xmx8g -Dhazelcast.config=$HZ_CONFIG"

# Need mod00 jar to get domain classes for serialization, queries, and map loader
# MariaDB jar is used only for the MapLoader example in Module 3
CLASSPATH=$(PWD)/target/classes
#CLASSPATH=$(PWD)/target/classes:$MARIADB_JAR
#ls -al "$CLASSPATH"

# May Need Query jarfile on classpath -- not conclusive yet)
# may need others as additional labs are created

java $JAVA_MODS $JAVA_ARGS -jar $HZ_JAR



