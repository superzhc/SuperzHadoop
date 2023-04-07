package com.github.superzhc.hadoop.flink.connector;

/**
 * @author superz
 * @create 2023/3/22 1:45
 */
@Deprecated
public class JDBCSqlConnectorTest {
    public void jdbc(){
        String sql="CREATE TABLE MyUserTable (" +
                "  id BIGINT," +
                "  name STRING," +
                "  age INT," +
                "  status BOOLEAN," +
                "  PRIMARY KEY (id) NOT ENFORCED" +
                ") WITH (" +
                "   'connector' = 'jdbc'," +
                "   'url' = 'jdbc:mysql://localhost:3306/mydatabase'," +
                "   'table-name' = 'users'" +
                ")";
    }
}
