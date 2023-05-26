package com.github.superzhc.hadoop.kafka;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MyProducerWithSchemaTest {
    static final String KAFKA_BROKER = "127.0.0.1:9092";
    static final String SCHEMA_REGISTRY_URL = "http://127.0.0.1:8081";

    private MyProducerNew<String, GenericRecord> producer = null;

    @Before
    public void setUp() throws Exception {
        Map<String, String> props = new HashMap<>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 指定Value的序列化类，KafkaAvroSerializer
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        // 指定 registry 服务的地址
        // 如果 Schema Registry 启动了高可用，那么这儿的配置值可以是多个服务地址，以逗号隔开
        props.put("schema.registry.url", SCHEMA_REGISTRY_URL);
        producer = new MyProducerNew<>(KAFKA_BROKER, props);
    }

    @After
    public void tearDown() throws Exception {
        if (null != producer) {
            producer.close();
        }
    }

    @Test
    public void product() throws Exception {
        String path=this.getClass().getResource("/avro/user.avsc").getPath();
        System.out.println(path);
        Schema schema = new Schema.Parser().parse(new File(path));
        GenericRecord avroRecord = new GenericData.Record(schema);
        avroRecord.put("name", "Alyssa");
        avroRecord.put("favorite_number", 256);
        avroRecord.put("favorite_color", "red");

        producer.send("test_20230525", "k1", avroRecord);
    }

    public void consume() throws Exception {

    }
}
