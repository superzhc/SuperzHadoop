package com.github.superzhc.hadoop.flink.connector;

/**
 * @author superz
 * @create 2023/3/22 1:52
 */
@Deprecated
public class FileSystemSqlConnectorTest {
    public void sql(){
        String sql="CREATE TABLE MyUserTable (\n" +
                "  column_name1 INT,\n" +
                "  column_name2 STRING,\n" +
                "  ...\n" +
                "  part_name1 INT,\n" +
                "  part_name2 STRING\n" +
                ")" +
                " PARTITIONED BY (part_name1, part_name2)" +
                " WITH (\n" +
                "  'connector' = 'filesystem',           -- 必选：指定连接器类型\n" +
                "  'path' = 'file:///path/to/whatever',  -- 必选：指定路径\n" +
                "  'format' = '...',                     -- 必选：文件系统连接器指定 format\n" +
                "                                        -- 有关更多详情，请参考 Table Formats\n" +
                "  'partition.default-name' = '...',     -- 可选：默认的分区名，动态分区模式下分区字段值是 null 或空字符串\n" +
                "\n" +
                "  -- 可选：该属性开启了在 sink 阶段通过动态分区字段来 shuffle 数据，该功能可以大大减少文件系统 sink 的文件数，但是可能会导致数据倾斜，默认值是 false\n" +
                "  'sink.shuffle-by-partition.enable' = '...'," +
                "  ..." +
                " )";
    }
}
