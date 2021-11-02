package com.github.superzhc.hadoop.flink.streaming.demo.fund;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.concurrent.TimeUnit;

/**
 * @author superz
 * @create 2021/10/28 19:40
 */
public class FlinkFundSource extends RichSourceFunction<String> {
    private static final Logger log = LoggerFactory.getLogger(FlinkFundSource.class);

    private static final LocalTime time0900 = LocalTime.of(9, 0, 0);
    private static final LocalTime time1130 = LocalTime.of(11, 30, 0);
    private static final LocalTime time1300 = LocalTime.of(13, 0, 0);
    private static final LocalTime time1500 = LocalTime.of(15, 0, 0);

    private volatile boolean cancelled = false;

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();

    private String[] codes;
    private String[] urls;

    private transient ObjectMapper mapper;

    public FlinkFundSource(String[] codes) {
        this.codes = codes;
        this.urls = new String[codes.length];
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        this.mapper = new ObjectMapper();

        for (int i = 0, len = codes.length; i < len; i++) {
            String url = String.format("https://fundgz.1234567.com.cn/js/%s.js?rt=", codes[i]);
            urls[i] = url;
        }
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {

        Long nextReadTime = System.currentTimeMillis();
        while (!cancelled) {
            LocalDateTime now = LocalDateTime.now();

            for (int i = 0, len = urls.length; i < len; i++) {
                /* 添加时间戳，保证数据获取最新的 */
                String url = urls[i] + nextReadTime;
                Request request = new Request.Builder().url(url).build();
                try (Response response = okHttpClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        String json = body.substring(8, body.length() - 2);

                        // 对字段进行重命名，原首字母简写比较费解
                        ObjectNode node = (ObjectNode) mapper.readTree(json);
                        // 净值日期
                        node.put("net_worth_date", node.get("jzrq").asText());
                        node.remove("jzrq");
                        // 当日净值
                        node.put("net_worth", node.get("dwjz").asText());
                        node.remove("dwjz");
                        // 估算净值
                        node.put("valuation", node.get("gsz").asText());
                        node.remove("gsz");
                        // 估算涨幅百分比，注意百分比单位
                        node.put("valuation_per", node.get("gszzl").asText());
                        node.remove("gszzl");
                        // 估值时间
                        node.put("valuation_datetime", node.get("gztime").asText());
                        node.remove("gztime");


                        ctx.collect(mapper.writeValueAsString(node));
                    } else {
                        ctx.collect(response.message());
                    }
                }
            }

            LocalDate nowDate = now.toLocalDate();
            if (now.getDayOfWeek() == DayOfWeek.SATURDAY) {
                log.info("Stop trading[weekend]");
                Duration duration = Duration.between(now, nowDate.plusDays(2).atTime(time0900));
                nextReadTime += duration.toMillis();
            } else if (now.getDayOfWeek() == DayOfWeek.SUNDAY) {
                log.info("Stop trading[weekend]");
                Duration duration = Duration.between(now, nowDate.plusDays(1).atTime(time0900));
                nextReadTime += duration.toMillis();
            } else if (now.isBefore(nowDate.atTime(time0900))) {
                log.info("Have not yet started trading");
                Duration duration = Duration.between(now, nowDate.atTime(time0900));
                nextReadTime += duration.toMillis();
            } else if (now.isAfter(nowDate.atTime(time1130)) && now.isBefore(nowDate.atTime(time1300))) {
                log.info("Pause trading");
                Duration duration = Duration.between(now, nowDate.atTime(time1300));
                nextReadTime += duration.toMillis();
            } else if (now.isAfter(nowDate.atTime(time1500))) {
                log.info("Stop trading");
                Duration duration = Duration.between(now, nowDate.plusDays(now.getDayOfWeek() == DayOfWeek.FRIDAY ? 3 : 1).atTime(time0900));
                nextReadTime += duration.toMillis();
            } else {
                // 一分钟去请求一次
                nextReadTime += 1000 * 60;
            }

            Long waitMs = Math.max(0, nextReadTime - System.currentTimeMillis());
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

        DataStream<String> ds = env.addSource(new FlinkFundSource(new String[]{"164402", "000478", "001594"}));

        ds.print();

        env.execute("flink fund");
    }
}
