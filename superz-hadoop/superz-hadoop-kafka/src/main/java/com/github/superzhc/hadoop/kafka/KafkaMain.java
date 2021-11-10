package com.github.superzhc.hadoop.kafka;

/**
 * Kafka
 * <p>
 * Apache Kafka 是一个分布式发布-订阅（pub-sub）消息系统，它以高吞吐、持久化、易水平扩展、支持流数据处理等多种特性而被广泛使用。
 * <p>
 * Kafka 具备如下特点：
 * 1. 高吞吐量、低延迟：Kafka 每秒可以处理几十万条消息，它的延迟最低只有几毫秒，每个 topic 可以分多个 partition；
 * 2. 可扩展性：Kafka 集群支持热扩展；
 * 3. 持久性、可靠性：消息被持久化到本地磁盘，并且支持数据备份防止数据丢失；
 * 4. 容错性：允许集群中节点失败（若副本数量为n，则允许n-1个节点失败）；
 * 5. 高并发：支持数千个客户端同时读写。
 *
 * @author superz
 * @create 2021/11/10 11:56
 */
public class KafkaMain {

    /**
     * Broker 可以看作一个独立的 Kafka 服务器或者 Kafka 服务实例
     * <p>
     * 1. Broker 接收来自生产者的消息，对消息设置偏移量，并将消息保存到磁盘中
     * 2. Broker 为消费者提供服务，对客户端订阅的分区的请求做出响应，返回消息给客户端
     */
    static class Broker {
    }

    /**
     * Kafka 的消息通过主题进行分类，用来实现区分实际业务
     */
    static class Topic {
    }

    /**
     * 一个主题可以被分成若干个分区
     * <p>
     * 消息以追加的方式写入分区，然后以顺序的方式进行读取
     * <p>
     * Kafka 通过分区来实现数据冗余和伸缩性
     * <p>
     * 分区可以分布在不同的服务器上，即一个主题的数据可能分布在多个服务器上，以此提供比单个服务器更高的性能
     * <p>
     * 注意事项：
     * 1. 一个分区中的消息是有序的，但对于有多个分区的主题来说，不能保证消息的顺序性
     */
    static class Partition {
    }

    /**
     * Kafka 的数据单元被称为消息
     * <p>
     * Kafka 中的消息格式由多个字段组成，其中很多字段都是用于管理消息的元数据字段，对用户是完全透明的
     * <p>
     * 消息由消息头部、key 和 value 组成，消息头部包括 消息版本号、属性、时间戳、键长度和消息体长度等信息，一般需要掌握以下 3 个重要字段的含义：
     * 1. key：消息键，对消息做分区时使用，即决定消息被保存在某个主题的哪个分区下
     * 2. value：消息体，保存实际的消息数据
     * 3. timestamp：消息发送的时间戳，用于流式处理及其他依赖时间的处理语义，如果不指定则取当前时间
     */
    static class Message {
    }

    /**
     * 趣味问题：
     * Q：Kafka 的 offset 不断积累到了最大值，该如何处理？
     * 官方回答：
     * We don't roll back offset at this moment. Since the offset is a long, it can last for a really long time. If you write 1TB a day, you can keep going for about 4 million days.
     * Plus, you can always use more partitions (each partition has its own offset).
     */
}
