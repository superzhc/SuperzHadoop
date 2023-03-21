package com.github.superzhc.hadoop.flink.connector;

/**
 * @author superz
 * @create 2023/3/22 1:56
 */
public class PrintSqlConnectorTest {
    public void sql(){
        String sql="CREATE TABLE print_table (\n" +
                " f0 INT,\n" +
                " f1 INT,\n" +
                " f2 STRING,\n" +
                " f3 DOUBLE\n" +
                ") WITH (\n" +
                " 'connector' = 'print'\n" +
                ")";
    }
}
