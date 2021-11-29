package com.github.superzhc.hadoop.flink.streaming.connector.mqtt;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author superz
 * @create 2021/11/29 15:34
 */
public class MqttSource extends RichSourceFunction<String> {
    private static final String DEFAULT_CLIENT_ID = "flink-mqtt";
    private static final Integer DEFAULT_PORT = 1883;
    private static final Integer DEFAULT_QOS = 2;

    private String host;
    private Integer port;
    private String username = null;
    private String password = null;
    private String topic;
    private Integer qos;
    private String clientId;

    private MqttClient client = null;

    public MqttSource(String host, String topic) {
        this(host, DEFAULT_PORT, topic, DEFAULT_QOS, DEFAULT_CLIENT_ID);
    }

    public MqttSource(String host, String topic, String clientId) {
        this(host, DEFAULT_PORT, topic, DEFAULT_QOS, clientId);
    }

    public MqttSource(String host, Integer port, String topic, String clientId) {
        this(host, port, topic, DEFAULT_QOS, clientId);
    }

    public MqttSource(String host, Integer port, String topic, Integer qos) {
        this(host, port, topic, qos, DEFAULT_CLIENT_ID);
    }

    public MqttSource(String host, Integer port, String topic, Integer qos, String clientId) {
        this(host, port, null, null, topic, qos, clientId);
    }

    public MqttSource(String host, Integer port, String username, String password, String topic, Integer qos, String clientId) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.topic = topic;
        this.qos = qos;
        this.clientId = clientId;
    }


    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        MemoryPersistence persistence = new MemoryPersistence();
        String brokerAddress = "tcp://" + host + ":" + port;
        client = new MqttClient(brokerAddress, clientId, persistence);
        // MQTT 连接选项
        MqttConnectOptions connOpts = new MqttConnectOptions();
        if (null != username) {
            connOpts.setUserName(username);
            if (null != password) {
                connOpts.setPassword(password.toCharArray());
            }
        }
        // 保留会话
        connOpts.setCleanSession(false);

        // 设置回调
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                // 连接丢失后，一般在这里面进行重连
                try {
                    client.reconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                // subscribe后得到的消息会执行到这里面
                ctx.collect(new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        client.connect(connOpts);

        client.subscribe(topic, qos);

        while (client.isConnected()) {
        }
    }

    @Override
    public void cancel() {
        if (null != client) {
            try {
                client.disconnect();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(new MqttSource("localhost", "/superz-mqtt")).print();

        env.execute("flink mqtt");
    }
}
