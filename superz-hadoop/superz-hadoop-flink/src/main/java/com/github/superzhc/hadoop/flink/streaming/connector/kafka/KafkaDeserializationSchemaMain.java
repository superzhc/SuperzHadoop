package com.github.superzhc.hadoop.flink.streaming.connector.kafka;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author superz
 * @create 2021/10/26 14:23
 */
public class KafkaDeserializationSchemaMain implements KafkaDeserializationSchema<JsonNode> {
    @Override
    public boolean isEndOfStream(JsonNode nextElement) {
        return false;
    }

    @Override
    public JsonNode deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(record.value());
    }

    @Override
    public TypeInformation<JsonNode> getProducedType() {
        return TypeInformation.of(JsonNode.class);
    }
}
