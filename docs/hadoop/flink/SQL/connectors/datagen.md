# DataGen

## Source

```sql
CREATE TABLE flink_sql_datagen_20250304 (
 f_sequence INT,
 age INT,
 f_random_str STRING,
 ts AS localtimestamp,
 WATERMARK FOR ts AS ts
) WITH (
 'connector' = 'datagen',
 'rows-per-second'='5',

 'fields.f_sequence.kind'='sequence',
 'fields.f_sequence.start'='1',
 'fields.f_sequence.end'='1000',

 'fields.age.min'='1',
 'fields.age.max'='120',

 'fields.f_random_str.length'='10'
)
```