#!/bin/bash

prestoServerUrl="https://repo1.maven.org/maven2/com/facebook/presto/presto-server/0.265/presto-server-0.265.tar.gz"
curl "$prestoServerUrl" -o /tmp/presto-server-0.265.tar.gz
tar -xvf /tmp/presto-server-0.265.tar.gz -C /opt

mkdir -p /opt/presto-server-0.265/etc
touch config.properties
touch jvm.config
#touch log.properties
touch node.properties

# jvm.config 配置
echo "-server"                           >> jvm.config
echo "-Xmx16G"                           >> jvm.config
echo "-XX:+UseG1GC"                      >> jvm.config
echo "-XX:G1HeapRegionSize=32M"          >> jvm.config
echo "-XX:+UseGCOverheadLimit"           >> jvm.config
echo "-XX:+ExplicitGCInvokesConcurrent"  >> jvm.config
echo "-XX:+HeapDumpOnOutOfMemoryError"   >> jvm.config
echo "-XX:+ExitOnOutOfMemoryError"       >> jvm.config
echo "-DHADOOP_USER_NAME=hdfs"           >> jvm.config

# config.properties 配置
## 是否是 coordinator
echo "coordinator=true"                         >> config.properties
echo "node-scheduler.include-coordinator=false" >> config.properties
echo "http-server.http.port=10008"              >> config.properties
echo "query.max-memory=50GB"                    >> config.properties
echo "query.max-memory-per-node=1GB"            >> config.properties
echo "query.max-total-memory-per-node=2GB"      >> config.properties
echo "discovery-server.enabled=true"            >> config.properties
echo "discovery.uri=http://192.168.1.151:10058" >> config.properties

# node.properties 配置
echo "node.environment=production"               >> node.properties
echo "node.id=master01"                          >> node.properties # 注意每台机器的id要唯一
echo "node.data-dir=/home/presto/presto-datadir" >> node.properties

# 在 etc 文件夹下创建 catalog 目录
mkdir /opt/presto-server-0.265/etc/catalog
## hive 数据源
touch /opt/presto-server-0.265/etc/catalog/hive.properties
echo "connector.name=hive-hadoop2"                                                                           >> hive.properties
echo "hive.metastore.uri=thrift://master02.pxx.com:9083"                                                     >> hive.properties
echo "hive.config.resources=/etc/hadoop/3.1.4.0-315/0/core-site.xml,/etc/hadoop/3.1.4.0-315/0/hdfs-site.xml" >> hive.properties
## jmx 数据源
touch /opt/presto-server-0.265/etc/catalog/jmx.properties
echo "connector.name=jmx" >> jmx.properties

# 注意需要先启动 hive metastore 服务
$HIVE_HOME/bin/hive --service metastore

# 启动 presto，两种方式
## 后台启动
/opt/presto-server-0.265/bin/launcher run
## 前台启动
/opt/presto-server-0.265/bin/launcher start