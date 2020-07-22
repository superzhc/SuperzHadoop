package com.github.superzhc.yarn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

import org.apache.hadoop.yarn.api.ApplicationClientProtocol;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationRequest;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.ipc.YarnRPC;
import org.apache.hadoop.yarn.util.Records;

/**
 * 通常而言，客户端提交一个应用程序需经过以下两个步骤：
 * 1.Client通过RPC函数ApplicationClientProtocol#getNewApplication从ResourceManager中获取唯一的application ID
 * 2.Client通过RPC函数ApplicationClientProtocol#submitApplication将ApplicationMaster提交到ResourceManager上
 * 2020年07月22日 superz add
 */
public class YarnClientDemo
{
    // 创建一个ApplicationClientProtocol协议的RPC Client，并通过该Client与ResourceManager通信
    private ApplicationClientProtocol rmClient;

    private void init() throws IOException, YarnException {
        YarnConfiguration conf = new YarnConfiguration();

        InetSocketAddress rmAddress = null;// 服务器端地址
        rmClient = (ApplicationClientProtocol) YarnRPC.create(conf).getProxy(ApplicationClientProtocol.class, rmAddress,
                conf);

        // 调用ApplicationClientProtocol#getNewApplication从ResourceManager中获取唯一的ApplicationID
        GetNewApplicationRequest request = Records.newRecord(GetNewApplicationRequest.class);// Records#newRecord常用于构造一个可序列化对象
        GetNewApplicationResponse newApp = rmClient.getNewApplication(request);
        ApplicationId appId = newApp.getApplicationId();
    }

    private void submit() {
        // 客户端将启动ApplicationMaster所需的所有信息打包到数据结构ApplicationSubmissionContext中
        ApplicationSubmissionContext context = Records.newRecord(ApplicationSubmissionContext.class);
        context.setApplicationName("");// 设置应用程序名称
        // 设置应用程序其他属性，比如优先级、队列名称等
        ContainerLaunchContext amContainer = Records.newRecord(ContainerLaunchContext.class);// 构造一个AM启动上下文对象
        // ... //设置AM相关变量
        amContainer.setLocalResources(new HashMap<>());// 设置AM启动所需的本地资源
        amContainer.setEnvironment(null);// 设置AM启动所需的环境变量
    }

    public static void main(String[] args) throws IOException, YarnException {
        YarnConfiguration conf = new YarnConfiguration();
        /**
         * 不同类型应用程序与ResourceManager的交互逻辑是类似的，为了避免简化客户端重复开发，YARN提供了能与ResourceManager交互完成各种操作的编程库org.apache.hadoop.yarn.client.YarnClient。
         * 该库对常用函数进行了封装，并提供了重试、容错等机制，用户使用该库可以快速开发一个包含应用程序提交、状态查询和控制等逻辑的YARN客户端
         * 目前YARN本身自带的各类客户端均使用该编程库实现
         */
        YarnClient client = YarnClient.createYarnClient();
        client.init(conf);
        client.start();// 启动YarnClient

        // 获取一个新的Application ID
        YarnClientApplication app=client.createApplication();
    }
}
