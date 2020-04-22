#!/usr/bin/env bash
HZ_VERSION=3.12.6
MVN_ROOT=$HOME/.m2/repository
HZ_JAR=$MVN_ROOT/com/hazelcast/hazelcast/$HZ_VERSION/hazelcast-$HZ_VERSION.jar
# See if something about the MVN download is throwing us off ?
HZ_JAR=~/Documents/Hazelcast/Releases/hazelcast-3.12/lib/hazelcast-3.12.jar

JAVA_MODS="--add-modules java.se --add-exports java.base/jdk.internal.ref=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.management/sun.management=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED"
JAVA_ARGS="${JAVA_ARGS} -Xms8G -Xmx8g -Dhazelcast.config=$PWD/target/classes/hazelcast.xml"

# Need mod00 jar to get domain classes for serialization and queries
CLASSPATH=$PWD/target/classes
ls -al $CLASSPATH
# TODO: May Need Query jarfile on classpath -- not conclusive yet)
# may need others as additional labs are created

java $JAVA_MODS $JAVA_ARGS -cp $CLASSPATH -jar $HZ_JAR



