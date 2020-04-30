#!/usr/bin/env bash

# NOTE: all this index stuff needs to be done at server start, doing at client is pointless

HZ_CONFIG_NOINDEX="$(PWD)/target/classes/hazelcast-noindexes.xml"
HZ_CONFIG_INDEX="$(PWD)/target/classes/hazelcast-indexes.xml"

if [ $2 == -useindex ]
then
  echo Using indexes
  HZ_CONFIG=$HZ_CONFIG_INDEX
else
  echo Not using indexes
  HZ_CONFIG=$HZ_CONFIG_NOINDEX
fi

# $1 should be environment selector -- one of -onprem, -personal, -shared, -enterprise.  Default -onprem
mvn exec:java -Dexec.args="$1 -Dhazelcast.config=$HZ_CONFIG"