#!/bin/bash

mkdir /opt/hudi

HUDI_VERSION="0.9.0"
HUDI_HADOOP_MR_URL="https://repo1.maven.org/maven2/org/apache/hudi/hudi-hadoop-mr-bundle/$HUDI_VERSION/hudi-hadoop-mr-bundle-$HUDI_VERSION.jar"
curl "$HUDI_HADOOP_MR_URL" -o "/opt/hudi/hudi-hadoop-mr-bundle-$HUDI_VERSION.jar"

SCALA_VERSION=2.11
#FLINK_VERSION=1.12.2
HUDI_FLINK_URL="https://repo1.maven.org/maven2/org/apache/hudi/hudi-flink-bundle_$SCALA_VERSION/$HUDI_VERSION/hudi-flink-bundle_$SCALA_VERSION-$HUDI_VERSION.jar"
curl "$HUDI_FLINK_URL" -o "/opt/hudi/hudi-flink-bundle_$SCALA_VERSION-$HUDI_VERSION.jar"

#export HADOOP_CLASSPATH=`$HADOOP_HOME/bin/hadoop classpath`
