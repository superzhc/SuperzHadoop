# Redis

[Redis连接器](https://github.com/jeff-zou/flink-connector-redis)

```xml
<dependency>
    <groupId>io.github.jeff-zou</groupId>
    <artifactId>flink-connector-redis</artifactId>
    <version>1.4.3</version>
    <classifier>jar-with-dependencies</classifier>
    <scope>provided</scope>
</dependency>
```

<!--
-- 创建redis表示例
CREATE TABLE redis_table (
 name varchar,
 age int
)WITH(
 'connector'='redis',
 'host'='10.100.2.254',
 'port'='6379',
 'password'='123456',
 'database'='9',
 'redis-mode'='single',
 'command'='set'
);
-- 写入  
insert into redis_table select * from (values('dev_test', 30));

-- 查询  
--insert into redis_table select name,age + 1 from redis_table /*+ options('scan.key'='test') */
  
--create table gen_table (age int , level int, proctime as procTime()) with ('connector'='datagen','fields.age.kind' = 'sequence','fields.age.start' = '2','fields.age.end' = '2','fields.level.kind' = 'sequence','fields.level.start' = '10','fields.level.end' = '10'); 

-- 关联查询 
--insert into redis_table select 'test', j.age + 10 from gen_table s left join redis_table  for system_time as of proctime as j on j.name = 'test'
-->