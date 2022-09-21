package com.github.superzhc.hadoop.flink.api;

import com.github.superzhc.common.http.HttpRequest;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 版本：1.12
 * 官方文档：<a>https://ci.apache.org/projects/flink/flink-docs-release-1.12/ops/rest_api.html</a>
 * @author superz
 * @create 2021/10/28 15:43
 */
public class FlinkRestAPI {
    private String url;

    public FlinkRestAPI(String url) {
        this.url = url;
    }

    protected String get(String path) {
        return HttpRequest.get(fullUrl(path)).body();
    }

    protected String post(String path, String reqBody) {
        return HttpRequest.post(fullUrl(path)).json(reqBody).body();
    }

    private String fullUrl(String path) {
        return String.format("%s/v1%s", url, path);
    }


    public static class Overview extends FlinkRestAPI {

        public Overview(String url) {
            super(url);
        }

        /**
         * 查看 WEB UI 的配置信息
         * <p>
         * 请求路径：http://{ip}:{port}/v1/config
         * 请求方式：GET
         * 请求参数：
         * 返回结果：
         */
        public void config() {
        }

        /**
         * 查看集群信息
         * <p>
         * 请求路径：http://{ip}:{port}/v1/overview
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：
         * {
         * "taskmanagers": 1,
         * "slots-total": 1,
         * "slots-available": 0,
         * "jobs-running": 1,
         * "jobs-finished": 0,
         * "jobs-cancelled": 0,
         * "jobs-failed": 0,
         * "flink-version": "1.11.2",
         * "flink-commit": "fe36135"
         * }
         */
        public void overview() {
        }
    }

    public static class JobManager extends FlinkRestAPI{
        public JobManager(String url) {
            super(url);
        }

        /**
         * 查看集群的配置信息
         *
         * 请求路径：http://{ip}:{port}/v1/jobmanager/config
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：
         * {
         *   "type" : "object",
         *   "id" : "urn:jsonschema:org:apache:flink:runtime:rest:messages:DashboardConfiguration",
         *   "properties" : {
         *     "features" : {
         *       "type" : "object",
         *       "id" : "urn:jsonschema:org:apache:flink:runtime:rest:messages:DashboardConfiguration:Features",
         *       "properties" : {
         *         "web-submit" : {
         *           "type" : "boolean"
         *         }
         *       }
         *     },
         *     "flink-revision" : {
         *       "type" : "string"
         *     },
         *     "flink-version" : {
         *       "type" : "string"
         *     },
         *     "refresh-interval" : {
         *       "type" : "integer"
         *     },
         *     "timezone-name" : {
         *       "type" : "string"
         *     },
         *     "timezone-offset" : {
         *       "type" : "integer"
         *     }
         *   }
         * }
         */
        public void config(){}

        /**
         * 查看 JobManager 上所有日志文件列表
         *
         * 请求路径：http://{ip}:{port}/v1/jobmanager/logs
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：
         * {
         *   "logs": [
         *     {
         *       "name": "flink-wy-client-wy.lan.log",
         *       "size": 6763
         *     },
         *     ...
         *   ]
         * }
         */
        public void logs(){}

        /**
         * 查看 JobManager 的 Metrics 信息
         *
         * 请求路径：http://{ip}:{port}/v1/jobmanager/metrics
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：
         * [
         *   {
         *     "id": "taskSlotsAvailable"
         *   },
         *   ...
         *   {
         *     "id": "Status.JVM.Memory.Heap.Used"
         *   }
         * ]
         */
        public void metrics(){}
    }

    public static class TaskManager extends FlinkRestAPI{
        public TaskManager(String url) {
            super(url);
        }

        /**
         * 查看所有 Taskmanager 的信息
         *
         * 请求路径：http://{ip}:{port}/v1/taskmanagers
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：
         * {
         *   "taskmanagers": [
         *     {
         *       "id": "14b7a2a632fd93d3548a8e17a311697b",
         *       "path": "akka.tcp://flink@192.168.99.198:49627/user/rpc/taskmanager_0",
         *       "dataPort": 49629,
         *       "timeSinceLastHeartbeat": 1605340256498,
         *       "slotsNumber": 1,
         *       "freeSlots": 0,
         *       "totalResource": {
         *         "cpuCores": 0.0,
         *         "taskHeapMemory": 0,
         *         "taskOffHeapMemory": 0,
         *         "managedMemory": 0,
         *         "networkMemory": 0,
         *         "extendedResources": {
         *         }
         *       },
         *       "freeResource": {
         *         "cpuCores": 0.0,
         *         "taskHeapMemory": 0,
         *         "taskOffHeapMemory": 0,
         *         "managedMemory": 0,
         *         "networkMemory": 0,
         *         "extendedResources": {
         *         }
         *       },
         *       "hardware": {
         *         "cpuCores": 4,
         *         "physicalMemory": 8589934592,
         *         "freeMemory": 536870912,
         *         "managedMemory": 536870920
         *       }
         *     }
         *   ]
         * }
         */
        public void taskManagers(){}
    }

    public static class Jar extends FlinkRestAPI {

        public Jar(String url) {
            super(url);
        }

        /**
         * 获取所有上传的 jar 包
         * 请求路径：http://{ip}:{port}/v1/jars
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：
         * {
         * "address": "http://0.0.0.0:8081",
         * "files": [
         * {
         * "id": "a9ffd519-5222-40aa-9228-22464417a055_examples-java-1.0.jar",
         * "name": "examples-java-1.0.jar",
         * "uploaded": 1590635755000,
         * "entry": []
         * }
         * ]
         * }
         */
        public void jars() {
        }

        /**
         * 上传 jar 包
         * 请求路径：http://{ip}:{port}/v1/jars/upload
         * 请求方式：POST
         * 请求参数：设置请求头 application/java-archive
         * 返回结果：
         * {
         * "status": "success",
         * "filename": "a9ffd519-5222-40aa-9228-22464417a055_examples-java-1.0.jar"
         * }
         */
        public void upload() {
        }

        /**
         * 运行程序
         * <p>
         * 请求路径：http://{ip}:{port}/v1/jars/{jarid}/run
         * 请求方式：POST
         * 请求参数：
         * 1. entry-class 程序入口类
         * 2. programArgsList：Array
         * 3. programArg：String
         * 4. parallelism：Integer
         * 返回结果：jobid
         * {
         * "jobid": "f188d5c0ce0b0730010d3286b3a2cf8a"
         * }
         * <p>
         * 示例：
         * 1. http://{ip}:{port}/jars/a9ffd519-5222-40aa-9228-22464417a055_examples-java-1.0.jar/run?entry-class=io.github.streamingwithflink.demo.BatchCountFromJdbc&programArgsList=--bootstrap.servers hadoop2.test.yunwei.puppet.dh:6667,hadoop3.test.yunwei.puppet.dh:6667,hadoop4.test.yunwei.puppet.dh:6667 --group.id m3gcn_tab_map_race_3 --client.id m3gcn_tab_map_race_3 --config.file hdfs://DHTestCluster/user/gezhihui/flink/configuration/m3gcn_tab_map_race2.txt --job.name m3gcn_tab_map_race
         * 2. http://{ip}:{port}/jars/a9ffd519-5222-40aa-9228-22464417a055_examples-java-1.0.jar/run
         * {
         * "entryClass":"io.github.streamingwithflink.demo.BatchCountFromJdbc"
         * "programArgsList": [
         * "--name",
         * "zhansan"]
         * }
         * <p>
         * 请求体中的programArgsList可以传值到main函数的string[]args，取值可以通过ParameterTool parameters =
         * ParameterTool.fromArgs(args);String name = parameters.get("name");所以他的key-value的格式是key为"--key"或者"-key",
         * value为key的下一行，根据上述代码，value不要以"-"作为开头，这里也可以自己去解析String[]，这里args就对应我们programArgsList
         * 的传参，如3所示
         * <p>
         * 3. http://{ip}:{port}/jars/a9ffd519-5222-40aa-9228-22464417a055_examples-java-1.0.jar/run
         * {
         * "entryClass":"io.github.streamingwithflink.demo.BatchCountFromJdbc"
         * "programArgsList": [
         * "lisi",
         * "zhansan"]
         * }
         */
        public void run() {
        }

        /**
         * 删除 jar 包
         * <p>
         * 请求路径：http://{ip}:{port}/jars/{jarid}
         * 请求方式：DELETE
         * 请求参数：无
         * 返回结果：
         */
        public void delete() {
        }
    }

    public static class Job extends FlinkRestAPI {

        public Job(String url) {
            super(url);
        }

        /**
         * 查看所有的 job
         * <p>
         * 请求路径：http://{ip}:{port}/v1/jobs/overview
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：
         * {
         *   "jobs": [
         *     {
         *       "jid": "a2848961b7e7768dcf2d9ac71405aacf",
         *       "name": "flink-kafka-stream",
         *       "state": "RUNNING",
         *       "start-time": 1604157862897,
         *       "end-time": -1,
         *       "duration": 983781978,
         *       "last-modification": 1604157863753,
         *       "tasks": {
         *         "total": 1,
         *         "created": 0,
         *         "scheduled": 0,
         *         "deploying": 0,
         *         "running": 1,
         *         "finished": 0,
         *         "canceling": 0,
         *         "canceled": 0,
         *         "failed": 0,
         *         "reconciling": 0
         *       }
         *     }
         *   ]
         * }
         */
        public void jobs() {
        }

        /**
         * 查看某个job的具体信息
         * <p>
         * 请求路径：http://{ip}:{port}/v1/jobs/{jobid}
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：
         * {
         *   "jid": "a2848961b7e7768dcf2d9ac71405aacf",
         *   "name": "flink-kafka-stream",
         *   "isStoppable": false,
         *   "state": "RUNNING",
         *   "start-time": 1604157862897,
         *   "end-time": -1,
         *   "duration": 983903294,
         *   "now": 1605141766191,
         *   "timestamps": {
         *     "RESTARTING": 0,
         *     "FAILED": 0,
         *     "SUSPENDED": 0,
         *     "RECONCILING": 0,
         *     "CREATED": 1604157862897,
         *     "RUNNING": 1604157862984,
         *     "CANCELED": 0,
         *     "FINISHED": 0,
         *     "FAILING": 0,
         *     "CANCELLING": 0
         *   },
         *   "vertices": [
         *     {
         *       "id": "cbc357ccb763df2852fee8c4fc7d55f2",
         *       "name": "Source: Custom Source -> Map -> Sink: Print to Std. Out",
         *       "parallelism": 1,
         *       "status": "RUNNING",
         *       "start-time": 1604157863134,
         *       "end-time": -1,
         *       "duration": 983903057,
         *       "tasks": {
         *         "CANCELED": 0,
         *         "RUNNING": 1,
         *         "CANCELING": 0,
         *         "RECONCILING": 0,
         *         "CREATED": 0,
         *         "DEPLOYING": 0,
         *         "FAILED": 0,
         *         "SCHEDULED": 0,
         *         "FINISHED": 0
         *       },
         *       "metrics": {
         *         "read-bytes": 0,
         *         "read-bytes-complete": true,
         *         "write-bytes": 0,
         *         "write-bytes-complete": true,
         *         "read-records": 0,
         *         "read-records-complete": true,
         *         "write-records": 0,
         *         "write-records-complete": true
         *       }
         *     }
         *   ],
         *   "status-counts": {
         *     "CANCELED": 0,
         *     "RUNNING": 1,
         *     "CANCELING": 0,
         *     "RECONCILING": 0,
         *     "CREATED": 0,
         *     "DEPLOYING": 0,
         *     "FAILED": 0,
         *     "SCHEDULED": 0,
         *     "FINISHED": 0
         *   },
         *   "plan": {
         *     "jid": "a2848961b7e7768dcf2d9ac71405aacf",
         *     "name": "flink-kafka-stream",
         *     "nodes": [
         *       {
         *         "id": "cbc357ccb763df2852fee8c4fc7d55f2",
         *         "parallelism": 1,
         *         "operator": "",
         *         "operator_strategy": "",
         *         "description": "Source: Custom Source -&gt; Map -&gt; Sink: Print to Std. Out",
         *         "optimizer_properties": {
         *
         *         }
         *       }
         *     ]
         *   }
         * }
         */
        public void job() {
        }

        /**
         * 查看作业的数据流执行计划
         *
         * 请求路径：http://{ip}:{port}/v1/jobs/{jobid}/plan
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：
         * {
         *   "plan": {
         *     "jid": "719ca461851b0afad055d81309b945a8",
         *     "name": "flink-kafka-stream",
         *     "nodes": [
         *       {
         *         "id": "cbc357ccb763df2852fee8c4fc7d55f2",
         *         "parallelism": 1,
         *         "operator": "",
         *         "operator_strategy": "",
         *         "description": "Source: Custom Source -&gt; Map -&gt; Sink: Print to Std. Out",
         *         "optimizer_properties": {
         *
         *         }
         *       }
         *     ]
         *   }
         * }
         */
        public void plan(){
        }

        /**
         * 取消 Job
         * <p>
         * 请求路径：http://{ip}:{port}/jobs/{jobid}/cancel
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：
         */
        public void cancle() {
        }

        /**
         * 查看某个job的checkpoints
         * <p>
         * 请求路径：http://{ip}:{port}/jobs/{jobid}/checkpoints
         * 请求方式：GET
         * 请求参数：无
         * 返回结果：略
         */
        public void checkpoints() {
        }

        /**
         * 触发保存点
         *
         * 请求路径：http://{ip}:{port}/jobs/{jobid}/savepoints
         * 请求方式：POST
         * 请求参数：
         * {
         *     cancle-job:[boolean 类型],
         *     target-directory:xxx
         * }
         * 返回结果：
         * {"request-id":"e7c2e8806a24818baebe23012569d9e1"}
         * 注意：这是一个异步接口
         */
        public String savepoints(String jobId, Boolean cancelJob, String targetDirectory) throws Exception {
            String path = String.format("/jobs/%s/savepoints", jobId);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("cancel-job", cancelJob);
            if (null != targetDirectory && targetDirectory.length() > 0) {
                json.put("target-directory", targetDirectory);
            }

            return post(path, mapper.writeValueAsString(json));
        }

        public String savepointsStatus(String jobId, String triggerId) throws Exception {
            String path = String.format("/jobs/%s/savepoints/%s", jobId, triggerId);
            return get(path);
        }
    }
}
