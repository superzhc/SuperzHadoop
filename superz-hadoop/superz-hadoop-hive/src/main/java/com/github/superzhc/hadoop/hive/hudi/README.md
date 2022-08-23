# Hive 集成 Hudi

> Hive 查询 Hudi 数据主要是在 hive 中建立外部表数据路径指向 hdfs 路径，同时 hudi 重写了inputformat 和 outpurtformat。

Hive 集成 Hudi 外表的结构如下：

```sql
CREATE EXTERNAL TABLE 
`test_partition`
(
`_hoodie_commit_time` string,
`_hoodie_commit_seqno` string,
`_hoodie_record_key` string,
`_hoodie_file_name` string,
`id` string,
`oid` string,
`name` string,
`dt` string,
`isdeleted` string,
`lastupdatedttm` string,
`rowkey` string
)
PARTITIONED BY (`_hoodie_partition_path` string)
ROW FORMAT SERDE 'org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe'
STORED AS INPUTFORMAT 'org.apache.hudi.hadoop.HoodieParquetInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat'
LOCATION 'hdfs://hj:9000/tmp/hudi'
TBLPROPERTIES ('transient_lastDdlTime'='1582111004')
```

> hive集成hudi方法：将hudi jar复制到hive lib下