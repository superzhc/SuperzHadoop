package com.github.superzhc.hadoop.kafka.tool;

import com.github.superzhc.hadoop.kafka.MyAdminClient;
import com.github.superzhc.hadoop.kafka.MyProducer;
/*import org.apache.commons.lang3.StringUtils;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * @author superz
 */
public abstract class MyProducerTool extends MyBasicTool {
    private static final Logger log = LoggerFactory.getLogger(MyProducerTool.class);

    public void run(String[] args) {
        /* 消息发送时间是否随机 */
        boolean isRandom = false;
        if (null != args) {
            for (String arg : args) {
                if ("random".equalsIgnoreCase(arg)) {
                    isRandom = true;
                }
            }
        }

        log.info("{}\nKafka相关信息：\n\tbrokers:{}\n\ttopic:{}", this.getClass().getName(), brokers(), topic());
        try (MyAdminClient adminClient = new MyAdminClient(brokers())) {
            if (!adminClient.exist(topic())) {
                log.info("主题【{}】不存在，开始创建...");
                adminClient.create(topic(), 1, (short) 1, null);
                log.info("主题【{}】创建成功！");
            }
        }

        try (MyProducer producer = new MyProducer(brokers())) {
            while (true) {
                producer.send(topic(), key(), message());
//                if (StringUtils.isBlank(key())) {
//                    log.info("消息：【{}】发送成功", message());
//                } else {
//                    log.info("消息发送成功：\n\tkey:【{}】\n\tmessage:【{}】", key(), message());
//                }

                Thread.sleep(isRandom ? new Random().nextInt(60) * 1000 : 1000);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            log.info("生产者异常", e);
        }
    }

    protected abstract String topic();

    protected String key() {
        return null;
    }

    protected abstract String message();
}
