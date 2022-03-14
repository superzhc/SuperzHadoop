#!/bin/bash

mkdir -p /opt/hudi

SCALA_VERSION=2.11
HUDI_VERSION=0.9.0

HUDI_HADOOP_MR_URL="https://repo1.maven.org/maven2/org/apache/hudi/hudi-hadoop-mr-bundle/$HUDI_VERSION/hudi-hadoop-mr-bundle-$HUDI_VERSION.jar"
curl "$HUDI_HADOOP_MR_URL" -o "/opt/hudi/hudi-hadoop-mr-bundle-$HUDI_VERSION.jar"
cp "/opt/hudi/hudi-hadoop-mr-bundle-$HUDI_VERSION.jar" "$HIVE_HOME/lib"

# Spark
HUDI_SPARK_URL="https://repository.apache.org/service/local/repositories/releases/content/org/apache/hudi/hudi-spark3-bundle_$SCALA_VERSION/$HUDI_VERSION/hudi-spark3-bundle_$SCALA_VERSION-$HUDI_VERSION.jar"
curl "$HUDI_SPARK_URL" -o "/opt/hudi/hudi-spark3-bundle_$SCALA_VERSION-$HUDI_VERSION.jar"
cp "/opt/hudi/hudi-spark3-bundle_$SCALA_VERSION-$HUDI_VERSION.jar" "$SPARK_HOME/jars"

# Flink
HUDI_FLINK_URL="https://repo1.maven.org/maven2/org/apache/hudi/hudi-flink-bundle_$SCALA_VERSION/$HUDI_VERSION/hudi-flink-bundle_$SCALA_VERSION-$HUDI_VERSION.jar"
curl "$HUDI_FLINK_URL" -o "/opt/hudi/hudi-flink-bundle_$SCALA_VERSION-$HUDI_VERSION.jar"
cp "/opt/hudi/hudi-flink-bundle_$SCALA_VERSION-$HUDI_VERSION.jar" "$FLINK_HOME/lib/"

#export HADOOP_CLASSPATH=`$HADOOP_HOME/bin/hadoop classpath`
