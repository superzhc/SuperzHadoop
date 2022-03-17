# NodeManager

NodeManager 是 Hadoop Yarn 在每个计算节点上的代理，它根据 YARN 应用程序的要求，使用节点上的物理资源来运行 Container。**NodeManager 本质上是 YARN 的工作守护进程**，主要有以下职责：

- 保持与 ResourceManager 的同步
- 跟踪节点的健康状况
- 管理各个 Container 的生命周期，监控每个 Container 的资源使用情况（如内存，CPU）
- 管理分布式缓存（对 Container 所需的 Jar、库等文件的本地文件系统缓存）
- 管理各个 Container 生成的日志
- 不同的 YARN 应用可能需要的辅助服务

> Container 的管理是 NodeManager 的核心功能。