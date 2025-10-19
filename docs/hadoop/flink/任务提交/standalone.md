# Standalone 模式

```sh
./bin/flink run \
    -d \
    -m 10.90.20.69:8081 \
    # 注意，此处指定依赖包需要 Standalone 集群上的所有机器都可通过该本地路径访问到该文件，此种方式下，需要搭配使用 NFS
    # 支持 HTTP 协议的文件，即支持HTTP可以下载到依赖包
    # -C file:///data/flink-client/custom/smile-core-3.0.1.jar \
    # -C file:///data/flink-client/custom/opencc4j-1.8.1.jar \
    -c com.flink.streaming.visualization.interpreter.FlinkJobApplication \
    /data/flink-client/custom/flink-streaming-visualization-interpreter-1.5.0.RELEASE.jar \
    -graphpath /data/flink-client/custom/job_canvas_10.json -type 3
```