# History Server

## 1. JobManager 配置任务日志持久化存储目录

`$FLINK_HOME/conf/flink-conf.yaml`

```yaml
#Flink job运行完成后日志存储目录
#支持file:///xxx 协议
jobmanager.archive.fs.dir: hdfs://mycluster/flink/completed-jobs/
```

## 2. 配置 HistroyServer

`$FLINK_HOME/conf/flink-conf.yaml`

```yaml
#Flink History Server 节点
historyserver.web.address: node4

#Flink History Server 端口
historyserver.web.port: 8082

#Flink History Server 恢复任务的目录
#支持file:///xxxx 协议
historyserver.archive.fs.dir: hdfs://mycluster/flink/completed-jobs/

#Flink History Server 监控任务日志目录刷新时间间隔（毫秒）
historyserver.archive.fs.refresh-interval: 10000
```

## 3. 启动 HistoryServer

```bash
./bin/historyserver.sh start
```