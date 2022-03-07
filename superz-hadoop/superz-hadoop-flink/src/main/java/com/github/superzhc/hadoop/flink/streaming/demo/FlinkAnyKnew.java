package com.github.superzhc.hadoop.flink.streaming.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.superzhc.hadoop.flink.streaming.connector.customsource.JavaHttpSource;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2022/2/23 15:51
 */
public class FlinkAnyKnew {
    public static void main(String[] args) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "123456";
        String sql = "INSERT INTO any_knew_hot_news(id, title, more, tag, sub_tag, create_time, update_time) VALUES(?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE id = ?, title = ?, more = ?, tag = ?, sub_tag = ?, update_time =?";

        String[] types = {"baidu", "weibo", "zhihu", "sina", "163", "360", "toutiao", "xueqiu", /*"investing",*/ "wallstreetcn"
                , "eastmoney", "caixin", "guokr", "36kr", "yc", "kepuchina", "cnbeta", "zol", "smzdm", "autohome", "jiemian"
                , "thepaper", "pearvideo", "bilibili", "mtime", "gamesky", "endata", "v2ex", "toutiaoio", "oschina"};


        DataStream<String> ds = null;
        for (String type : types) {
            String httpUrl = String.format("https://www.anyknew.com/api/v1/sites/%s", type);
            DataStream<String> typeDs = env.addSource(JavaHttpSource.get(httpUrl));
            typeDs = typeDs.flatMap(new FlatMapFunction<String, String>() {
                Double number(String str) {
                    if (null == str || str.trim().length() == 0) {
                        return 0.0;
                    }

                    str = str.trim().replace(",", "");

                    Double number = 0.0;
                    String unit = "";
                    if (str.endsWith("万")) {
                        int len = str.length();
                        unit = str.substring(len - 1);
                    }

                    String regex = "\\d+([.]\\d*)?";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(str);
                    if (matcher.find()) {
                        number = Double.parseDouble(matcher.group());
                    }

                    switch (unit) {
                        case "万":
                            return number * 10000;
                        default:
                            return number;
                    }
                }

                @Override
                public void flatMap(String data, Collector<String> collector) throws Exception {
                    if (null == data || data.trim().length() == 0) {
                        return;
                    }

                    JsonNode dataNode = mapper.readTree(data);
                    JsonNode site = dataNode.get("data").get("site");
                    String tag = site.get("attrs").get("cn").asText();
                    for (JsonNode sub : site.get("subs")) {
                        List<List<Object>> params = new ArrayList<>();

                        String subTag = sub.get("attrs").get("cn").asText();
                        for (JsonNode item : sub.get("items")) {
                            Integer iid = item.get("iid").asInt();
                            String title = item.get("title").asText();
                            String more = null == item.get("more") ? null : item.get("more").asText();
                            Long addDate = item.get("add_date").asLong();
                            Long currentDate = System.currentTimeMillis();

                            ObjectNode out = mapper.createObjectNode();
                            out.put("id", iid);
                            out.put("title", title);
                            out.put("more", number(more));
                            out.put("tag", tag);
                            out.put("subTag", subTag);
                            out.put("createTime", addDate * 1000);
                            out.put("updateTime", currentDate);
                            collector.collect(mapper.writeValueAsString(out));
                        }
                    }
                }
            });

            if (null == ds) {
                ds = typeDs;
            } else {
                ds = ds.union(typeDs);
            }
        }

        ds.addSink(JdbcSink.sink(
                sql,
                new JdbcStatementBuilder<String>() {
                    @Override
                    public void accept(PreparedStatement preparedStatement, String data) throws SQLException {
                        try {
                            JsonNode item = mapper.readTree(data);
                            preparedStatement.setInt(1, item.get("id").asInt());
                            preparedStatement.setString(2, item.get("title").asText());
                            preparedStatement.setDouble(3, item.get("more").asDouble());
                            preparedStatement.setString(4, item.get("tag").asText());
                            preparedStatement.setString(5, item.get("subTag").asText());
                            preparedStatement.setTimestamp(6, new Timestamp(item.get("createTime").asLong()));
                            preparedStatement.setTimestamp(7, new Timestamp(item.get("updateTime").asLong()));
                            preparedStatement.setInt(8, item.get("id").asInt());
                            preparedStatement.setString(9, item.get("title").asText());
                            preparedStatement.setDouble(10, item.get("more").asDouble());
                            preparedStatement.setString(11, item.get("tag").asText());
                            preparedStatement.setString(12, item.get("subTag").asText());
                            // preparedStatement.setTimestamp(13, new Timestamp(item.get("createTime").asLong()));
                            preparedStatement.setTimestamp(13, new Timestamp(item.get("updateTime").asLong()));
                        } catch (Exception e) {
                            System.out.println("解析data异常：" + data);
                            e.printStackTrace();
                            return;
                        }
                    }
                },
                new JdbcExecutionOptions.Builder()
                        // 默认每5000条一批提交，测试为了方便查看，将此处设置为10条
                        .withBatchSize(100)
                        .build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withDriverName(driver)
                        .withUrl(url)
                        .withUsername(username)
                        .withPassword(password)
                        .build()
        ));

        env.execute("flink http source");
    }
}
