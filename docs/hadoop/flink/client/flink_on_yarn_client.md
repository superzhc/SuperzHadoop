# Flink On Yarn 模式客户端提交

## 客户端环境搭建

0. 配置服务器 Hosts，安装 JDK11
1. 下载 Hadoop 安装包：[Hadoop 3.1.1](https://archive.apache.org/dist/hadoop/common/hadoop-3.1.1/hadoop-3.1.1.tar.gz)
2. 解压到指定目录，并配置 `HADOOP_HOME` 环境变量
   ```sh
   export HADOOP_HOME=/data/flink-client/hadoop-3.1.1
   export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

   export HADOOP_CLASSPATH=`hadoop classpath`
   export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
   ```
3. 下载 [Flink 1.16.2](https://archive.apache.org/dist/flink/flink-1.16.2)
4. 解压缩到指定目录
5. 将实际 Hadoop 配置文件 `core-site.xml`,`hdfs-site.xml`,`yarn-site.xml` 替换掉 `${HADOOP_HOME}/etc/hadoop` 目录下默认文件
6. 根据实际需要修改 `flink-conf.yaml`

## Yarn Rest Api

查看 `yarn-site.xml`

```xml
<property>
    <name>yarn.resourcemanager.ha.enabled</name>
    <value>true</value>
</property>

<property>
    <name>yarn.resourcemanager.ha.rm-ids</name>
    <value>rm1,rm2</value>
</property>

<property>
    <name>yarn.resourcemanager.hostname</name>
    <value>bigdata14</value>
</property>

<property>
    <name>yarn.resourcemanager.hostname.rm1</name>
    <value>bigdata14</value>
</property>

<property>
    <name>yarn.resourcemanager.hostname.rm2</name>
    <value>bigdata15</value>
</property>

<property>
    <name>yarn.resourcemanager.webapp.address</name>
    <value>bigdata14:8088</value>
    <description>ResourceManager对外web ui地址。用户可通过该地址在浏览器中查看集群各类信息，默认值:yarn.resourcemanager.hostname:8088 </description>
</property>

<property>
    <name>yarn.resourcemanager.webapp.address.rm1</name>
    <value>bigdata14:8088</value>
</property>

<property>
    <name>yarn.resourcemanager.webapp.address.rm2</name>
    <value>bigdata15:8088</value>
</property>
```

**获取所有Applications**

```
GET http://rm-http-address:port/ws/v1/cluster/apps
```

**获取指定Application**

```
GET http://rm-http-address:port/ws/v1/cluster/apps/{appId}
```

## Flink 运行参数

| 参数                        | 名称     | 默认值    | 备注 | 模式 |
| --------------------------- | -------- | --------- | ---- | ---- |
| `parallelism`               | 并行度   | 1         |      | All  |
| ~~`yarn.application.name`~~ | 任务名称 |           |      | Yarn |
| `yarn.application.queue`    | 运行队列 | `default` |      | Yarn |

## Checkpoint 参数【不区分运行模式】

| 参数                               | 名称                           | 默认值 | 备注                                                                |
| ---------------------------------- | ------------------------------ | ------ | ------------------------------------------------------------------- |
| +`enableCheckpointing`             | 是否开启                       | false  | 是否开启Checkpoint，开启后下面参数才有效                            |
| `checkpointInterval`               | 保存时间间隔（单位毫秒）       |        |                                                                     |
| `checkpointingMode`                | 一致性模式                     |        | 可选值：`EXACTLY_ONCE`,`AT_LEAST_ONCE`                              |
| `checkpointTimeout`                | 超时时间（单位毫秒）           |        |                                                                     |
| `tolerableCheckpointFailureNumber` | 设置失败次数                   |        |                                                                     |
| +`minPauseBetweenCheckpoints`      | 两次保存的最小间隔（单位毫秒） |        |                                                                     |
| +`maxConcurrentCheckpoints`        | 保存的最大并行度               |        |                                                                     |
| `externalizedCheckpointCleanup`    | 作业取消后检查点是否删除       |        | 可选值：`DELETE_ON_CANCELLATION`,`RETAIN_ON_CANCELLATION`，可不选择 |
| `stateBackendType`                 | 后端状态                       |        | 可选值：`memory`,`file`,`rocksdb`                                   |
| ~~`checkpointDir`~~                | 保存地址                       |        | 已废弃未删除，含义同 `checkpointStorage`                            |
| +`checkpointStorage`               | 保存地址                       |        | `stateBackendType` 为 `memory`，无需设置，其他两种都必须设置该参数  |
| `enableIncremental`                | 是否采用增量                   |        | 仅 `rocksdb` 有效                                                   |
| ~~`asynchronousSnapshots`~~        | 是否异步                       |        | 已废弃未删除                                                        |

## 内置系统参数

| 参数                                | 名称                                                                                      | 描述 |
| ----------------------------------- | ----------------------------------------------------------------------------------------- | ---- |
| `flink_home`                        | flink客户端目录（必选）                                                                   |      |
| `flink_streaming_platform_web_home` | flink-streaming-platform-web应用安装的目录（必选）                                        |      |
| `flink_rest_http_address`           | flink Rest & web frontend 地址(Local Cluster模式)                                         |      |
| `flink_rest_ha_http_address`        | flink Rest & web frontend HA 地址(Standalone Cluster模式 支持HA 可以填写多个地址 ;用分隔) |      |
| `yarn_rm_http_address`              | Yarn RM Http地址                                                                          |      |
| +`yarn_container_java_home`                   | Yarn Container JAVA HOME                                                                  |      |


## Yarn 任务状态

1. `NEW` ：应用程序刚创建时的状态。应用程序会被分配一个唯一的Application ID，但还没有分配资源，也没有进入资源队列。
2. `NEW_SAVING` ：应用程序等待资源保存。这个状态只存在于开启了Application历史保存的集群上，如果没有保存历史，则该状态的转换不会发生。
3. `SUBMITTED` ：应用程序已经提交给YARN，并在队列中等待调度资源。在该状态下，YARN只是对应用程序进行了初步的运行时配置，但还没有将任何容器分配到该应用程序。
4. `ACCEPTED` ：应用程序已经通过队列，并已经分配了它需要的初始和最小容器。
5. `RUNNING` ：应用程序正在运行中，并具有正在运行的容器。
6. `FINISHED` ：应用程序已经成功完成，并且其最终状态已经保存到YARN应用历史中。
7. `FAILED` ：应用程序运行失败，并且其最终状态已经保存到YARN应用历史中。
8. `KILLED` ：应用程序已被终止，并且其最终状态已经保存到YARN应用历史中。