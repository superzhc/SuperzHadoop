# 如何迁移 DataSet 到 DataStream

DataSet API 已被正式弃用，并且将不再获得主动的维护和支持，它将在 Flink 2.0 版本被删除。 建议 Flink 用户从 DataSet API 迁移到 DataStream API、Table API 和 SQL 来满足数据处理需求。

请注意，DataStream 中的 API 并不总是与 DataSet 完全匹配。 本文档的目的是帮助用户理解如何使用 DataStream API 实现与使用 DataSet API 相同的数据处理行为。

根据迁移过程中开发和执行效率的变化程度，我们将 DataSet API 分为四类：

- 第一类：在 DataStream 中具有完全相同的 API，几乎不需要任何更改即可迁移；
- 第二类：其行为可以通过 DataStream 中具有不同语义的其他 API 来实现，这可能需要更改一些代码，但仍保持相同的执行效率；
- 第三类：其行为可以通过 DataStream 中具有不同语义的其他 API 来实现，但可能会增加额外的执行效率成本；
- 第四类：其行为不被 DataStream API 支持。

> 后续章节将首先介绍如何设置执行环境和 source/sink ，然后详细解释每种类别的 DataSet API 如何迁移到 DataStream API，强调与每个类别迁移过程中相关的考虑因素和面临的挑战。

## 设置执行环境

将应用程序从 DataSet API 迁移到 DataStream API 的第一步是将 `ExecutionEnvironment` 替换为 `StreamExecutionEnvironment`。

| DataSet                                                      | DataStream                                                   |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| <code>// 创建执行环境 ExecutionEnvironment.getExecutionEnvironment(); // 创建本地执行环境 ExecutionEnvironment.createLocalEnvironment(); // 创建 collection 环境 new CollectionEnvironment(); // 创建远程执行环境 ExecutionEnvironment.createRemoteEnvironment(String host, int port, String... jarFiles);</code> | `// 创建执行环境 StreamExecutionEnvironment.getExecutionEnvironment(); // 创建本地执行环境 StreamExecutionEnvironment.createLocalEnvironment(); // 不支持 collection 环境 // 创建远程执行环境 StreamExecutionEnvironment.createRemoteEnvironment(String host, int port, String... jarFiles);` |

与 DataSet 不同，DataStream 支持对有界和无界数据流进行处理。

如果需要的话，用户可以显式地将执行模式设置为 `RuntimeExecutionMode.BATCH`。

```java
StreamExecutionEnvironment executionEnvironment = // [...];
executionEnvironment.setRuntimeMode(RuntimeExecutionMode.BATCH);
```

## 设置 streaming 类型的 Source 和 Sink

### Sources

DataStream API 使用 `DataStreamSource` 从外部系统读取记录，而 DataSet API 使用 `DataSource`。

| DataSet                                                      | DataStream                                                   |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| `// Read data from file DataSource<> source = ExecutionEnvironment.readFile(inputFormat, filePath); // Read data from collection DataSource<> source = ExecutionEnvironment.fromCollection(data); // Read data from inputformat DataSource<> source = ExecutionEnvironment.createInput(inputFormat)` | `// Read data from file DataStreamSource<> source = StreamExecutionEnvironment.readFile(inputFormat, filePath); // Read data from collection DataStreamSource<> source = StreamExecutionEnvironment.fromCollection(data); // Read data from inputformat DataStreamSource<> source = StreamExecutionEnvironment.createInput(inputFormat)` |

### Sinks

DataStream API 使用 `DataStreamSink` 将记录写入外部系统，而 DataSet API 使用 `DataSink`。

| DataSet                                                      | DataStream                                                   |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| `// Write to outputformat DataSink<> sink = dataSet.output(outputFormat); // Write to csv file DataSink<> sink = dataSet.writeAsCsv(filePath); // Write to text file DataSink<> sink = dataSet.writeAsText(filePath);` | `// Write to sink DataStreamSink<> sink = dataStream.sinkTo(sink) // Write to csv file DataStreamSink<> sink = dataStream.writeAsCsv(path); // Write to text file DataStreamSink<> sink = dataStream.writeAsText(path);` |

## 迁移 DataSet APIs

### 第一类

对于第一类，这些 DataSet API 在 DataStream 中具有完全相同的功能，几乎不需要任何更改即可迁移。

| Operations                   | DataSet                                                      | DataStream                                                   |
| :--------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| Map                          | `dataSet.map(new MapFunction<>(){ // implement user-defined map logic });` | `dataStream.map(new MapFunction<>(){ // implement user-defined map logic });` |
| FlatMap                      | `dataSet.flatMap(new FlatMapFunction<>(){ // implement user-defined flatmap logic });` | `dataStream.flatMap(new FlatMapFunction<>(){ // implement user-defined flatmap logic });` |
| Filter                       | `dataSet.filter(new FilterFunction<>(){ // implement user-defined filter logic });` | `dataStream.filter(new FilterFunction<>(){ // implement user-defined filter logic });` |
| Union                        | `dataSet1.union(dataSet2);`                                  | `dataStream1.union(dataStream2);`                            |
| Rebalance                    | `dataSet.rebalance();`                                       | `dataStream.rebalance();`                                    |
| Project                      | `DataSet<Tuple3<>> dataSet = // [...] dataSet.project(2,0);` | `DataStream<Tuple3<>> dataStream = // [...] dataStream.project(2,0);` |
| Reduce on Grouped DataSet    | `DataSet<Tuple2<>> dataSet = // [...] dataSet.groupBy(value -> value.f0)       .reduce(new ReduceFunction<>(){        // implement user-defined reduce logic        });` | `DataStream<Tuple2<>> dataStream = // [...] dataStream.keyBy(value -> value.f0)          .reduce(new ReduceFunction<>(){          // implement user-defined reduce logic          });` |
| Aggregate on Grouped DataSet | `DataSet<Tuple2<>> dataSet = // [...] // compute sum of the second field dataSet.groupBy(value -> value.f0)       .aggregate(SUM, 1); // compute min of the second field dataSet.groupBy(value -> value.f0)       .aggregate(MIN, 1); // compute max of the second field dataSet.groupBy(value -> value.f0)       .aggregate(MAX, 1);` | `DataStream<Tuple2<>> dataStream = // [...] // compute sum of the second field dataStream.keyBy(value -> value.f0)          .sum(1); // compute min of the second field dataStream.keyBy(value -> value.f0)          .min(1); // compute max of the second field dataStream.keyBy(value -> value.f0)          .max(1);` |

### 第二类

对于第二类，这些 DataSet API 的行为可以通过 DataStream 中具有不同语义的其他 API 来实现，这可能需要更改一些代码来进行迁移，但仍保持相同的执行效率。

DataSet 中存在对整个 DataSet 进行操作的 API。这些 API 在 DataStream 中可以用一个全局窗口来实现，该全局窗口只会在输入数据结束时触发窗口内数据的计算。 [附录](https://nightlies.apache.org/flink/flink-docs-release-1.20/zh/docs/dev/datastream/dataset_migration/#endofstreamwindows)中的 `EndOfStreamWindows` 显示了如何实现这样的窗口，我们将在本文档的其余部分重复使用它。

| Operations                     | DataSet                                                      | DataStream                                                   |
| :----------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| Distinct                       | `DataSet<Integer> dataSet = // [...] dataSet.distinct();`    | `DataStream<Integer> dataStream = // [...] dataStream.keyBy(value -> value)          .reduce((value1, value2) -> value1);` |
| Hash-Partition                 | `DataSet<Tuple2<>> dataSet = // [...] dataSet.partitionByHash(value -> value.f0);` | `DataStream<Tuple2<>> dataStream = // [...] // partition by the hashcode of key dataStream.partitionCustom(          (key, numSubpartition) -> key.hashCode() % numSubpartition,          value -> value.f0);` |
| Reduce on Full DataSet         | `DataSet<String> dataSet = // [...] dataSet.reduce(new ReduceFunction<>(){        // implement user-defined reduce logic        });` | `DataStream<String> dataStream = // [...] dataStream.windowAll(EndOfStreamWindows.get())          .reduce(new ReduceFunction<>(){          // implement user-defined reduce logic          });` |
| Aggregate on Full DataSet      | `DataSet<Tuple2<>> dataSet = // [...] // compute sum of the second field dataSet.aggregate(SUM, 1); // compute min of the second field dataSet.aggregate(MIN, 1); // compute max of the second field dataSet.aggregate(MAX, 1);` | `DataStream<Tuple2<>> dataStream = // [...] // compute sum of the second field dataStream.windowAll(EndOfStreamWindows.get())          .sum(1); // compute min of the second field dataStream.windowAll(EndOfStreamWindows.get())          .min(1); // compute max of the second field dataStream.windowAll(EndOfStreamWindows.get())          .max(1);` |
| GroupReduce on Full DataSet    | `DataSet<Integer> dataSet = // [...] dataSet.reduceGroup(new GroupReduceFunction<>(){        // implement user-defined group reduce logic        });` | `DataStream<Integer> dataStream = // [...] dataStream.windowAll(EndOfStreamWindows.get())          .apply(new WindowFunction<>(){          // implement user-defined group reduce logic          });` |
| GroupReduce on Grouped DataSet | `DataSet<Tuple2<>> dataSet = // [...] dataSet.groupBy(value -> value.f0)       .reduceGroup(new GroupReduceFunction<>(){       // implement user-defined group reduce logic       });` | `DataStream<Tuple2<>> dataStream = // [...] dataStream.keyBy(value -> value.f0)          .window(EndOfStreamWindows.get())          .apply(new WindowFunction<>(){          // implement user-defined group reduce logic          });` |
| First-n                        | `dataSet.first(n)`                                           | `dataStream.windowAll(EndOfStreamWindows.get())          .apply(new AllWindowFunction<>(){          // implement first-n logic          });` |
| Join                           | `DataSet<Tuple2<>> dataSet1 = // [...] DataSet<Tuple2<>> dataSet2 = // [...] dataSet1.join(dataSet2)        .where(value -> value.f0)        .equalTo(value -> value.f0)        .with(new JoinFunction<>(){        // implement user-defined join logic        });` | `DataStream<Tuple2<>> dataStream1 = // [...] DataStream<Tuple2<>> dataStream2 = // [...] dataStream1.join(dataStream2)           .where(value -> value.f0)           .equalTo(value -> value.f0)           .window(EndOfStreamWindows.get()))           .apply(new JoinFunction<>(){           // implement user-defined join logic           });` |
| CoGroup                        | `DataSet<Tuple2<>> dataSet1 = // [...] DataSet<Tuple2<>> dataSet2 = // [...] dataSet1.coGroup(dataSet2)        .where(value -> value.f0)        .equalTo(value -> value.f0)        .with(new CoGroupFunction<>(){        // implement user-defined co group logic        });` | `DataStream<Tuple2<>> dataStream1 = // [...] DataStream<Tuple2<>> dataStream2 = // [...] dataStream1.coGroup(dataStream2)           .where(value -> value.f0)           .equalTo(value -> value.f0)           .window(EndOfStreamWindows.get()))           .apply(new CoGroupFunction<>(){           // implement user-defined co group logic           });` |
| OuterJoin                      | `DataSet<Tuple2<>> dataSet1 = // [...] DataSet<Tuple2<>> dataSet2 = // [...] // left outer join dataSet1.leftOuterJoin(dataSet2)        .where(dataSet1.f0)        .equalTo(dataSet2.f0)        .with(new JoinFunction<>(){        // implement user-defined left outer join logic        }); // right outer join dataSet1.rightOuterJoin(dataSet2)        .where(dataSet1.f0)        .equalTo(dataSet2.f0)        .with(new JoinFunction<>(){        // implement user-defined right outer join logic        });` | `    DataStream<Tuple2<>> dataStream1 = // [...]    DataStream<Tuple2<>> dataStream2 = // [...]    // left outer join    dataStream1.coGroup(dataStream2)               .where(value -> value.f0)               .equalTo(value -> value.f0)               .window(EndOfStreamWindows.get())               .apply((leftIterable, rightInterable, collector) -> {    if(!rightInterable.iterator().hasNext()){    // implement user-defined left outer join logic    } });    // right outer join    dataStream1.coGroup(dataStream2)               .where(value -> value.f0)               .equalTo(value -> value.f0)               .window(EndOfStreamWindows.get())               .apply((leftIterable, rightInterable, collector) -> {    if(!leftIterable.iterator().hasNext()){    // implement user-defined right outer join logic    } });` |

### 第三类

对于第三类，这些 DataSet API 的行为可以通过 DataStream 中具有不同语义的其他 API 来实现，但可能会增加额外的执行效率成本。

目前，DataStream API 不直接支持 non-keyed 流上的聚合（对 subtask 内的数据进行聚合）。为此，我们需要首先将 subtask ID 分配给记录，然后将流转换为 keyed 流。 [附录](https://nightlies.apache.org/flink/flink-docs-release-1.20/zh/docs/dev/datastream/dataset_migration/#addsubtaskidmapfunction)中的 `AddSubtaskIdMapFunction` 显示了如何执行此操作，我们将在本文档的其余部分中重复使用它。

| Operations                 | DataSet                                                      | DataStream                                                   |
| :------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| MapPartition/SortPartition | `DataSet<Integer> dataSet = // [...] // MapPartition dataSet.mapPartition(new MapPartitionFunction<>(){        // implement user-defined map partition logic        }); // SortPartition dataSet.sortPartition(0, Order.ASCENDING); dataSet.sortPartition(0, Order.DESCENDING);` | `DataStream<Integer> dataStream = // [...] // assign subtask ID to all records DataStream<Tuple2<String, Integer>> dataStream1 = dataStream.map(new AddSubtaskIDMapFunction()); dataStream1.keyBy(value -> value.f0)           .window(EndOfStreamWindows.get())           .apply(new WindowFunction<>(){           // implement user-defined map partition or sort partition logic           });` |
| Cross                      | `DataSet<Integer> dataSet1 = // [...] DataSet<Integer> dataSet2 = // [...] // Cross dataSet1.cross(dataSet2)        .with(new CrossFunction<>(){        // implement user-defined cross logic        })` | `// the parallelism of dataStream1 and dataStream2 should be same DataStream<Integer> dataStream1 = // [...] DataStream<Integer> dataStream2 = // [...] DataStream<Tuple2<String, Integer>> datastream3 = dataStream1.broadcast().map(new AddSubtaskIDMapFunction()); DataStream<Tuple2<String, Integer>> datastream4 = dataStream2.map(new AddSubtaskIDMapFunction()); // join the two streams according to the subtask ID dataStream3.join(dataStream4)           .where(value -> value.f0)           .equalTo(value -> value.f0)           .window(EndOfStreamWindows.get())           .apply(new JoinFunction<>(){           // implement user-defined cross logic           })` |

### 第四类

以下 DataSet API 的行为不被 DataStream 支持。

- RangePartition
- GroupCombine

## 附录

#### EndOfStreamWindows

以下代码展示了 `EndOfStreamWindows` 示例实现。

```java
public class EndOfStreamWindows extends WindowAssigner<Object, TimeWindow> {
    private static final long serialVersionUID = 1L;

    private static final EndOfStreamWindows INSTANCE = new EndOfStreamWindows();

    private static final TimeWindow TIME_WINDOW_INSTANCE =
            new TimeWindow(Long.MIN_VALUE, Long.MAX_VALUE);

    private EndOfStreamWindows() {}

    public static EndOfStreamWindows get() {
        return INSTANCE;
    }

    @Override
    public Collection<TimeWindow> assignWindows(
            Object element, long timestamp, WindowAssignerContext context) {
        return Collections.singletonList(TIME_WINDOW_INSTANCE);
    }

    @Override
    public Trigger<Object, TimeWindow> getDefaultTrigger(StreamExecutionEnvironment env) {
        return new EndOfStreamTrigger();
    }

    @Override
    public String toString() {
        return "EndOfStreamWindows()";
    }

    @Override
    public TypeSerializer<TimeWindow> getWindowSerializer(ExecutionConfig executionConfig) {
        return new TimeWindow.Serializer();
    }

    @Override
    public boolean isEventTime() {
        return true;
    }
    
    @Internal
    public static class EndOfStreamTrigger extends Trigger<Object, TimeWindow> {
        @Override
        public TriggerResult onElement(
                Object element, long timestamp, TimeWindow window, TriggerContext ctx)
                throws Exception {
            return TriggerResult.CONTINUE;
        }

        @Override
        public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) {
            return time == window.maxTimestamp() ? TriggerResult.FIRE : TriggerResult.CONTINUE;
        }

        @Override
        public void clear(TimeWindow window, TriggerContext ctx) throws Exception {}

        @Override
        public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) {
            return TriggerResult.CONTINUE;
        }
    }
}
```

#### AddSubtaskIDMapFunction

以下代码展示了 `AddSubtaskIDMapFunction` 示例实现。

```java
public static class AddSubtaskIDMapFunction<T> extends RichMapFunction<T, Tuple2<String, T>> {
    @Override
    public Tuple2<String, T> map(T value) {
        return Tuple2.of(String.valueOf(getRuntimeContext().getIndexOfThisSubtask()), value);
    }
}
```