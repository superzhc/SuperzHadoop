# Print

## Sink

```sql
CREATE TABLE flink_sql_print (
  `id` String,
    `device_id` String,
    `intvalue` INT,
    `longvalue` BIGINT,
    `stringvalue` String,
    `sign` Int
) WITH (
 'connector' = 'print'
);
```