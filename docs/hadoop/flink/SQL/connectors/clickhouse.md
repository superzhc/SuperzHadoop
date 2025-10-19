# Clickhouse

自研Clickhouse连接器

```xml
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-clickhouse-connector</artifactId>
    <version>1.1</version>
    <scope>provided</scope>
</dependency>
```

## Source

```sql
CREATE TABLE t_clickhouse_input(
	`id` String,
    `device_id` String,
    `intvalue` INT,
    `longvalue` BIGINT,
    `stringvalue` String,
    `sign` Int
)WITH (
    'connector' = 'clickhouse',
    'url' = 'jdbc:clickhouse://127.0.0.1:8123',
    'username' = 'root',
    'password' = '123456',
    -- 默认数据库是 default
	--'database-name' = 'default',
    'table-name' = 'test1'
);
```

## Sink

```sql
CREATE TABLE t_clickhouse_output (
    id INT,
    name String,
    age INT
) WITH (
    'connector' = 'clickhouse',
    'url' = 'jdbc:clickhouse://10.90.20.197:8123',
    'username' = 'hanyun',
    'password' = 'c#sh4Y5I',
	'database-name' = 'default',
    'table-name' = 'dev_flink_clickhouse_sink'
);
```