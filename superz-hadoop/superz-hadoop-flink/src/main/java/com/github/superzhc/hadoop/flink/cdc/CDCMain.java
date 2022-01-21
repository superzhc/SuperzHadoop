package com.github.superzhc.hadoop.flink.cdc;

import com.alibaba.ververica.cdc.connectors.mysql.MySQLSource;
import com.alibaba.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.alibaba.ververica.cdc.debezium.DebeziumSourceFunction;
import com.alibaba.ververica.cdc.debezium.StringDebeziumDeserializationSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Flink Mysql CDC
 * <p>
 * CDC 是 Change Data Capture(变更数据获取)的简称。核心思想是，监测并捕获数据库的变动（包括数据或数据表的插入、更新以及删除等），将这些变更按发生的顺序完整记录下来，写入到消息中间件中以供其他服务进行订阅及消费。
 *
 * @author superz
 * @create 2022/1/21 13:35
 */
public class CDCMain {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DebeziumSourceFunction<String> sourceFunction = MySQLSource
                .<String>builder()
                .hostname("127.0.0.1")
                .port(13306)
                .username("root")
                .password("123456")
                .databaseList("data_warehouse")
                //.tableList()
                /**
                 * 启动的选项，目前支持五种模式：
                 * StartupOptions.initial()：【默认值】该模式是两步操作结合，先扫描监控的表；扫描完成后，对最新的binlog进行数据监控
                 * StartupOptions.earliest()：从最早的数据开始读取，注意该模式下，数据量会非常大
                 * StartupOptions.latest()：从最新的数据开始读取
                 * StartupOptions.specificOffset(String specificOffsetFile, int specificOffsetPos)：指定一个读取的文件信息，自己指定个任意个位置
                 * StartupOptions.timestamp(long startupTimestampMillis)：从特定的时间戳开始读取数据，因为binlog是按照时间戳来写数据的
                 */
                .startupOptions(StartupOptions.latest())
                .deserializer(new StringDebeziumDeserializationSchema())
                .build();

        env.addSource(sourceFunction).print().setParallelism(1);

        env.execute("flink mysql cdc");
    }
}
