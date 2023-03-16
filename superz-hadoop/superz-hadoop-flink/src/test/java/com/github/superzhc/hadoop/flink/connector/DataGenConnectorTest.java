package com.github.superzhc.hadoop.flink.connector;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Before;
import org.junit.Test;

/**
 * @author superz
 * @create 2023/3/16 10:09
 **/
public class DataGenConnectorTest {
    StreamExecutionEnvironment env;
    TableEnvironment tEnv;

    @Before
    public void setUp() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        tEnv = StreamTableEnvironment.create(env);
    }

    /**
     * 表的有界性：当表中字段的数据全部生成完成后，source 就结束了。 因此，表的有界性取决于字段的有界性。
     * <p>
     * 每个列，都有两种生成数据的方法：
     * 1. 随机生成器是默认的生成器，您可以指定随机生成的最大和最小值。char、varchar、string （类型）可以指定长度。它是无界的生成器。
     * 2. 序列生成器，您可以指定序列的起始和结束值。它是有界的生成器，当序列数字达到结束值，读取结束。
     */


    @Test
    public void createBounded() {
        String sql = "CREATE TABLE datagen_bounded (\n" +
                " f_sequence INT,\n" +
                " f_random INT,\n" +
                " f_random_str STRING,\n" +
                " ts AS localtimestamp,\n" +
                " WATERMARK FOR ts AS ts\n" +
                ") WITH (\n" +
                " 'connector' = 'datagen',\n" +
                " 'rows-per-second'='5',\n" +
                // 字段为序列生成器，因此此表为有界表
                " 'fields.f_sequence.kind'='sequence',\n" +
                " 'fields.f_sequence.start'='1',\n" +
                " 'fields.f_sequence.end'='1000',\n" +
                "" +
                " 'fields.f_random.min'='1',\n" +
                " 'fields.f_random.max'='1000',\n" +
                " 'fields.f_random_str.length'='10'\n" +
                ")";
        tEnv.executeSql(sql);
        tEnv.executeSql("SELECT * FROM datagen_bounded").print();
    }

    @Test
    public void createUnbounded(){
        String sql = "CREATE TABLE datagen_unbounded (\n" +
                " f_random INT,\n" +
                " f_random_str STRING,\n" +
                " ts AS localtimestamp,\n" +
                " WATERMARK FOR ts AS ts\n" +
                ") WITH (\n" +
                " 'connector' = 'datagen',\n" +
                " 'rows-per-second'='5',\n" +
                " 'fields.f_random.min'='1',\n" +
                " 'fields.f_random.max'='1000',\n" +
                " 'fields.f_random_str.length'='10'\n" +
                ")";
        tEnv.executeSql(sql);
        tEnv.executeSql("SELECT * FROM datagen_unbounded").print();
    }
}
