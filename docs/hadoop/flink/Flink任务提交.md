# Flink 任务提交

## Standalone 模式

**前提**

需提前以 Standalone 部署方式，启动 Flink 集群

**使用**

```shell
cd ${FLINK_HOME}

./bin/flink run 
-c com.github.superzhc.flink.test.WordCount 
~/superz-flink-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Yarn-Session 模式

Yarn-Session 模式表示在 Yarn 中创建一个一直运行的 Flink 集群

**使用**

1. 在 Yarn 中启动一个 Flink 集群
```shell
cd ${FLINK_HOME}

./bin/yarn-session.sh -n 2 -jm 1024 -tm 1024 -d
```
2. 提交作业到创建好的集群上
```shell
${FLINK_HOME}

./bin/flink run ./examples/batch/WordCount.jar
```

## Yarn-Per-Job 模式

Yarn-Per-Job 模式表示提交 Flink 任务的同时创建 Flink 集群。

```shell
${FLINK_HOME}

./bin/flink run \
-m yarn-cluster \
./examples/batch/WordCount.jar
```

## Application 模式

> Flink-1.11 引入的新的部署模式。目前 Flink-1.11 已经可以支持基于 Yarn 和 Kubernetes 的 Application 模式。

Application 模式下，用户程序的 main 方法将在集群中而不是客户端运行，用户将程序逻辑和依赖打包进一个可执行的 jar 包里，集群的入口程序 (ApplicationClusterEntryPoint) 负责调用其中的 main 方法来生成 JobGraph。

Application 模式为每个提交的应用程序创建一个集群，该集群可以看作是在特定应用程序的作业之间共享的会话集群，并在应用程序完成时终止。

在这种体系结构中，Application 模式在不同应用之间提供了资源隔离和负载平衡保证。在特定一个应用程序上，JobManager 执行 main() 可以节省所需的 CPU 周期，还可以节省本地下载依赖项所需的带宽。

**使用**

Application 模式使用 `${FLINK_HOME}/bin/flink run-application` 提交作业，参数如下：

- `-t` 指定部署环境，目前 application 模式支持部署在 yarn 上(`-t yarn-application`) 和 k8s 上(`-t kubernetes-application`)；
- `-D` 指定通用的运行配置，如 jobmanager/taskmanager 内存、checkpoint 时间间隔
- ~~`-e` 该参数目前已被废弃~~
- `-h` 查看参数帮助

*`-Dyarn.provided.lib.dirs`*

该参数表示用户可以预先上传 flink 客户端依赖包到远程存储（一般是 HDFS），然后通过该参数指定路径，flink 检测到这个配置时，就会从该地址拉取 flink 运行需要的依赖包，省去依赖包上传的过程。

**示例**

```shell
cd ${FLINK_YHOME}

./bin/flink run-application \
-t yarn-application \
-Dparallelism.default=4 \
-Djobmanager.memory.process.size=1024m \
-Dtaskmanager.memory.process.size=2048m \
-Dtaskmanager.numberOfTaskSlots=3 \
./examples/streaming/SocketWindowWordCount.jar \
--hostname log-platform02 --port 9000
```