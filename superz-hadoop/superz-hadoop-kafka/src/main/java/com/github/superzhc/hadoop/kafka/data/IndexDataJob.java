package com.github.superzhc.hadoop.kafka.data;

import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.data.index.CNIndex;
import com.github.superzhc.hadoop.kafka.MyProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/11/20 23:11
 */
public class IndexDataJob {
    private static final Logger log = LoggerFactory.getLogger(IndexDataJob.class);

    private static final String BROKERS = "127.0.0.1:19092";
    private static final String TOPIC = "index_cnindex_ohlc";

    public static void main(String[] args) throws Exception {
        String indexCode = "399986";
        List<Map<String, Object>> data = CNIndex.history(indexCode);

        try (MyProducer producer = new MyProducer(BROKERS)) {
            int len = data.size();
            for (int i = 1; i <= len; i++) {
                Map<String, Object> item = data.get(len - i);
                String key = String.valueOf(item.get("indexCode"));
                String value = JsonUtils.asString(item);
                producer.send(TOPIC, key, value);
            }
        }
    }
}
