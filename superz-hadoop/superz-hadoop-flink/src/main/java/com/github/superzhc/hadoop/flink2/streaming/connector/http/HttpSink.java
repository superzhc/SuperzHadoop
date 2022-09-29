package com.github.superzhc.hadoop.flink2.streaming.connector.http;

import com.github.superzhc.common.faker.utils.ExpressionUtils;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.hadoop.flink2.streaming.connector.generator.FakerSource;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.flink.util.Preconditions.checkArgument;

/**
 * @author superz
 * @create 2022/9/24 11:08
 **/
public class HttpSink<T> extends RichSinkFunction<T> {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(HttpSink.class);

    private HttpConfig config;

    public HttpSink(HttpConfig config) {
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
        log.debug("request data:{}", value);
        HttpRequest request = new HttpRequest(config.getUrl(), config.getMethod());
        request.headers(config.getHeaders());
        if (null != value) {
            request.send(value.toString());
        }
        int code = request.code();
        String result = request.body();
        if (code < 400) {
            log.debug(result);
        } else {
            log.error(result);
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
        ds.addSink(new HttpSink<>(httpConfig));

        env.execute("flink http sink");
    }
}