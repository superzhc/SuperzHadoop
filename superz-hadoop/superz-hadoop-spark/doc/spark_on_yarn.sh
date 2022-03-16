#!/bin/bash

##########################################Yarn-Cluster############################################################
$SPARK_HOME/bin/spark-submit --master yarn \
--deploy-mode cluster \
--executor-memory 2G \
--executor-cores 2 \
--class org.apache.spark.examples.SparkPi \
$SPARK_HOME/examples/jars/spark-examples_2.12-3.0.0.jar \
1000
##################################################################################################################