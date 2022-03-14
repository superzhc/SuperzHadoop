#!/bin/bash

HADOOP_VERSION=3.2.2
curl -sL "https://archive.apache.org/dist/hadoop/common/hadoop-$HADOOP_VERSION/hadoop-$HADOOP_VERSION.tar.gz" -o "/tmp/hadoop-$HADOOP_VERSION.tar.gz"
tar -zxvf "/tmp/hadoop-$HADOOP_VERSION.tar.gz" -C /opt

echo "export HADOOP_HOME=/opt/hadoop-$HADOOP_VERSION"  >> /etc/profile
echo "export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop"  >> /etc/profile
echo "export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin"  >> /etc/profile
source /etc/profile

# 初始化 namenode
$HADOOP_HOME/bin/hdfs namenode -format
# 启动 namenode
$HADOOP_HOME/sbin/start-dfs.sh
# 启动 yarn
$HADOOP_HOME/sbin/start-yarn.sh