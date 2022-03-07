#!/bin/bash

# 基于已安装的 Hadoop 环境，启动 Yarn

echo "启动开始，启动类型 $1"
if [ "$1" = "resourcemanager" ]
  then
    `nohup $HADOOP_PREFIX/bin/yarn --config $HADOOP_CONF_DIR resourcemanager > /opt/hadoop-$HADOOP_VERSION/logs/resourcemanager-launch.log 2>&1 &`
elif [ "$1" = "nodemanager" ]
  then
    `nohup $HADOOP_PREFIX/bin/yarn --config $HADOOP_CONF_DIR nodemanager > /opt/hadoop-$HADOOP_VERSION/logs/nodemanager-launch.log 2>&1 &`
else
  echo "尚不支持 $1 启动类型，请自启动"
fi
echo "启动完成"
exit 0