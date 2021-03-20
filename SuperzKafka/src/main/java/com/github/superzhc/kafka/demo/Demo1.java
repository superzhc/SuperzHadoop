package com.github.superzhc.kafka.demo;

import java.util.concurrent.*;

import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * 2020年07月23日 superz add
 */
public class Demo1
{
    private static final String BROKER = "ep-003.hadoop:6667";
    private static Integer timeout = 1;

    ExecutorService producerExecutors = Executors.newFixedThreadPool(10);
    ExecutorService consumerExecutors = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        Demo1 demo1 = new Demo1();
        // demo1.consume();
        demo1.consume("superzhc", "superzhc");

        // KafkaProducer<String, String> producer = ProducerThread.create(BROKER);
        // produce(producer);

        try {
            TimeUnit.MINUTES.sleep(timeout);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            demo1.close();

            // ProducerThread.close(producer);
        }
    }

    private void produce(KafkaProducer<String, String> producer) {
        for (int i = 0; i < 10; i++) {
            producerExecutors.execute(new ProducerThreadDemo(producer));
        }
    }

    private void consume() {
        for (int i = 0; i < 4; i++) {// 总共才4个分区，设置10个分区的消费者太多了
            consumerExecutors.execute(new ConsumerThread(BROKER));
        }
    }

    private void consume(String groupId, String topic) {
        for (int i = 0; i < 3; i++) {// 总共才4个分区，设置10个分区的消费者太多了
            consumerExecutors.execute(new ConsumerThread(BROKER, groupId, topic));
        }
    }

    public void close() {
        // 关闭线程池
        producerExecutors.shutdown();
        consumerExecutors.shutdown();
    }
}
