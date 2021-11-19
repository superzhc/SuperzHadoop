package com.github.superzhc.hadoop.flink.streaming.demo.stock;

import com.github.superzhc.hadoop.flink.utils.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author superz
 * @create 2021/11/18 11:30
 */
public class SinaSource extends RichParallelSourceFunction<String> {
    private static final String API = "http://hq.sinajs.cn/rn=%d&list=%s";
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();

    private volatile boolean cancelled = false;
    private String[] stocks;
    private transient ObjectMapper mapper;

    public SinaSource(String[] stocks) {
        this.stocks = stocks;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        mapper = new ObjectMapper();
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {

        String[] subStocks = stocksForSubTask();
        if (subStocks == null || subStocks.length == 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String stock : subStocks) {
            sb.append(",").append(stockType(stock)).append(stock);
        }

        while (!cancelled) {
            // 获取当前时间的时间戳
            Long currentTimestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            String url = String.format(API, currentTimestamp, sb.substring(1));
            Request request = new Request.Builder().url(url).build();
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    String[] ss = body.split(";");
                    for (String s : ss) {
                        if (null == s || s.trim().length() == 0) {
                            continue;
                        }

                        String s2 = StringUtils.replaceBlank(s);
                        String[] ss2 = s2.split("=");
                        String code = ss2[0].substring("var hq_str_".length() + 2);

                        String infos = ss2[1].substring(1, ss2[1].length() - 1);
                        String[] infoArr = infos.split(",");

                        ObjectNode node = mapper.createObjectNode();
                        node.put("code", code);
                        node.put("name", infoArr[0]);
                        node.put("opening", Double.valueOf(infoArr[1]));
                        node.put("close", Double.valueOf(infoArr[2]));
                        node.put("now", Double.valueOf(infoArr[3]));
                        node.put("high", Double.valueOf(infoArr[4]));
                        node.put("low", Double.valueOf(infoArr[5]));
                        node.put("x1", Double.valueOf(infoArr[6]));
                        node.put("x2", Double.valueOf(infoArr[7]));
                        // 成交量
                        node.put("VOL", BigDecimal.valueOf(Double.valueOf(infoArr[8])));
                        // 成交额
                        node.put("AMO", BigDecimal.valueOf(Double.valueOf(infoArr[9])));
                        node.put("x5", Double.valueOf(infoArr[10]));
                        node.put("x6", Double.valueOf(infoArr[11]));
                        node.put("x7", Double.valueOf(infoArr[12]));
                        node.put("x8", Double.valueOf(infoArr[13]));
                        node.put("x9", Double.valueOf(infoArr[14]));
                        node.put("x10", Double.valueOf(infoArr[15]));
                        node.put("x11", Double.valueOf(infoArr[16]));
                        node.put("x12", Double.valueOf(infoArr[17]));
                        node.put("x13", Double.valueOf(infoArr[18]));
                        node.put("x14", Double.valueOf(infoArr[19]));
                        node.put("x15", Double.valueOf(infoArr[20]));
                        node.put("x16", Double.valueOf(infoArr[21]));
                        node.put("x17", Double.valueOf(infoArr[22]));
                        node.put("x18", Double.valueOf(infoArr[23]));
                        node.put("x19", Double.valueOf(infoArr[24]));
                        node.put("x20", Double.valueOf(infoArr[25]));
                        node.put("x21", Double.valueOf(infoArr[26]));
                        node.put("x22", Double.valueOf(infoArr[27]));
                        node.put("x23", Double.valueOf(infoArr[28]));
                        node.put("x24", Double.valueOf(infoArr[29]));
                        // 转换成时间
                        node.put("datetime", infoArr[30] + " " + infoArr[31]);
                        node.put("x27", infoArr[32]);
                        // node.put("x28", infoArr[33]);

                        ctx.collect(mapper.writeValueAsString(node));
                    }
                } else {
                    ctx.collect(response.message());
                }
            }

            Thread.sleep(1000 * 3);
        }
    }

    private String[] stocksForSubTask() {
        int numSubtasks = getRuntimeContext().getNumberOfParallelSubtasks();
        int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();

        List<String> lst = new ArrayList<>();
        for (int i = 0, len = stocks.length; i < len; i++) {
            if (i % numSubtasks == indexOfThisSubtask - 1) {
                lst.add(stocks[i]);
            }
        }

        if (lst.size() == 0) {
            return null;
        }

        String[] subStocks = new String[lst.size()];
        return lst.toArray(subStocks);
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    private static String stockType(String stockCode) {
        if (stockCode.startsWith("sh") || stockCode.startsWith("sz") || stockCode.startsWith("zz")) {
            return stockCode.substring(0, 2);
        } else {
            String[] shHeads = new String[]{"50", "51", "60", "90", "110", "113", "132", "204", "5", "6", "9", "7"};
            for (String shHead : shHeads) {
                if (stockCode.startsWith(shHead)) {
                    return "sh";
                }
            }
            return "sz";
        }
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setParallelism(1);

        env.addSource(new SinaSource(new String[]{"688232", "600392"})).print();

        env.execute("stock info by sina");
    }
}
