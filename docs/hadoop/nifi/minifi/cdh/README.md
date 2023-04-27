# 边缘管理

Cloudera Edge Management（CEM）是由边缘代理和边缘管理中心组成的边缘管理解决方案。它管理、控制和监视边缘代理从边缘设备收集数据并将智能推回边缘。

CEM由两个组成部分组成：

- Apache minifi
- Edge Flow Manager（EFM）

CEM 为 Edge Flow LifeCycle 提供了三个主要功能：

- Flow Creation
- Flow Deployment
- Flow Monitoring

## 概述

Cloudera Edge Management (CEM) 是一款边缘管理解决方案，可以帮助边缘设备管理员、数据工程师和IoT管理员通过实时边缘数据收集和管理来控制边缘设备的数据。熟悉CEM、其组件、主要功能和用例。

CEM由边缘代理和管理代理的管理中心组成。管理中心可以管理、控制和监视代理，以从边缘设备收集数据并将智能推送回这些设备。CEM提供开箱即用的数据谱系跟踪和流动数据的来源证明。

### 组件

CEM由两个组件组成：

- Edge Flow Manager（EFM）是一个代理管理中心，支持基于图形的基于流程的编程模型，用于在数千个MiNiFi代理上开发、部署和监视边缘流。
- Apache MiNiFi 是一个轻量级的边缘代理，实现了Apache NiFi的核心功能，重点是在边缘收集、处理和分发数据。它可以嵌入到任何小型边缘设备中，如传感器或Raspberry Pi。它有两种版本可用：Java和C++。

### 功能

CEM提供边缘流生命周期的三个主要能力：

**Flow creation**

EFM通过提供无代码拖放式开发环境来解决开发IoT应用程序的挑战。该开发环境提供了类似于NiFi的体验，用于从边缘代理捕获、过滤、转换和传输数据到上游企业系统，如CDP Private Cloud Base或CDP Public Cloud。

**Flow deployment**

管理IoT应用程序的部署一直是行业挑战。EFM通过为代理提供简单而强大的流程部署模型来减轻这一挑战。已向EFM注册的代理将在新流程或修改后的流程可用时收到通知。代理访问并在本地应用流程。

**Flow monitoring**

CEM中的代理定期向其EFM实例发送心跳。心跳包含有关部署和运行时指标的信息。EFM存储、分析和呈现这些心跳包给最终用户。这些心跳包使操作员能够可视化详细信息，例如流量吞吐量、连接深度、运行的处理器和整体代理健康状况。

### 主要特性

- 无代码拖放式用户界面

提供数百个预构建处理器，可连接各种数据源、设备和协议，构建复杂的数据流水线。

- 边缘管理中心【Edge management hub】

您可以从任何流媒体源（包括点击流、社交媒体、移动设备或物联网设备）实时摄取、捕获和传递数据。

- 边缘流程设计师【Flow designer for edge flows】

您可以通过类似NiFi的用户界面可视化构建边缘数据流，实现边缘数据采集和处理。

- MiNiFi边缘代理【MiNiFi edge agents】

轻量级和便携式的C++和Java代理，不断生成精细的数据血统信息。

- 企业级安全和数据可溯性

具备强大的身份验证和授权选项，以及开箱即用的数据血统跟踪和运动数据的可溯源性。

- 边缘管理和数据收集【Edge management and data collection】

定制的仪表板可以在最小的占用空间下实现大规模的边缘管理，命令、控制和监控成百上千个代理，以收集、过滤和处理数据。您可以同时向数千个边缘代理部署更新。

## 开始

### Cloudera Edge Management

Cloudera Edge Management（CEM）是一种解决方案，可帮助您管理、控制和监视部署在IoT实现中的边缘设备上的代理。您可以使用这些代理收集来自设备的实时数据，创建并将可操作的智能和洞见推送回数据源。

CEM由两个组件组成：

- MiNiFi-一种轻量级的边缘代理，实现了Apache NiFi的核心功能，重点是在边缘进行数据收集和处理。
- Edge Flow Manager (EFM) - 一个代理管理中心，支持基于图形流程的编程模型，以开发、部署和监视成千上万的MiNiFi代理的边缘流程。

CEM可以将时间序列指标导出到多个指标存储提供程序。推荐的度量存储服务是Prometheus。Prometheus与Grafana集成，用于时间序列度量可视化。使用Prometheus和Grafana，您可以存储和可视化CEM的指标。

**MiNiFi**

MiNiFi是一种边缘代理，您可以将其部署到数千个边缘设备上来收集数据。它是NiFi的轻量级版本，在边缘充当运行时以执行数据流。

**Edge Flow Manager**

Edge Flow Manager（EFM）是一个管理中心，支持基于GUI的工具来管理、控制和监视在现场部署的MiNiFi代理。EFM通过让您可视化构建数据流程，从而减少开发IoT应用程序的时间和成本，无需编写任何代码。

### Cloudera Edge Management（CEM）用户界面

