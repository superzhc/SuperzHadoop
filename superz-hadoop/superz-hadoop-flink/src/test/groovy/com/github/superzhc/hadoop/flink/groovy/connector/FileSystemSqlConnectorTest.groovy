package com.github.superzhc.hadoop.flink.groovy.connector

class FileSystemSqlConnectorTest {
    void ddl(){
        String sql="""
CREATE TABLE MyUserTable (
    column_name1 INT,
    column_name2 STRING,
    ...
    part_name1 INT,
    part_name2 STRING
)
PARTITIONED BY (part_name1, part_name2)
WITH (
    'connector' = 'filesystem',           -- 必选：指定连接器类型
    'path' = 'file:///path/to/whatever',  -- 必选：指定路径
    'format' = '...',                     -- 必选：文件系统连接器指定 format
                                          -- 有关更多详情，请参考 Table Formats
    'partition.default-name' = '...',     -- 可选：默认的分区名，动态分区模式下分区字段值是 null 或空字符串
    -- 可选：该属性开启了在 sink 阶段通过动态分区字段来 shuffle 数据，该功能可以大大减少文件系统 sink 的文件数，但是可能会导致数据倾斜，默认值是 false
    'sink.shuffle-by-partition.enable' = '...',
    ...
)
"""
    }
}
