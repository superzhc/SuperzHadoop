#!/bin/bash

HADOOP_VERSION=3.2
SPARK_VERSION=3.0.0
#curl "https://archive.apache.org/dist/spark/spark-3.0.0/spark-3.0.0-bin-hadoop3.2.tgz" -o /tmp/ "spark-3.0.0-bin-hadoop3.2.tgz"
curl "https://archive.apache.org/dist/spark/spark-$SPARK_VERSION/spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION.tgz" -o "/tmp/spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION.tgz"
tar -zxvf "/tmp/spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION.tgz" -C /opt
ln -s /opt/spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION /opt/spark

echo "export SPARK_HOME=/opt/spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION" >> /etc/profile
echo "export SPARK_CONF_DIR=$SPARK_HOME/conf" >> /etc/profile
echo 'export PATH=$PATH:$SPARK_HOME/bin' >> /etc/profile
source /etc/profile

cp $SPARK_HOME/conf/spark-defaults.conf.template $SPARK_HOME/conf/spark-defaults.conf

echo "spark.master            yarn"  >> $SPARK_HOME/conf/spark-defaults.conf
echo "spark.eventLog.enabled  false" >> $SPARK_HOME/conf/spark-defaults.conf
echo "spark.executor.extraJavaOptions  -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70 -XX:MaxHeapFreeRatio=70 -XX:+CMSClassUnloadingEnabled" >> $SPARK_HOME/conf/spark-defaults.conf
echo "spark.driver.extraJavaOptions    -Dspark.driver.log.level=INFO -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70 -XX:MaxHeapFreeRatio=70 -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=512M"     >> $SPARK_HOME/conf/spark-defaults.conf

#$HADOOP_HOME/bin/hdfs dfs -mkdir /spark-jars
#$HADOOP_HOME/bin/hdfs dfs -put   $SPARK_HOME/jars/* /spark-jars/

# 启动 Spark-Shell
$SPARK_HOME/bin/spark-shell