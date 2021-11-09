package com.github.superzhc.hadoop.flink.table;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author superz
 * @create 2021/11/5 17:28
 */
public class DataStream2Table {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put("fields.name.expression", FakerUtils.Expression.name());
        fakerConfigs.put("fields.age.expression", FakerUtils.Expression.age(1, 80));
        fakerConfigs.put("fields.date.expression", FakerUtils.Expression.pastDate(5));

        final ObjectMapper mapper = new ObjectMapper();
        DataStream<Tuple3<String, Integer, Date>> ds = env.addSource(new JavaFakerSource(fakerConfigs)).map(new MapFunction<String, Tuple3<String, Integer, Date>>() {
            @Override
            public Tuple3<String, Integer, Date> map(String value) throws Exception {
                JsonNode node = mapper.readTree(value);
                return Tuple3.of(node.get("name").asText(), node.get("age").asInt(), FakerUtils.toDate(node.get("date").asText()));
            }
        });

        tEnv.createTemporaryView("faker", ds
                , $("name"), $("age"), $("date") /* 自定义字段 */
                , $("process_time").proctime() /* 声明 process time */
        );
        String sql = "select * from faker";
        Table table = tEnv.sqlQuery(sql);
        table.execute().print();
        env.execute("datastream to table");
    }
}
