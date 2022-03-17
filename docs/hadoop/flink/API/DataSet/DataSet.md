# DataSet API

DataSet API 分为以下三类：

- Source: 数据源创建初始数据集，例如来自文件或 Java 集合；
- Transformation: 数据转换将一个或多个 DataSet 转换为新的 DataSet；
- Sink: 将计算结果存储或返回。

## Source

### 基于文件

- `readTextFile(path)/ TextInputFormat` - 按行读取文件并将其作为字符串返回。
- `readTextFileWithValue(path)/ TextValueInputFormat` - 按行读取文件并将它们作为 StringValues 返回。StringValues 是可变字符串。
- `readCsvFile(path)/ CsvInputFormat` - 解析逗号（或其他字符）分隔字段的文件。返回元组或 POJO 的 DataSet。支持基本 java 类型及其 Value 对应作为字段类型。
- `readFileOfPrimitives(path, Class)/ PrimitiveInputFormat` - 解析新行（或其他字符序列）分隔的原始数据类型（如String 或 Integer）的文件。
- `readFileOfPrimitives(path, delimiter, Class)/ PrimitiveInputFormat` - 解析新行（或其他字符序列）分隔的原始数据类型的文件，例如 String 或 Integer 使用给定的分隔符。
- ~~`readSequenceFile(Key, Value, path)/ SequenceFileInputFormat` - 创建一个 JobConf 并从类型为 SequenceFileInputFormat，Key class 和 Value 类的指定路径中读取文件，并将它们作为 `Tuple2 <Key，Value>` 返回~~【1.12.1 版本已无此方法了】。

### 基于集合

- `fromCollection(Collection)` - 从 Java Java.util.Collection 创建数据集。集合中的所有数据元必须属于同一类型。
- `fromCollection(Iterator, Class)` - 从迭代器创建数据集。该类指定迭代器返回的数据元的数据类型。
- `fromElements(T ...)` - 根据给定的对象序列创建数据集。所有对象必须属于同一类型。
- `fromParallelCollection(SplittableIterator, Class)` - 并行地从迭代器创建数据集。该类指定迭代器返回的数据元的数据类型。
- `generateSequence(from, to)` - 并行生成给定间隔中的数字序列。

### 通用方法

- `readFile(inputFormat, path)/ FileInputFormat` - 接受文件输入格式。
- `createInput(inputFormat)/ InputFormat` - 接受通用输入格式。

## Transformation

Flink 针对 DataSet 提供了大量的已经实现的算子。

### map

> 输入一个元素，然后返回一个元素，中间可以进行清洗转换等操作。

**Java 版本**

```java
data.map(new MapFunction<String, Integer>() {
  public Integer map(String value) { return Integer.parseInt(value); }
});
```

### flatMap

> 输入一个元素，可以返回零个、一个或者多个元素。


**Java 版本**

```java
data.flatMap(new FlatMapFunction<String, String>() {
  public void flatMap(String value, Collector<String> out) {
    for (String s : value.split(" ")) {
      out.collect(s);
    }
  }
});
```

### mapPartition

> 类似 map，一次处理一个分区的数据（如果在进行 map 处理的时候需要获取第三方资源连接，建议使用 mapPartition）。

**Java 版本**

```java
data.mapPartition(new MapPartitionFunction<String, Long>() {
  public void mapPartition(Iterable<String> values, Collector<Long> out) {
    long c = 0;
    for (String s : values) {
      c++;
    }
    out.collect(c);
  }
});
```

### filter

> 过滤函数，对传入的数据进行判断，符合条件的数据会被留下。

**Java 版本**

```java
data.filter(new FilterFunction<Integer>() {
  public boolean filter(Integer value) { return value > 1000; }
});
```

### reduce

> 对数据进行聚合操作，结合当前元素和上一次 reduce 返回的值进行聚合操作，然后返回一个新的值。

**Java 版本**

```java
data.reduce(new ReduceFunction<Integer> {
  public Integer reduce(Integer a, Integer b) { return a + b; }
});
```

### reduceGroup

**Java 版本**

```java
data.reduceGroup(new GroupReduceFunction<Integer, Integer> {
  public void reduce(Iterable<Integer> values, Collector<Integer> out) {
    int prefixSum = 0;
    for (Integer i : values) {
      prefixSum += i;
      out.collect(prefixSum);
    }
  }
});
```

### aggregations

> sum、max、min等。

### distinct

> 返回一个数据集中去重之后的元素。

**Java 版本**

```java
data.distinct();
```

### join

> 内连接。

### outerJoin

> 外链接。

### cross

> 获取两个数据集的笛卡尔积。

### union

> 返回两个数据集的总和，数据类型需要一致。

**Java 版本**

```java
DataSet<String> data1 = // [...]
DataSet<String> data2 = // [...]
DataSet<String> result = data1.union(data2);
```

### first-n

> 获取集合中的前N个元素。

**Java 版本**

```java
DataSet<Tuple2<String,Integer>> in = // [...]
// regular data set
DataSet<Tuple2<String,Integer>> result1 = in.first(3);
// grouped data set
DataSet<Tuple2<String,Integer>> result2 = in.groupBy(0).first(3);
// grouped-sorted data set
DataSet<Tuple2<String,Integer>> result3 = in.groupBy(0).sortGroup(1, Order.ASCENDING).first(3);
```

### Sort Partition

> 在本地对数据集的所有分区进行排序，通过 `sortPartition()` 的链接调用来完成对多个字段的排序。

### 数据分区算子

Flink针对DataSet提供了一些数据分区规则，具体如下：

- Rebalance：对数据集进行再平衡、重分区以及消除数据倾斜操作。
  ```java
  DataSet<String> in = // [...]
  DataSet<String> result = in.rebalance().map(new Mapper());
  ```
- Hash-Partition：根据指定Key的散列值对数据集进行分区。
  ```java
  // partitionByHash()
  DataSet<Tuple2<String,Integer>> in = // [...]
  DataSet<Integer> result = in.partitionByHash(0)
                            .mapPartition(new PartitionMapper());
  ```
- Range-Partition：根据指定的Key对数据集进行范围分区。
  ```java
  // partitionByRange()
  DataSet<Tuple2<String,Integer>> in = // [...]
  DataSet<Integer> result = in.partitionByRange(0)
                            .mapPartition(new PartitionMapper());
  ```
- Custom Partitioning：自定义分区规则，自定义分区需要实现 Partitioner 接口
  ```java
  dss.partitionCustom(partitioner,"someKey");
  // 或者
  dss.partitionCustom(partitioner,0);
  ```

## Sink

DataSet 用 Sink 来设置存储或返回。使用 OutputFormat 描述数据接收器算子操作 。Flink 带有各种内置输出格式，这些格式封装在 DataSet 上的算子操作中：

- `writeAsText()/ TextOutputFormat` - 按字符串顺序写入数据元。通过调用每个数据元的 `toString()` 方法获得字符串。
- `writeAsFormattedText()/ TextOutputFormat` - 按字符串顺序写数据元。通过为每个数据元调用用户定义的 `format()` 方法来获取字符串。
- `writeAsCsv(...)/ CsvOutputFormat` - 将元组写为逗号分隔值文件。行和字段分隔符是可配置的。每个字段的值来自对象的 `toString()` 方法。
- `print()/ printToErr()/ print(String msg)/ printToErr(String msg)` - 在标准输出/标准错误流上打印每个数据元的 `toString()` 值。可选地，可以提供前缀（msg），其前缀为输出。这有助于区分不同的打印调用。如果并行度大于 1，则输出也将与生成输出的任务的标识符一起添加。
- `write()/ FileOutputFormat` - 自定义文件输出的方法和基类。支持自定义对象到字节的转换。
- `output()/ OutputFormat` - 大多数通用输出方法，用于非基于文件的数据接收器（例如将结果存储在数据库中）。

### writeAsText

> 将元素以字符串形式逐行写入，这些字符串通过调用每个元素的 `toString()` 方法来获取。

### writeAsCsv

> 将元组以逗号分隔写入文件中，行及字段之间的分隔是可配置的，每个字段的值来自对象的toString()方法。

### print

> 打印每个元素的 `toString()` 方法的值到标准输出或者标准错误输出流中。