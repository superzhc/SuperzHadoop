# 监控 JVM

## JMX Exporter

JMX Exporter 利用 Java 的 JMX 机制来读取 JVM 运行时的一些监控数据，然后将其转换为 Prometheus 所认知的 metrics 格式，以便让 Prometheus 对其进行监控采集。

JMX-Exporter 提供了两种用法:

1. 启动独立进程。JVM 启动时指定参数，暴露 JMX 的 RMI 接口，JMX-Exporter 调用 RMI 获取 JVM 运行时状态数据，转换为 Prometheus metrics 格式，并暴露端口让 Prometheus 采集。
2. JVM 进程内启动(in-process)。JVM 启动时指定参数，通过 javaagent 的形式运行 JMX-Exporter 的 jar 包，进程内读取 JVM 运行时状态数据，转换为 Prometheus metrics 格式，并暴露端口让 Prometheus 采集。

> 官方不推荐使用第一种方式，一方面配置复杂，另一方面因为它需要一个单独的进程，而这个进程本身的监控又成了新的问题

## 参考

- [在云原生中监控JVM指标](https://www.cnblogs.com/hahaha111122222/p/16189637.html)
- [Prometheus+Grafana监控JVM实战](https://www.cnblogs.com/wx170119/p/16927890.html)
- [client_java](https://prometheus.github.io/client_java/)
- [prometheus/client_java](https://github.com/prometheus/client_java)
- [jmx-exporter](https://github.com/prometheus/jmx_exporter)