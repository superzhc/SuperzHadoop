package com.github.superzhc.hadoop.flink.streaming.connector.mysql;

import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaFakerSource;
import com.github.superzhc.hadoop.flink.utils.FakerUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2021/10/26 13:41
 */
public class MysqlSinkDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Map<String, String> map = new HashMap<>();
        map.put("fields.name.expression", "#{Name.name}");
        map.put("fields.age.expression", "#{number.number_between '1','80'}");
        map.put("fields.sex.expression", "#{regexify '(男|女){1}'}");
        map.put("fields.create_time.expression", "#{date.past '5','SECONDS'}");
        JavaFakerSource source = new JavaFakerSource(map);

        DataStream<String> ds = env.addSource(source);


        final ObjectMapper mapper = new ObjectMapper();
        DataStream<JsonNode> ds2 = ds.map(new MapFunction<String, JsonNode>() {
            @Override
            public JsonNode map(String value) throws Exception {
                ObjectNode objectNode = (ObjectNode) mapper.readTree(value);
                objectNode.put("t1", FakerUtils.toLocalDateTime(objectNode.get("create_time").asText()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                objectNode.put("t2", FakerUtils.toTimestamp(objectNode.get("create_time").asText()));
                return objectNode;
            }
        });

        ds2.print();

        ds2.addSink(JdbcSink.sink(
                "insert into javafaker(name,age,sex,create_time) values(?,?,?,?)",
                new JdbcStatementBuilder<JsonNode>() {
                    @Override
                    public void accept(PreparedStatement preparedStatement, JsonNode jsonNode) throws SQLException {
                        preparedStatement.setString(1, jsonNode.get("name").asText());
                        preparedStatement.setInt(2, jsonNode.get("age").asInt());
                        preparedStatement.setString(3, jsonNode.get("sex").asText());
                        preparedStatement.setTimestamp(4, new Timestamp(FakerUtils.toTimestamp(jsonNode.get("create_time").asText())));
                    }
                },
                new JdbcExecutionOptions.Builder()
                        // 默认每5000条一批提交，测试为了方便查看，将此处设置为10条
                        .withBatchSize(10)
                        .build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withDriverName("com.mysql.cj.jdbc.Driver")
                        .withUrl("jdbc:mysql://127.0.0.1:3306/superz?zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2b8&useSSL=true")
                        .withUsername("root")
                        .withPassword("123456")
                        .build()
        ));

        env.execute("mysql sink");
    }
}
