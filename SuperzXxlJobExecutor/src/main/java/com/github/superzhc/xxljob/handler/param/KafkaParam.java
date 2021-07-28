package com.github.superzhc.xxljob.handler.param;

import com.beust.jcommander.Parameter;

/**
 * @author superz
 * @create 2021/7/27 17:51
 */
public class KafkaParam {
    @Parameter(names = "--bootstrap-server",description = "kafka 的连接信息")
    private String bootstrapServer;

    @Parameter(names = {"--list","-a"},description = "kafka 的所有 topic")
    private boolean list=false;

    @Parameter(names = {"--topic","-t"},description = "指定主题")
    private String topic;

    public String getBootstrapServer() {
        return bootstrapServer;
    }

    public void setBootstrapServer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
