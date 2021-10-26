package com.github.superzhc.hadoop.flink.streaming.demo;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author superz
 * @create 2021/10/22 18:29
 */
public class JavaJsonDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<String> text = env.socketTextStream("127.0.0.1", 8421);

        // objectMapper对象是一个可重复使用的对象
        /* ObjectMapper类是Jackson库的主要类。它提供一些功能将转换成Java对象匹配JSON结构，反之亦然。 */
        final ObjectMapper mapper = new ObjectMapper();

        DataStream<JsonNode> ds = text.map(new MapFunction<String, JsonNode>() {
            @Override
            public JsonNode map(String value) throws Exception {
                JsonNode node = mapper.readValue(value, JsonNode.class);
                ((ObjectNode) node).put("editor", "superz");
                return node;
            }
        }).keyBy(new KeySelector<JsonNode, String>() {
            @Override
            public String getKey(JsonNode value) throws Exception {
                return value.get("name").asText();
            }
        });

        ds.print();

        env.execute("Java Json Demo");
    }
}
