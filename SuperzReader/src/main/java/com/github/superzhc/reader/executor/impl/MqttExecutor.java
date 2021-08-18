package com.github.superzhc.reader.executor.impl;

import com.github.superzhc.reader.datasource.impl.MqttDatasource;
import com.github.superzhc.reader.param.impl.MqttParam;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author superz
 * @create 2021/8/17 19:33
 */
public class MqttExecutor {
    private MqttDatasource dataSource;
    private MqttParam param;

    public MqttExecutor(MqttDatasource dataSource, MqttParam param) {
        this.dataSource = dataSource;
        this.param = param;
    }

    public void execute() {
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            // clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示
            // MemoryPersistence设置clientid的保存形式，默认为以内存保存
            MqttClient client = new MqttClient(dataSource.broker(), dataSource.clientId(), persistence);

            // MQTT 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(dataSource.username());
            connOpts.setPassword(dataSource.password().toCharArray());
            // 保留会话
            connOpts.setCleanSession(true);

            // 设置回调
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    // 连接丢失后，一般在这里面进行重连
                    System.out.println("连接断开，可以做重连");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // subscribe后得到的消息会执行到这里面
                    System.out.println("接收消息主题:" + topic);
                    System.out.println("接收消息Qos:" + message.getQos());
                    System.out.println("接收消息内容:" + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });

            // 建立连接
            System.out.println("Connecting to broker: " + dataSource.broker());
            client.connect(connOpts);

            System.out.println("Connected");


            // 订阅
            client.subscribe(param.getTopic());

            client.disconnect();
            System.out.println("Disconnected");
            client.close();
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
