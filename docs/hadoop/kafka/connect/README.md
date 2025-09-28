# Kafka Connect

Kafka Connect 是 Apache Kafka 生态系统中的一个关键组件，旨在简化数据源和数据目标之间的数据集成。它提供了一种可扩展且可靠的方式，将数据从外部系统（如数据库、消息队列、文件系统等）导入到 Kafka，或者将数据从 Kafka 导出到外部系统。

Kafka Connect 是一个框架，用于在 Kafka 和其他系统之间进行可扩展且可靠的数据传输。它通过 **连接器（Connectors）** 来实现这一点，连接器是预定义的插件，用于与特定类型的外部系统进行交互。Kafka Connect 提供了两种类型的连接器：

- Source Connector：从外部系统读取数据并将其写入 Kafka。
- Sink Connector：从 Kafka 读取数据并将其写入外部系统。

Kafka Connect 的核心优势在于它的可扩展性和易用性。你可以轻松地配置和使用现有的连接器，也可以根据需要开发自定义连接器。

## 工作原理

Kafka Connect 的核心组件包括：

- Connectors：负责管理数据流的任务。
- Tasks：实际执行数据导入或导出的工作单元。
- Workers：运行 Connectors 和 Tasks 的进程。
- Converters：负责将数据格式转换为 Kafka 可以理解的格式（如 JSON、Avro 等）。

Kafka Connect 的工作流程如下：

1. 配置连接器：定义数据源或数据目标的连接器配置。
2. 启动连接器：Kafka Connect 会根据配置启动相应的任务。
3. 数据流动：Source Connector 从外部系统读取数据并写入 Kafka，Sink Connector 从 Kafka 读取数据并写入外部系统。

## 核心架构

- 连接器（Connectors）
- 任务（Tasks）
- 工作线程（Workers）
- 转换器（Converters）
- 插件（Plugins）

### 连接器（Connectors）

连接器是 Kafka Connect 的核心组件之一，负责定义数据源或数据目标的配置。连接器分为两种类型：

- 源连接器（Source Connector）：从外部系统读取数据并将其写入 Kafka。
- 接收连接器（Sink Connector）：从 Kafka 读取数据并将其写入外部系统。

连接器的主要职责是管理任务的创建和分配，并监控任务的状态。

### 任务（Tasks）

任务是实际执行数据传输的单元。每个连接器可以创建多个任务，以实现并行处理。任务负责从数据源读取数据或向数据目标写入数据。

> 任务的并行度可以通过配置参数 tasks.max 来控制。适当增加任务数量可以提高数据传输的吞吐量。

### 工作线程（Workers）

工作线程是 Kafka Connect 的运行时环境，负责执行连接器和任务。在分布式模式下，多个工作线程可以组成一个集群，共同处理数据传输任务。

### 转换器（Converters）

转换器用于在 Kafka 和外部系统之间进行数据格式的转换。常见的转换器包括：

- StringConverter：将数据转换为字符串格式。
- JsonConverter：将数据转换为 JSON 格式。
- AvroConverter：将数据转换为 Avro 格式。

### 插件（Plugins）

插件是 Kafka Connect 的扩展机制，允许开发者自定义连接器、转换器和数据格式。Kafka Connect 提供了丰富的插件生态系统，支持与多种数据源和目标系统的集成。

## 工作流程

1. 配置连接器：定义数据源或数据目标的配置。
2. 启动连接器：连接器根据配置创建任务。
3. 任务执行：任务从数据源读取数据或向数据目标写入数据。
4. 数据转换：转换器将数据转换为目标格式。
5. 数据存储：数据被写入 Kafka 或外部系统。

## 示例

```json
{
  "name": "file-source-connector",
  "config": {
    "connector.class": "FileStreamSource",
    "tasks.max": "1",
    "file": "/path/to/input/file.txt",
    "topic": "test-topic"
  }
}
```

在这个示例中，我们配置了一个 FileStreamSource 连接器，它会从指定的文件中读取数据并将其写入 Kafka 的 `test-topic` 主题。

## 参考

- [kafka connector 使用总结以及自定义connector开发](https://www.cnblogs.com/laoqing/p/11927958.html)