package com.github.superzhc.reader.datasource.impl;

import com.github.superzhc.reader.datasource.Datasource;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;

/**
 * @author superz
 * @create 2021/8/17 17:23
 */
public class KafkaConsumerDatasource extends Datasource {

    public KafkaConsumerDatasource(Map<String, Object> params) {
        super(params);
    }

    public String bootstrapServers() {
        return (String) params.get("bootstrap.servers");
    }

    public String groupId() {
        return (String) params.get("group.id");
    }

    public String keyDeserializer() {
        if (params.containsKey("key.deserializer")) {
            return (String) params.get("key.deserializer");
        }
        return StringDeserializer.class.getName();
    }

    public String valueDeserializer() {
        if (params.containsKey("value.deserializer")) {
            return (String) params.get("value.deserializer");
        }
        return StringDeserializer.class.getName();
    }
}
