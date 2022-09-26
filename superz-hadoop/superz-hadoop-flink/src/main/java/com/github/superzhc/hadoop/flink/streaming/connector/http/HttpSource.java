package com.github.superzhc.hadoop.flink.streaming.connector.http;

import com.github.superzhc.common.http.HttpRequest;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.apache.flink.util.Preconditions.checkArgument;

/**
 * @author superz
 * @create 2022/9/26 16:22
 **/
public class HttpSource extends RichSourceFunction<String> {
    private static final Logger log = LoggerFactory.getLogger(HttpSource.class);

    private static final int DEFAULT_REQUEST_RATE = 1000 * 60;

    private volatile boolean cancelled = false;
    private HttpConfig config;

    /* 请求频率 */
    private int requestRate;

    public HttpSource(HttpConfig config) {
        this(config, DEFAULT_REQUEST_RATE);
    }

    public HttpSource(HttpConfig config, int requestRate) {
        this.config = config;
        this.requestRate = requestRate;
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
    public void run(SourceContext<String> ctx) throws Exception {
        long nextRequestTime;
        while (!cancelled) {
            nextRequestTime = System.currentTimeMillis();

            HttpRequest request = new HttpRequest(config.getUrl(), config.getMethod());

            if (null != config.getHeaders() && !config.getHeaders().isEmpty()) {
                request.headers(config.getHeaders());
            }

            // 请求体
            Object requestBody = config.getBody();
            if (null != requestBody) {
                if (requestBody instanceof Map) {
                    Map<?, ?> body = (Map<?, ?>) requestBody;
                    String contentType = config.getContentType();
                    if (contentType.contains("application/json")) {
                        request.json(body);
                    } else {
                        request.form(body);
                    }
                } else if (requestBody instanceof byte[]) {
                    request.send((byte[]) requestBody);
                } else if (requestBody instanceof CharSequence) {
                    request.send((CharSequence) requestBody);
                } else {
                    request.send(String.valueOf(requestBody));
                }
            }

            String result = request.body();
            ctx.collect(result);

            nextRequestTime += requestRate;
            long waitMs = Math.max(0, nextRequestTime - System.currentTimeMillis());
            Thread.sleep(waitMs);
        }
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<String> ds = env.addSource(new HttpSource(
                new HttpConfig()
                        .setUrl(String.format("https://api.tophub.fun/v2/GetAllInfoGzip?id=%s&page=0&type=pc", "11"))
                        .setMethod("GET")
                        // .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36")
                        // .setCookie("MoFish=eeaf88bdb0f11edefda876032003c267")
                        // .setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        // .setAcceptEncoding("gzip")
        ));
        ds.print();

        env.execute("flink http source");
    }
}
