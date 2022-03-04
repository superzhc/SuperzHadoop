#!/bin/bash

# https://archive.apache.org/dist/flink/flink-1.12.2/flink-1.12.2-bin-scala_2.11.tgz
SCALA_VERSION="2.11"
FLINK_VERSION="1.12.2"
FLINK_URL="https://archive.apache.org/dist/flink/flink-$FLINK_VERSION/flink-$FLINK_VERSION-scala_$SCALA_VERSION.tgz"
curl "$FLINK_URL" -o "/tmp/flink-$FLINK_VERSION-bin-scala_$SCALA_VERSION.tgz"
tar -zxvf "/tmp/flink-$FLINK_VERSION-bin-scala_$SCALA_VERSION.tgz" -C /opt
mv "/opt/flink-$FLINK_VERSION" /opt/flink

if [ $FLINK_HOME ] && [-z $FLINK_HOME ];then
  echo "export FLINK_HOME=/opt/flink" >> /etc/profile
fi
source /etc/profile