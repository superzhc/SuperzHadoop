#!/bin/bash

# 基于已安装的 Hadoop 环境，进行 hive 的安装

# 下载 hive 安装包
#curl "https://downloads.apache.org/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz" -o /tmp/apache-hive-3.1.2-bin.tar.gz
curl -fkSL "https://mirrors.tuna.tsinghua.edu.cn/apache/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz" -o /tmp/apache-hive-3.1.2-bin.tar.gz
tar -xvf /tmp/apache-hive-3.1.2-bin.tar.gz -C /opt

# 配置环境变量
if[$HADOOP_HOME];then
  echo "export HADOOP_HOME=$HADOOP_PREFIX" >> /etc/profile
fi
echo "export HIVE_HOME=/opt/apache-hive-3.1.2-bin" >> /etc/profile
echo "export PATH=\$HIVE_HOME/bin:\$PATH" >> /etc/profile
source /etc/profile

# 下载mysql驱动包
curl -fSL "https://downloads.mysql.com/archives/get/p/3/file/mysql-connector-java-5.1.48.tar.gz" -o /tmp/mysql-connector-java-5.1.48.tar.gz
tar -xvf /tmp/mysql-connector-java-5.1.48.tar.gz -C /opt
cp /opt/mysql-connector-java-5.1.48/mysql-connector-java-5.1.48.jar $HIVE_HOME/lib/

# 在 HDFS 上创建相关文件夹
$HADOOP_HOME/bin/hadoop fs -mkdir       /tmp
$HADOOP_HOME/bin/hadoop fs -mkdir  -p   /user/hive/warehouse
$HADOOP_HOME/bin/hadoop fs -chmod g+w   /tmp
$HADOOP_HOME/bin/hadoop fs -chmod g+w   /user/hive/warehouse

# 写配置
function addProperty(){
  echo "<property><name>$2</name><value>$3</value></property>" >> $1
}

file="${HIVE_HOME}/conf/hive-site.xml"
if [! -f "$file"];then
  touch $file
fi
echo "<configuration>" >> $file
addProperty $file hive.metastore.warehouse.dir /user/hive/warehouse
addProperty $file javax.jdo.option.ConnectionDriverName com.mysql.jdbc.Driver
addProperty $file javax.jdo.option.ConnectionURL jdbc:mysql://localhost:3306/hive_metastore?createDatabaseIfNotExist=true
addProperty $file javax.jdo.option.ConnectionUserName root
addProperty $file javax.jdo.option.ConnectionPassword 123456
## 指定 hiveserver2 连接的端口
addProperty $file hive.server2.thrift.port 10000
echo "</configuration>" >> $file

# 初始化 MySQL 元数据
$HIVE_HOME/bin/schematool -initSchema -dbType mysql -verbose
# 启动元数据服务[后台启动]
$HIVE_HOME/bin/hive --service metastore &
## 启动 hiveserver2[后台启动]
#$HIVE_HOME/bin/hive --service hiveserver2 &

## jdbc连接hive
#beeline -u jdbc:hive2://node01:10000 -n root

