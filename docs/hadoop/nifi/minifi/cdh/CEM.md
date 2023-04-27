# Cloudera Edge Management(CEM)

Cloudera Edge Management（CEM）是由边缘代理 \[MiNiFi\] 和边缘管理中心 \[Edge Flow Manager(EFM)\] 组成的边缘管理（Edge Management）解决方案。它管理、控制和监控边缘代理，可以从边缘设备收集数据并将数据推回边缘设备。

CEM由两个组件组成：

- Edge Flow Manager（EFM）是一个边缘管理中心，它支持基于图形界面的流（Flow）编程模型，可以在数千个 MiNiFi 代理上开发、部署和监控边缘流。
- Apache MiNiFi 是一个轻量级的边缘代理，实现了Apache NiFi的核心功能，重点是在边缘收集、处理和分发数据。它可以嵌入到任何小型边缘设备中，如传感器或Raspberry Pi。它有两种版本可用：Java和C++。

CEM为边缘流（Edge Flow）生命周期提供了三种主要功能：

**Flow creation**

EFM通过提供无代码拖放式开发环境来解决开发IoT应用程序的挑战。该开发环境提供了类似于NiFi的体验，用于从边缘代理捕获、过滤、转换和传输数据到上游企业系统，如CDP Private Cloud Base或CDP Public Cloud。

**Flow deployment**

管理IoT应用程序的部署一直是行业挑战。EFM通过为代理提供简单而强大的流程部署模型来减轻这一挑战。已向EFM注册的代理将在新流程或修改后的流程可用时收到通知。代理访问并在本地应用流程。

**Flow monitoring**

CEM中的代理定期向其EFM实例发送心跳。心跳包含有关部署和运行时指标的信息。EFM存储、分析和呈现这些心跳包给最终用户。这些心跳包使操作员能够可视化详细信息，例如流量吞吐量、连接深度、运行的处理器和整体代理健康状况。