package com.github.superzhc.hadoop.flink.streaming.connector.kafka.schema;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//import com.github.superzhc.kafka.jsdz.dto.EventDto;
//import com.github.superzhc.kafka.jsdz.dto.radarevent.ObjectPTCEventDetailDTO;
//import org.apache.flink.api.common.typeinfo.TypeHint;
//import org.apache.flink.api.common.typeinfo.TypeInformation;
//import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//
//import java.util.List;

public class ObjectPTCEventDetailDeserializationSchema
//        implements KafkaDeserializationSchema<EventDto<List<ObjectPTCEventDetailDTO>>>
{

//    @Override
//    public boolean isEndOfStream(EventDto<List<ObjectPTCEventDetailDTO>> nextElement) {
//        return false;
//    }
//
//    @Override
//    public EventDto<List<ObjectPTCEventDetailDTO>> deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
//        String message = new String(record.value(), "UTF-8");
//        EventDto<List<ObjectPTCEventDetailDTO>> eventDto =
//                JSON.parseObject(message, new TypeReference<EventDto<List<ObjectPTCEventDetailDTO>>>() {
//                });
//        return eventDto;
//    }
//
//    @Override
//    public TypeInformation<EventDto<List<ObjectPTCEventDetailDTO>>> getProducedType() {
//        return TypeInformation.of(new TypeHint<EventDto<List<ObjectPTCEventDetailDTO>>>() {
//        });
//    }
}
