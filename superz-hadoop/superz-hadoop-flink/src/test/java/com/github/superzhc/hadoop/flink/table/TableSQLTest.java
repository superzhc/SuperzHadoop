package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/3/16 11:26
 **/
public class TableSQLTest {
    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    @Before
    public void setUp() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        tEnv = StreamTableEnvironment.create(env);
    }

    public void testSetConfig() {
        tEnv.getConfig().getConfiguration().setBoolean("table.dynamic-table-options.enabled", true);
    }

    /**
     * SELECT 语句可使用 sqlQuery
     */
    public void testSqlQuery() {
        Table table = tEnv.sqlQuery("select * from t1");
    }

    /**
     * TableEnvironment.executeSql()。该方法用于执行给定的语句，一旦调用该方法，就会立即翻译 sql 查询
     */
    public void testExecuteSql() {
        TableResult tableResult = tEnv.executeSql("");
    }
}
