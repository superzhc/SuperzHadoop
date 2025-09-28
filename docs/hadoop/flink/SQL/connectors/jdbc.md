# JDBC

## Connector

```xml
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-connector-jdbc</artifactId>
    <version>3.2.0-1.18</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.2.2</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.27</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc11</artifactId>
    <version>23.3.0.23.09</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>12.4.1.jre11</version>
    <scope>provided</scope>
</dependency>
```

> **注意**：Flink SQL 的 JDBC 连接器不支持 Clickhouse 驱动

## Source

### Oracle

```sql
CREATE TABLE t_flink_jdbc_oracle (
  `ID_` STRING, 
  `REV_` NUMERIC(19,0), 
  `TYPE_` STRING, 
  `NAME_` STRING, 
  `EXECUTION_ID_` STRING, 
  `PROC_INST_ID_` STRING, 
  `TASK_ID_` STRING, 
  `BYTEARRAY_ID_` STRING, 
  `DOUBLE_` NUMERIC(19,10), 
  `LONG_` NUMERIC(19,0), 
  `TEXT_` STRING, 
  `TEXT2_` STRING, 
  `AC` STRING
) WITH (
   'connector' = 'jdbc',
   'driver' = 'oracle.jdbc.driver.OracleDriver',
   'url' = 'jdbc:oracle:thin:@127.0.0.1:1521:helowin',
   'username' = 'demo',
   'password' = 'password',
   'table-name' = 'ACT_RU_VARIABLE'
);
```

## Sink