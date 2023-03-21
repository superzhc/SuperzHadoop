package com.github.superzhc.hadoop.flink.connector;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Before;

/**
 * @author superz
 * @create 2023/3/22 1:40
 */
public class JDBCConnectorTest {
    StreamExecutionEnvironment env;

    @Before
    public void setUp() throws Exception {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    public void sink() throws Exception {
        JdbcSink.sink(
                "insert into books (id, title, author, price, qty) values (?,?,?,?,?)",
                (ps, t) -> {
                    // ps.setInt(1, t.id);
                    // ps.setString(2, t.title);
                    // ps.setString(3, t.author);
                    // ps.setDouble(4, t.price);
                    // ps.setInt(5, t.qty);
                },
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("")
                        .withDriverName("")
                        .build())
        ;
    }
}
