1. 执行 `docker-compose up -d` 启动 hadoop 初始环境【即已下载的 Hadoop 安装包，但无任何服务启动】

[//]: # (2. namenode)

[//]: # (   1. 创建 namenode 数据目录：`mkdir -p /hadoop/dfs/name`)

[//]: # (   2. 创建临时数据存放目录：`mkdir -p /hadoop/dfs/tmp`)

[//]: # (   3. 执行 namenode 初始化：`$HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode -format test`)

[//]: # (   4. 启动 namenode ：`nohup $HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode &`)

[//]: # (3. datanode)

[//]: # (   1. 创建 datanode 数据目录：`mkdir -p /hadoop/dfs/data`)

[//]: # (   2. 创建临时数据存放目录：`mkdir -p /hadoop/dfs/tmp`)

[//]: # (   3. 启动 datanode ：`$HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR datanode`)

2. 启动 yarn 环境
   - 在 namenode 下执行：`nohup $HADOOP_HOME/bin/yarn --config $HADOOP_CONF_DIR resourcemanager &`
   - 在 datanode[\*] 下执行： `nohup $HADOOP_HOME/bin/yarn --config $HADOOP_CONF_DIR nodemanager &`
3. hive 安装
   1. 下载 hive 包：`https://mirrors.tuna.tsinghua.edu.cn/apache/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz`
   2. 将本地下载的包上传到容器中：`docker cp <本地文件路径> <id全称>:<容器路径>`