package com.github.superzhc.web.controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import com.github.superzhc.kafka.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.superzhc.web.mapper.KafkaClusterMapper;
import com.github.superzhc.web.model.KafkaCluster;
import com.github.superzhc.web.utils.LayuiUtils;
import com.github.superzhc.web.utils.Result;

/**
 * 2020年09月07日 superz add
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController
{
//    // Kerberos认证相关
//    static {
//        System.setProperty("java.security.krb5.conf", "krb5.conf");
//        System.setProperty("java.security.auth.login.config", "kafka_server_jaas.conf");
//    }
//    static KerberosSASL kerberosSASL = new KerberosSASL("SASL_PLAINTEXT", "hadoop.HADOOPSW.COM", "kafka");
//    static String DEFAULT_BROKERS = "192.168.186.142:26667";
//
//    // 创建线程池
//    // static ExecutorService executor = Executors.newFixedThreadPool(5);
//
//    static ConcurrentHashMap<String, MyProducer> producers = new ConcurrentHashMap<>();
//    static ConcurrentHashMap<String, MyConsumer> consumers = new ConcurrentHashMap<>();
//    static ConcurrentHashMap<String, Thread> producerThreads = new ConcurrentHashMap<>();
//
//    @Autowired
//    private KafkaClusterMapper kafkaClusterMapper;
//
//    @GetMapping("/cluster/list")
//    public Result cluster_list() {
//        List<KafkaCluster> lst = kafkaClusterMapper.selectAll();
//        return LayuiUtils.data(lst);
//    }
//
//    @PostMapping("/cluster/add")
//    public Result cluster_add(String url) {
//        KafkaCluster kafkaCluster = new KafkaCluster();
//        kafkaCluster.setUrl(url);
//        int i = kafkaClusterMapper.insert(kafkaCluster);
//        if (i > 0)
//            return LayuiUtils.data(kafkaClusterMapper.selectAll());
//        else
//            return LayuiUtils.msg_ok("新增失败");
//    }
//
//    @DeleteMapping("/cluster/delete/{id}")
//    public Result cluster_delete(@PathVariable(name = "id") Integer id) {
//        int i = kafkaClusterMapper.deleteByPrimaryKey(id);
//        // if (i > 0)
//        return LayuiUtils.table_ok(kafkaClusterMapper.selectAll());
//        // else
//        // return LayuiUtils.form_error("删除失败");
//    }
//
//    @PostMapping("/topic/add")
//    public Result topic_add(String brokers, String name, int partitions, short replications) {
//        MyAdminClient adminClient = new MyAdminClient(brokers, kerberosSASL, null);
//        adminClient.create(name, partitions, replications, null);
//        adminClient.close();
//        return LayuiUtils.msg_ok("新建主题成功!");
//    }
//
//    @GetMapping("/topic/list")
//    public Result topic_list(String brokers) {
//        MyAdminClient adminClient = new MyAdminClient(brokers, kerberosSASL, null);
//        Set<String> topics = adminClient.list();
//        List<Map<String, String>> ret = new ArrayList<>();
//        for (String topic : topics) {
//            Map<String, String> map = new HashMap<>();
//            map.put("name", topic);
//            map.put("value", topic);
//            ret.add(map);
//        }
//        return LayuiUtils.data(ret);
//    }
//
//    @PostMapping("/producer")
//    public Result producer(String brokers, String topic, String template) {
//        MyProducer producer = null;
//        if (!producers.containsKey(brokers)) {
//            producer = new MyProducer(brokers, kerberosSASL, null);
//            producers.put(brokers, producer);
//        }
//        else {
//            producer = producers.get(brokers);
//        }
//
//        Thread t = new Thread(new MyProducerThread(producer, topic));
//        producerThreads.put(t.getName(), t);
//        return LayuiUtils.msg_ok("生产者创建成功");
//    }
//
//    public void consumer() {
//    }
}
