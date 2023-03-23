# `hive-standalone-metastore`

## `metastore-site.xml`

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <property>
        <name>javax.jdo.option.ConnectionURL</name>
        <value>jdbc:mysql://172.17.0.8:3306/hive_standalone_metastore?useSSL=false</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionDriverName</name>
        <value>com.mysql.jdbc.Driver</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionUserName</name>
        <value>root</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionPassword</name>
        <value>123456</value>
    </property>
    <property>
        <name>hive.metastore.event.db.notification.api.auth</name>
        <value>false</value>
    </property>
    <property>
        <name>metastore.thrift.uris</name>
        <value>thrift://0.0.0.0:9083</value>
        <description>Thrift URI for the remote metastore. Used by metastore client to connect to remote metastore.</description>
    </property>
    <property>
        <name>metastore.task.threads.always</name>
        <value>org.apache.hadoop.hive.metastore.events.EventCleanerTask,org.apache.hadoop.hive.metastore.MaterializationsCacheCleanerTask</value>
    </property>
    <property>
        <name>metastore.expression.proxy</name>
        <value>org.apache.hadoop.hive.metastore.DefaultPartitionExpressionProxy</value>
    </property>
    <property>
        <name>metastore.warehouse.dir</name>
        <value>s3a://superz/metastore</value>
    </property>
	<property>
        <name>fs.s3a.access.key</name>
        <value>admin</value>
    </property>
    <property>
        <name>fs.s3a.secret.key</name>
        <value>admin123456</value>
    </property>
    <property>
        <name>fs.s3a.connection.ssl.enabled</name>
        <value>false</value>
    </property>
    <property>
        <name>fs.s3a.path.style.access</name>
        <value>true</value>
    </property>
    <property>
        <name>fs.s3a.endpoint</name>
        <value>http://172.17.0.3:9000</value>
    </property>
    <property>
        <name>fs.s3a.region</name>
        <value>us-east-1</value>
    </property>
</configuration>
```

## `Dockerfile`

```
FROM centos:centos7

RUN yum install -y wget java-1.8.0-openjdk-devel && yum clean all

WORKDIR /opt

RUN wget https://repo1.maven.org/maven2/org/apache/hive/hive-standalone-metastore/3.1.2/hive-standalone-metastore-3.1.2-bin.tar.gz \
  && tar zxvf hive-standalone-metastore-3.1.2-bin.tar.gz \
  && rm -rf hive-standalone-metastore-3.1.2-bin.tar.gz \
  && mv apache-hive-metastore-3.1.2-bin metastore

# 若下载速度慢，可先本地下载copy上来，根据需要选择
# RUN wget https://archive.apache.org/dist/hadoop/common/hadoop-3.2.2/hadoop-3.2.2.tar.gz &&\
COPY hadoop-3.2.2.tar.gz /opt/
RUN tar zxvf hadoop-3.2.2.tar.gz \
  && rm -rf hadoop-3.2.2.tar.gz \
  && mv hadoop-3.2.2 hadoop

RUN wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.49/mysql-connector-java-5.1.49.jar \
  && cp mysql-connector-java-5.1.49.jar ./metastore/lib

ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk
ENV HADOOP_HOME=/opt/hadoop
ENV METASTORE_HOME=/opt/metastore

RUN rm -f ${METASTORE_HOME}/lib/guava-19.0.jar \
  && cp ${HADOOP_HOME}/share/hadoop/common/lib/guava-27.0-jre.jar ${METASTORE_HOME}/lib \
  && cp ${HADOOP_HOME}/share/hadoop/tools/lib/hadoop-aws-3.2.2.jar ${METASTORE_HOME}/lib \
  && cp ${HADOOP_HOME}/share/hadoop/tools/lib/aws-java-sdk-bundle-*.jar ${METASTORE_HOME}/lib

# copy Hive metastore configuration file
COPY metastore-site.xml ${METASTORE_HOME}/conf/

# Hive metastore data folder
VOLUME ["/user/hive/warehouse"]

WORKDIR ${METASTORE_HOME}

RUN ${METASTORE_HOME}/bin/schematool -initSchema -dbType mysql

CMD ["/opt/metastore/bin/start-metastore"]
```

## 构建

> 将上面的文件放到同一个目录文件夹下，执行如下命令进行构建：

```shell
docker build . -t hive-standalone-metastore:v1.0
```

**注意1**：当前的使用方式下，不同环境配置的构建，需要对 `metastore-site.xml` 进行配置即可

**注意2**：数据库要提前创建好库，如上配置，需要在mysql库建立hive_standalone_metastore库

## 启动容器

```shell
docker run -d -p 9083:9083 -v /f/docker/volumes/hive_standalone_metastore/warehouse:/user/hive/warehouse --name hive-standalone-metastore hive-standalone-metastore:v1.0
```