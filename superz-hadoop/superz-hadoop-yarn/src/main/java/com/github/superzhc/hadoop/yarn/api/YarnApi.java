package com.github.superzhc.hadoop.yarn.api;

/**
 * 参考：
 * 1. https://developer.aliyun.com/article/840468
 * @author superz
 * @create 2022/3/14 17:52
 **/
public class YarnApi {
    private String rmHttpAddress;
    private int port;

    public YarnApi(String rmHttpAddress, int port) {
        this.rmHttpAddress = rmHttpAddress;
        this.port = port;
    }

    /**
     * 生成 application-id
     * {
     *     // 新创建的 applicationId
     *     "application-id": "application_1613349389113_0535",
     *     // 此集群上最大的资源
     *     "maximum-resource-capability": {
     *         // 容器可用最大内存
     *         "memory": 16384,
     *         // 容器可用最大内核
     *         "vCores": 8,
     *         "resourceInformations": {
     *             "resourceInformation": [
     *                 {
     *                     "maximumAllocation": 9223372036854775807,
     *                     "minimumAllocation": 0,
     *                     "name": "memory-mb",
     *                     "resourceType": "COUNTABLE",
     *                     "units": "Mi",
     *                     "value": 16384
     *                 },
     *                 {
     *                     "maximumAllocation": 9223372036854775807,
     *                     "minimumAllocation": 0,
     *                     "name": "vcores",
     *                     "resourceType": "COUNTABLE",
     *                     "units": "",
     *                     "value": 8
     *                 }
     *             ]
     *         }
     *     }
     * }
     */
    public void newApplication(){
        // POST http://cdh-1:8088/ws/v1/cluster/apps/new-application
        String url=String.format("http://%s:%d/ws/v1/cluster/apps/new-application",rmHttpAddress,port);
    }

    /**
     * 提交任务
     * @param json
     * {
     *     "application-id":"application_1404203615263_0001",
     *     "application-name":"test",
     *     "am-container-spec":
     *     {
     *       "local-resources":
     *       {
     *         "entry":
     *         [
     *           {
     *             "key":"AppMaster.jar",
     *             "value":
     *             {
     *               "resource":"hdfs://hdfs-namenode:9000/user/testuser/DistributedShell/demo-app/AppMaster.jar",
     *               "type":"FILE",
     *               "visibility":"APPLICATION",
     *               "size": 43004,
     *               "timestamp": 1405452071209
     *             }
     *           }
     *         ]
     *       },
     *       "commands":
     *       {
     *         "command":"{{JAVA_HOME}}/bin/java -Xmx10m org.apache.hadoop.yarn.applications.distributedshell.ApplicationMaster --container_memory 10 --container_vcores 1 --num_containers 1 --priority 0 1><LOG_DIR>/AppMaster.stdout 2><LOG_DIR>/AppMaster.stderr"
     *       },
     *       "environment":
     *       {
     *         "entry":
     *         [
     *           {
     *             "key": "DISTRIBUTEDSHELLSCRIPTTIMESTAMP",
     *             "value": "1405459400754"
     *           },
     *           {
     *             "key": "CLASSPATH",
     *             "value": "{{CLASSPATH}}<CPS>./*<CPS>{{HADOOP_CONF_DIR}}<CPS>{{HADOOP_COMMON_HOME}}/share/hadoop/common/*<CPS>{{HADOOP_COMMON_HOME}}/share/hadoop/common/lib/*<CPS>{{HADOOP_HDFS_HOME}}/share/hadoop/hdfs/*<CPS>{{HADOOP_HDFS_HOME}}/share/hadoop/hdfs/lib/*<CPS>{{HADOOP_YARN_HOME}}/share/hadoop/yarn/*<CPS>{{HADOOP_YARN_HOME}}/share/hadoop/yarn/lib/*<CPS>./log4j.properties"
     *           },
     *           {
     *             "key": "DISTRIBUTEDSHELLSCRIPTLEN",
     *             "value": "6"
     *           },
     *           {
     *             "key": "DISTRIBUTEDSHELLSCRIPTLOCATION",
     *             "value": "hdfs://hdfs-namenode:9000/user/testuser/demo-app/shellCommands"
     *           }
     *         ]
     *       }
     *     },
     *     "unmanaged-AM":false,
     *     "max-app-attempts":2,
     *     "resource":
     *     {
     *       "memory":1024,
     *       "vCores":1
     *     },
     *     "application-type":"YARN",
     *     "keep-containers-across-application-attempts":false
     *   }
     * application-id	string	申请编号
     * application-name	string	申请名称
     * queue	string	应将应用程序提交到的队列的名称
     * priority	int	应用程序的优先级
     * am-container-spec	object	应用程序主容器启动上下文，如下所述
     * unmanaged-AM	boolean	该应用程序是否使用非托管应用程序主机
     * max-app-attempts	int	此应用程序的最大尝试次数
     * resource	object	应用程序主机需要的资源，如下所述
     * application-type	string	应用程序类型（MapReduce，Pig，Hive等）
     * keep-containers-across-application-attmpts	boolean	YARN是否应保留此应用程序使用的容器而不是销毁它们
     * application-tags	object	应用程序标签列表，请参阅有关如何指定标签的请求示例
     *
     * local-resources	object	描述需要本地化的资源的对象，如下所述
     * environment	object	容器的环境变量，指定为键值对
     * commands	object	用于启动容器的命令（应按执行顺序）
     * service-data	object	特定于应用程序的服务数据；key是辅助服务的名称，值是您希望传递的数据的base-64编码
     * credentials	object	您的应用程序运行所需的凭据，如下所述
     * application-acls	object	您的应用程序的ACLs；密钥可以是“ VIEW_APP”或“ MODIFY_APP”，值是具有权限的用户列表
     *
     * resource	string	要本地化的资源的位置
     * type	string	资源类型；选项是“ ARCHIVE”，“ FILE”和“ PATTERN”
     * visibility	string	可见要本地化的资源；选项是“ PUBLIC”，“ PRIVATE”和“ APPLICATION”
     * size	long	要本地化的资源大小
     * timestamp	long	要本地化的资源的时间戳
     *
     * tokens	object	您希望传递给应用程序的令牌，指定为键值对。密钥是令牌的标识符，值是令牌（应使用相应的Web服务获取）
     * secrets	object	您希望在应用程序中使用的机密，指定为键值对。它们的键是标识符，值是密钥的base-64编码
     *
     * memory	int	每个容器所需的内存
     * vCores	int	每个容器所需的虚拟核心
     */
    public void submitJob(String json){
        // POST http://<rm http address:port>/ws/v1/cluster/apps
        String url=String.format("http://%s:%d/ws/v1/cluster/apps",rmHttpAddress,port);
    }

    /**
     * 查询所有任务
     */
    public void apps(Long start, Long end) {
        if (null == start) {
            start = 0L;
        }

        if (null == end) {
            end = Long.MAX_VALUE;
        }

        //GET rm-http-address:port/ws/v1/cluster/apps
        String url = String.format("http://%s:%d/ws/v1/cluster/apps?startedTimeBegin=%d＆startedTimeEnd=%d", rmHttpAddress, port, start, end);
    }

    /**
     * 查询单个任务
     *
     * @param applicationId
     */
    public void app(String applicationId){
        // GET rm-http-address:port/ws/v1/cluster/apps/{appid}
        String url=String.format("http://%s:%d/ws/v1/cluster/apps/%s",rmHttpAddress,port,applicationId);
    }
}
