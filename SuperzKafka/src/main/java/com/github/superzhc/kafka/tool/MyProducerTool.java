package com.github.superzhc.kafka.tool;

import com.github.superzhc.data.jsdz.generateor.ObjectPTCEventDetailData;
import com.github.superzhc.kafka.MyAdminClient;
import com.github.superzhc.kafka.MyProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author superz
 */
public class MyProducerTool {
    private static final Logger log = LoggerFactory.getLogger(MyProducerTool.class);

    public static void main(String[] args) {
        String topic = "superz-test";

        try (MyAdminClient adminClient = new MyAdminClient(MyKafkaConfigs.JSDZ_BROKER)) {
            if (!adminClient.exist(topic)) {
                adminClient.create(topic, 1, (short) 0, null);
            }
        }

        try (MyProducer producer = new MyProducer(MyKafkaConfigs.JSDZ_BROKER)) {
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (true) {
                // int random = (int) (Math.random() * 100) % 3;
                // String message = String.format("Producer-%d send the message at %s", random, sdf.format(new Date()));
                String message=new ObjectPTCEventDetailData().convert2String();
                producer.send(topic, null, message);
                log.info("消息：【{}】发送成功", message);

                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


}
