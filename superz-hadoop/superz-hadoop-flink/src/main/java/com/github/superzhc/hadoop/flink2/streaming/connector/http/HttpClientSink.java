package com.github.superzhc.hadoop.flink2.streaming.connector.http;

import com.github.superzhc.common.faker.utils.ExpressionUtils;
import com.github.superzhc.hadoop.flink2.streaming.connector.generator.FakerSource;
import com.github.superzhc.hadoop.flink2.streaming.connector.http.util.HttpDeleteWithEntity;
import com.github.superzhc.hadoop.flink2.streaming.connector.http.util.HttpGetWithEntity;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.apache.flink.util.Preconditions.checkArgument;

/**
 * @author superz
 * @create 2022/9/27 9:59
 **/
public class HttpClientSink<T> extends RichSinkFunction<T> {
    private static final Logger log = LoggerFactory.getLogger(HttpClientSink.class);

    private HttpConfig config;

    public HttpClientSink(HttpConfig config) {
        this.config = config;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        checkArgument(null != config.getUrl() && !"".equals(config.getUrl().trim()), "请求地址不能为空");
        checkArgument("GET".equalsIgnoreCase(config.getMethod())
                        || "POST".equalsIgnoreCase(config.getMethod())
                        || "DELETE".equalsIgnoreCase(config.getMethod())
                        || "PUT".equalsIgnoreCase(config.getMethod())
                        || "HEAD".equalsIgnoreCase(config.getMethod())
                        || "OPTIONS".equalsIgnoreCase(config.getMethod())
                        || "TRACE".equalsIgnoreCase(config.getMethod())
                , "Method Support:GET、POST、PUT、DELETE、HEAD、OPTIONS、TRACE");
    }

    @Override
    public void invoke(T value, Context context) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            String url = config.getUrl();

            String method = config.getMethod().toUpperCase();
            /*因HttpUriRequest不可序列化，无法公用*/
            HttpEntityEnclosingRequestBase request;
            switch (method) {
                case "POST":
                    request = new HttpPost(url);
                    break;
                case "PUT":
                    request = new HttpPut(url);
                    break;
                case "DELETE":
                    request = new HttpDeleteWithEntity(url);
                    break;
                case "GET":
                default:
                    request = new HttpGetWithEntity(url);
                    break;
            }
            log.debug("请求地址：[{}] {}", method, url);

            if (null != config.getHeaders() && !config.getHeaders().isEmpty()) {
                for (Map.Entry<String, String> header : config.getHeaders().entrySet()) {
                    request.setHeader(header.getKey(), header.getValue());
                }
            }

            if (null != value) {
                log.debug("请求体：{}", value);
                StringEntity entity = new StringEntity(value.toString(), Charset.forName("UTF-8"));
                request.setEntity(entity);
            }


            // 创建 httpResponse 对象
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int code = response.getStatusLine().getStatusCode();
                if (code < 400) {
                    HttpEntity entity = response.getEntity();
                    if (null == entity) {
                        log.debug("请求成功，响应内容为空");
                    } else {
                        log.debug("请求成功，响应内容：{}", EntityUtils.toString(entity, "UTF-8"));
                    }
                } else {
                    log.error("请求失败，响应码：{}", code);
                }
            } catch (Exception e) {
                log.error("请求异常！", e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Map<String, String> fakerConfigs = new HashMap<>();
        fakerConfigs.put("fields.name.expression", ExpressionUtils.NAME);
        fakerConfigs.put("fields.age.expression", ExpressionUtils.age(1, 80));
        fakerConfigs.put("fields.id_card.expression", ExpressionUtils.ID_CARD);
        fakerConfigs.put("fields.qq.expression", ExpressionUtils.QQ);
        fakerConfigs.put("fields.ip.expression", ExpressionUtils.IP);
        fakerConfigs.put("fields.plate_number.expression", ExpressionUtils.CAR_LICESE_PLATE);
        DataStream<String> ds = env.addSource(new FakerSource(fakerConfigs));

        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setUrl("http://localhost:7200/api/v1/flink/http/")
                .setMethod("POST")
                .setContentType("application/json;charset=UTF-8")
        ;
        ds.addSink(new HttpClientSink<>(httpConfig));

        env.execute("faker httpclient source");
    }
}
