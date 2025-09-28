# UDF

> Flink SQL 的自定义函数是用户可以自行编写的一种函数，用于扩展 Flink SQL 的功能。自定义函数可以在 SQL 查询中被调用，以完成用户自定义的数据处理逻辑。 在 Flink SQL 中，自定义函数分为 **标量函数**、**表函数** 和 **聚合函数** 三种类型。
>
> 在使用自定义函数时，需要将对应的 Jar 包提交到 Flink 集群中，并在执行任务时将其加入到 Classpath 中。Flink SQL 还提供了 `CREATE FUNCTION` 语句来注册用户自定义的函数，以便在 SQL 查询中进行调用。 总的来说，自定义函数是 Flink SQL 中非常重要的一个功能，可以帮助用户扩展 Flink SQL 的功能，提高数据处理的灵活性和效率。

## 标量函数（Scalar Function）

> 标量函数接受一行输入，返回一行输出。
> 
> 常见的标量函数有字符串函数、数学函数等。用户可以通过继承 `ScalarFunction` 类或实现 `ScalarFunction` 接口的方式来实现自定义的标量函数。

## 表函数（Table Function）

> 表函数接受一行输入，返回多行输出。
> 
> 在 Flink SQL 中，表函数可以使用 `LATERAL TABLE` 语法进行调用。用户可以通过继承 `TableFunction` 类或实现 `TableFunction` 接口的方式来实现自定义的表函数。

## 聚合函数（Aggregate Function）

> 聚合函数接受多行输入，返回一行输出。
> 
> 在 Flink SQL 中，聚合函数可以使用 `GROUP BY` 语法进行调用。用户可以通过继承 `AggregateFunction` 类或实现 `AggregateFunction` 接口的方式来实现自定义的聚合函数。 
 
自定义聚合函数是通过扩展 `AggregateFunction` 来实现的。`AggregateFunction` 的工作过程如下。首先，它需要一个 `accumulator`，它是一个数据结构，存储了聚合的中间结果。通过调用 `AggregateFunction` 的 `createAccumulator()` 方法创建一个空的 `accumulator`。接下来，对于每一行数据，会调用 `accumulate()` 方法来更新 `accumulator`。当所有的数据都处理完了之后，通过调用 `getValue` 方法来计算和返回最终的结果。

## 表值聚合函数

> 自定义表值聚合函数（UDTAGG）可以把一个表（一行或者多行，每行有一列或者多列）聚合成另一张表，结果中可以有多行多列。

用户自定义表值聚合函数是通过扩展 `TableAggregateFunction` 类来实现的。一个 `TableAggregateFunction` 的工作过程如下。首先，它需要一个 `accumulator`，这个 `accumulator` 负责存储聚合的中间结果。 通过调用 `TableAggregateFunction` 的 `createAccumulator` 方法来构造一个空的 `accumulator`。接下来，对于每一行数据，会调用 `accumulate` 方法来更新 `accumulator`。当所有数据都处理完之后，调用 `emitValue` 方法来计算和返回最终的结果。

## 异步表值函数

> 异步表值函数是异步查询外部数据系统的特殊函数。