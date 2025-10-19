# Flink On Yarn 模式

1. 设置 `HADOOP_HOME`
   ```sh
   export HADOOP_HOME=/data/flink-client/hadoop-3.1.1
   export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
   ```
2. 设置 `HADOOP_CLASSPATH`：flink 获取 Hadoop 依赖包所读取的路径
   ```sh
   export HADOOP_CLASSPATH=`hadoop classpath`
   ```
3. 设置 `HADOOP_CONF_DIR`：flink 获取 Hadoop 配置文件读取的路径
   ```sh
   export HADOOP_CONF_DIR=/data/flink-client/hadoop-3.1.1/etc/hadoop
   ```
4. 设置 `FLINK_LIB_DIR`
   ```sh
   export FLINK_LIB_DIR=/data/flink-client/flink-1.16.2/lib/
   ```

## `Per-Job` Mode

```sh
./bin/flink run \
    -t yarn-per-job \
    -Dyarn.application.name=dm_flink_test \
    -Dyarn.application.queue=default \
    -c com.flink.streaming.visualization.interpreter.FlinkJobApplication \
    /data/flink-client/custom/flink-streaming-visualization-interpreter-1.5.0.RELEASE.jar \
    -graph "{\"nodes\":[{\"id\":\"PtznjMcl0m\",\"name\":\"随机生成数据\",\"operator\":\"faker\",\"parameter\":{\"fields\":[{\"name\":\"name\",\"type\":\"expression\",\"parameter\":\"#{Name.name}\"},{\"name\":\"gender\",\"type\":\"options\",\"parameter\":\"F,M\"},{\"name\":\"age\",\"type\":\"expression\",\"parameter\":\"#{number.number_between '1','120'}\"}]},\"extraInfo\":{\"position\":{\"x\":195,\"y\":80},\"status\":\"Stopped\",\"validationErrors\":[]}},{\"id\":\"FrdZOfrJlg\",\"name\":\"Print输出\",\"operator\":\"console\",\"parameter\":{},\"extraInfo\":{\"position\":{\"x\":380,\"y\":80},\"status\":\"Stopped\",\"validationErrors\":[]}}],\"edges\":[{\"start\":\"PtznjMcl0m\",\"end\":\"FrdZOfrJlg\"}],\"name\":\"dev_test_20250411\"}" -type 3 -name dev_flink_job_20250304 -stateBackendType 0 -checkpointDir memory
```

## `Application` Mode

```sh
./bin/flink run-application -t yarn-application -Dyarn.application.name=dm_flink_application_test -Dyarn.application.queue=default -Dclassloader.resolve-order=parent-first -c com.flink.streaming.visualization.interpreter.FlinkJobApplication /data/flink-client/custom/flink-streaming-visualization-interpreter-1.5.0.RELEASE.jar -graph "{\"nodes\":[{\"id\":\"PtznjMcl0m\",\"name\":\"随机生成数据\",\"operator\":\"faker\",\"parameter\":{\"fields\":[{\"name\":\"姓名\",\"type\":\"options\",\"parameter\":\"张三,李四,王五\"},{\"name\":\"性别\",\"type\":\"options\",\"parameter\":\"F,M\"}]},\"extraInfo\":{\"position\":{\"x\":195,\"y\":80},\"status\":\"Stopped\",\"validationErrors\":[]}},{\"id\":\"FrdZOfrJlg\",\"name\":\"Print输出\",\"operator\":\"console\",\"parameter\":{},\"extraInfo\":{\"position\":{\"x\":380,\"y\":80},\"status\":\"Stopped\",\"validationErrors\":[]}}],\"edges\":[{\"start\":\"PtznjMcl0m\",\"end\":\"FrdZOfrJlg\"}],\"name\":\"dev_test_20250411\"}" -type 3 -name dev_flink_job_20250304 -stateBackendType 0 -checkpointDir memory
```