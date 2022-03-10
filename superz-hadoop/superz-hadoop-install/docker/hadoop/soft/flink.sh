#!/bin/bash

# https://archive.apache.org/dist/flink/flink-1.12.2/flink-1.12.2-bin-scala_2.11.tgz
SCALA_VERSION="2.11"
FLINK_VERSION="1.12.2"
FLINK_URL="https://archive.apache.org/dist/flink/flink-$FLINK_VERSION/flink-$FLINK_VERSION-scala_$SCALA_VERSION.tgz"
curl "$FLINK_URL" -o "/tmp/flink-$FLINK_VERSION-bin-scala_$SCALA_VERSION.tgz"
tar -zxvf "/tmp/flink-$FLINK_VERSION-bin-scala_$SCALA_VERSION.tgz" -C /opt
mv "/opt/flink-$FLINK_VERSION" /opt/flink

if [ $HADOOP_CONF_DIR ] && [ $HADOOP_CONF_DIR ]; then
  echo "export HADOOP_CONF_DIR=/etc/hadoop/conf" >> /etc/profile
fi
if [ $HADOOP_CLASSPATH ] && [ -z $HADOOP_CLASSPATH ];then
  echo 'export HADOOP_CLASSPATH=`$HADOOP_HOME/bin/hadoop classpath`' >> /etc/profile
fi
if [ $FLINK_HOME ] && [ -z $FLINK_HOME ];then
  echo "export FLINK_HOME=/opt/flink" >> /etc/profile
fi
source /etc/profile

# stand模式启动flink
$FLINK_HOME/bin/start-cluster.sh

## 启动 sql-client.sh
#$FLINK_HOME/bin/sql-client.sh embedded