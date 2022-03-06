package com.github.superzhc.hadoop.flink.streaming.connector.customsource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.superzhc.common.http.HttpRequest;
import okhttp3.*;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Http Source
 *
 * @author superz
 * @create 2022/2/21 10:52
 */
public class JavaHttpSource extends RichSourceFunction<String> {
    private static final Logger log = LoggerFactory.getLogger(JavaHttpSource.class);

    public static final String CONNECT_TIMEOUT = "connect.timeout";
    public static final String READ_TIMEOUT = "read.timeout";
    public static final String WRITE_TIMEOUT = "write.timeout";
    private static final String HTTP_JSON = "application/json; charset=utf-8";

    public static final String URL = "url";
    public static final String METHOD = "method";
    public static final String HEADERS = "headers";
    public static final String DATA = "data";

    public static final String PERIOD = "period";

    private volatile boolean cancelled = false;

    private Map<String, Object> configs;
    private OkHttpClient okHttpClient = null;
    private Request request = null;

    public JavaHttpSource(Map<String, Object> configs) {
        this.configs = configs;
    }

//    public JavaHttpSource(String url, String method) {
//        this(url, method, null, null);
//    }
//
//    public JavaHttpSource(String url, String method, Object data) {
//        this(url, method, null, data);
//    }
//
//    public JavaHttpSource(String url, String method, Map<String, String> headers, Object data) {
//        this(url, method, headers, data, null);
//    }

    public JavaHttpSource(String url, String method, Map<String, String> headers, Object data, Map<String, Object> configs) {
        if (null != configs) {
            this.configs = configs;
        } else {
            this.configs = new HashMap<>();
        }

        this.configs.put(URL, url);
        this.configs.put(METHOD, method);
        if (null != headers) {
            this.configs.put(HEADERS, headers);
        }
        if (null != data) {
            this.configs.put(DATA, data);
        }
    }

    public static JavaHttpSource get(String url) {
        return get(url, null);
    }

    public static JavaHttpSource get(String url, Map<String, String> headers) {
        return new JavaHttpSource(url, "GET", headers, null, null);
    }

    public static JavaHttpSource post(String url, Map<String, Object> form) {
        return post(url, null, form);
    }

    public static JavaHttpSource post(String url, Map<String, String> headers, Map<String, Object> form) {
        return new JavaHttpSource(url, "POST", headers, form, null);
    }

    public static JavaHttpSource post(String url, String json) {
        return post(url, null, json);
    }

    public static JavaHttpSource post(String url, Map<String, String> headers, String json) {
        return new JavaHttpSource(url, "POST", headers, json, null);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout((int) configs.getOrDefault(CONNECT_TIMEOUT, 120), TimeUnit.SECONDS);
        clientBuilder.readTimeout((int) configs.getOrDefault(READ_TIMEOUT, 120), TimeUnit.SECONDS);
        clientBuilder.writeTimeout((int) configs.getOrDefault(WRITE_TIMEOUT, 120), TimeUnit.SECONDS);
        okHttpClient = clientBuilder.build();

        RequestBody body = null;
        if (this.configs.containsKey(DATA) && null != this.configs.get(DATA)) {
            if (this.configs.get(DATA) instanceof String) {
                body = RequestBody.create((String) this.configs.get(DATA), MediaType.parse(HTTP_JSON));
            } else if (this.configs.get(DATA) instanceof Map) {
                Map<String, Object> d = (Map<String, Object>) this.configs.get(DATA);
                // 构建Form参数
                FormBody.Builder formBuilder = new FormBody.Builder();
                for (Map.Entry<String, Object> param : d.entrySet()) {
                    formBuilder.add(param.getKey(), String.valueOf(param.getValue()));
                }
                body = formBuilder.build();
            }
        }

        Request.Builder builder = new Request.Builder();
        builder.url((String) this.configs.get(URL));
        Map<String, String> headers = (Map<String, String>) this.configs.get(HEADERS);
        if (null != headers && 0 < headers.size()) {
            headers.forEach((String key, String value) -> builder.addHeader(key, value));
        }

        request = builder.method((String) this.configs.getOrDefault(METHOD, "GET"), body).build();
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while (!cancelled) {
            log.debug(request.toString());
            try (Response response = okHttpClient.newCall(request).execute()) {
                log.debug(response.toString());
                if (!response.isSuccessful()) {
                    // 不采集异常数据
                    //ctx.collect(response.message());
                } else {
                    ctx.collect(response.body().string());
                }
            }

            Thread.sleep(1000 * ((int) this.configs.getOrDefault(PERIOD, 60)));
        }
    }

    @Override
    public void cancel() {
        cancelled = false;
    }

    public static void main(String[] args) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<String> ds = null/*env.fromCollection(Collections.singletonList(""))*/;

//        // 获取所有类型[error:过多的类型会生成source报错Insufficient number of network buffers]
//        String moFishAllTypeUrl = "https://api.tophub.fun/GetAllType";
//        String allTypes = HttpRequest.get(moFishAllTypeUrl).body();
//        JsonNode allTypeRNode = mapper.readTree(allTypes);
//        JsonNode types = allTypeRNode.get("Data").get("全部");
//        for (JsonNode type : types) {
//            String id = type.get("id").asText();
//            String name = type.get("name").asText();
        Map<String, String> idAndSources = new HashMap<>();
        idAndSources.put("1", "知乎热榜");
        idAndSources.put("142", "acFun热榜");
        idAndSources.put("1066", "淘宝综合榜");
        idAndSources.put("151", "Zaker热榜");
        idAndSources.put("110", "抽屉热榜");
        idAndSources.put("1029", "羊毛绒榜");
        idAndSources.put("85", "Github热榜");
        idAndSources.put("11", "微信热榜");
        idAndSources.put("56", "贴吧热榜");
        idAndSources.put("58", "微博热搜");
//        idAndSources.put("","");
        for (Map.Entry<String, String> idAndSource : idAndSources.entrySet()) {
            String id = idAndSource.getKey();
            String name = idAndSource.getValue();
            DataStream<String> typeDs = env.addSource(JavaHttpSource.get(String.format("https://api.tophub.fun/v2/GetAllInfoGzip?id=%s&page=0&type=pc", id)));
            typeDs = typeDs.flatMap(new FlatMapFunction<String, String>() {
                @Override
                public void flatMap(String s, Collector<String> collector) throws Exception {
                    JsonNode node = mapper.readTree(s);
                    JsonNode datas = node.get("Data").get("data");
                    for (JsonNode data : datas) {
                        ObjectNode data2 = (ObjectNode) data;
                        data2.put("source", name);
                        // id 为空的不进行采集
                        if (null != data2.get("id")) {
                            collector.collect(mapper.writeValueAsString(data2));
                        }
                    }
                }
            });

            if (ds != null) {
                ds = ds.union(typeDs);
            } else {
                ds = typeDs;
            }
        }

        ds.addSink(JdbcSink.sink(
                "INSERT INTO mo_fish(id,title,create_time,type_name,url,approval_num,comment_num,hot_desc,description,img_url,is_rss,is_agree,source_name) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)" +
                        " ON DUPLICATE KEY UPDATE id=?,title=?,create_time=?,type_name=?,url=?,approval_num=?,comment_num=?,hot_desc=?,description=?,img_url=?,is_rss=?,is_agree=?,source_name=?"
                ,
                new JdbcStatementBuilder<String>() {
                    @Override
                    public void accept(PreparedStatement preparedStatement, String data) throws SQLException {
                        try {
                            JsonNode item = mapper.readTree(data);
                            preparedStatement.setInt(1, item.get("id").asInt());
                            preparedStatement.setString(2, null == item.get("Title") ? null : item.get("Title").asText());
                            preparedStatement.setTimestamp(3, new Timestamp(null == item.get("CreateTime") ? System.currentTimeMillis() : item.get("CreateTime").asLong() * 1000));
                            preparedStatement.setString(4, null == item.get("TypeName") ? null : item.get("TypeName").asText());
                            preparedStatement.setString(5, null == item.get("Url") ? null : item.get("Url").asText());
                            preparedStatement.setInt(6, null == item.get("approvalNum") ? null : item.get("approvalNum").asInt());
                            preparedStatement.setInt(7, null == item.get("commentNum") ? null : item.get("commentNum").asInt());
                            preparedStatement.setString(8, null == item.get("hotDesc") ? null : item.get("hotDesc").asText());
                            preparedStatement.setString(9, null == item.get("Desc") ? null : item.get("Desc").asText());
                            preparedStatement.setString(10, null == item.get("imgUrl") ? null : item.get("imgUrl").asText());
                            preparedStatement.setString(11, null == item.get("isRss") ? null : item.get("isRss").asText());
                            preparedStatement.setInt(12, null == item.get("is_agree") ? null : item.get("is_agree").asInt());
                            preparedStatement.setString(13, item.get("source").asText());
                            // upsert 需要重复设置一遍数据
                            preparedStatement.setInt(14, item.get("id").asInt());
                            preparedStatement.setString(15, null == item.get("Title") ? null : item.get("Title").asText());
                            preparedStatement.setTimestamp(16, new Timestamp(null == item.get("CreateTime") ? System.currentTimeMillis() : item.get("CreateTime").asLong() * 1000));
                            preparedStatement.setString(17, null == item.get("TypeName") ? null : item.get("TypeName").asText());
                            preparedStatement.setString(18, null == item.get("Url") ? null : item.get("Url").asText());
                            preparedStatement.setInt(19, null == item.get("approvalNum") ? null : item.get("approvalNum").asInt());
                            preparedStatement.setInt(20, null == item.get("commentNum") ? null : item.get("commentNum").asInt());
                            preparedStatement.setString(21, null == item.get("hotDesc") ? null : item.get("hotDesc").asText());
                            preparedStatement.setString(22, null == item.get("Desc") ? null : item.get("Desc").asText());
                            preparedStatement.setString(23, null == item.get("imgUrl") ? null : item.get("imgUrl").asText());
                            preparedStatement.setString(24, null == item.get("isRss") ? null : item.get("isRss").asText());
                            preparedStatement.setInt(25, null == item.get("is_agree") ? null : item.get("is_agree").asInt());
                            preparedStatement.setString(26, item.get("source").asText());
                        } catch (Exception e) {
                            System.out.println("解析data异常：" + data);
                            return;
                        }
                    }
                },
                new JdbcExecutionOptions.Builder()
                        // 默认每5000条一批提交，测试为了方便查看，将此处设置为10条
                        .withBatchSize(100)
                        .build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withDriverName("com.mysql.cj.jdbc.Driver")
                        .withUrl("jdbc:mysql://localhost:3306/news_dw?useSSL=false&useUnicode=true&characterEncoding=utf-8")
                        .withUsername("root")
                        .withPassword("123456")
                        .build()
        ));

        env.execute("flink http source");
    }
}
