package com.github.superzhc.hadoop.flink.deploy;

import com.github.superzhc.common.http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2022/3/18 17:47
 **/
public class FlinkOnYarnRestApi {
    public static void main(String[] args) {
        String host = "log-platform01";
        int port = 8088;

        String applicationId = "application_1647426438639_0022";

        applicationId = "application_1647426438639_0021";

        String yarnRestApiTemplate = "http://%s:%d/ws/%s/%s";
        // 分析任务的状态，获取 application 的信息
//        String applicationInfos = HttpRequest.get(String.format(yarnRestApiTemplate, host, port, "v1", String.format("cluster/apps/%s", applicationId))).body();
//        System.out.println(JSONUtils.format(applicationInfos));

//        nonRunning();
        running();
    }

    /**
     * 通过 WebHDFS 进行 HDFS 读取
     */
    public static void nonRunning() {
        String namenode = "log-platform01";
        int port = 50070;
        String user = "root";
        String applicationId = "application_1647426438639_0021";

        String hdfsPath = String.format("/tmp/logs/%s/logs-tfile/%s", user, applicationId);

        // 获取目录下的文件列表
//        String url = String.format("http://%s:%d/webhdfs/v1/%s?user.name=%s&op=LISTSTATUS", namenode, port, hdfsPath,user);
//        String result = HttpRequest.get(url).body();
//        System.out.println(JSONUtils.format(result));

        // 读取文件
        hdfsPath = String.format("/tmp/logs/%s/logs-tfile/%s/%s", user, applicationId, "log-platform01_37501");
        String url = String.format("http://%s:%d/webhdfs/v1/%s?user.name=%s&op=OPEN", namenode, port, hdfsPath, user);
        /* Fixme 中文乱码 */
        String result = HttpRequest.get(url).body("UTF-8");
        System.out.println(result);
    }

    public static void running() {
        // 通过 Application 信息解析获得 trackingUrl
        String trackingUrl = "http://log-platform01:8088/proxy/application_1647426438639_0023/";

        String path = null;
        /* {"jobs":[{"id":"8cab1668f35c5126cdeac282a5ffeb7c","status":"RUNNING"}]} */
        path = "jobs";

        /* {"logs":[{"name":"prelaunch.out","size":100},{"name":"prelaunch.err","size":0},{"name":"launch_container.sh","size":18712},{"name":"directory.info","size":6380},{"name":"jobmanager.out","size":0},{"name":"jobmanager.err","size":573},{"name":"jobmanager.log","size":61495}]} */
        path = "jobmanager/logs";

        path = "jobmanager/log";

        path = "jobmanager/stdout";

        /**
         * jonmanager.err
         * jobmanager.log
         * jobmanager.out
         *
         * taskmanager.err
         * taskmanager.log
         * taskmanager.out
         */
        path = "jobmanager/logs/jobmanager.log";

//        /* {"taskmanagers":[{"id":"container_e07_1647426438639_0022_01_000002","path":"akka.tcp://flink@log-platform02:40595/user/rpc/taskmanager_0","dataPort":38204,"jmxPort":-1,"timeSinceLastHeartbeat":1647598127171,"slotsNumber":12,"freeSlots":8,"totalResource":{"cpuCores":12.0,"taskHeapMemory":537,"taskOffHeapMemory":0,"managedMemory":634,"networkMemory":158,"extendedResources":{}},"freeResource":{"cpuCores":8.0,"taskHeapMemory":358,"taskOffHeapMemory":0,"managedMemory":423,"networkMemory":105,"extendedResources":{}},"hardware":{"cpuCores":32,"physicalMemory":67385241600,"freeMemory":669515776,"managedMemory":665719939},"memoryConfiguration":{"frameworkHeap":134217728,"taskHeap":563714445,"frameworkOffHeap":134217728,"taskOffHeap":0,"networkMemory":166429984,"managedMemory":665719939,"jvmMetaspace":268435456,"jvmOverhead":214748368,"totalFlinkMemory":null,"totalProcessMemory":2147483648}}]} */
//        path = "taskmanagers";
//
//        /* {"logs":[{"name":"prelaunch.out","size":100},{"name":"prelaunch.err","size":0},{"name":"launch_container.sh","size":12140},{"name":"directory.info","size":6088},{"name":"taskmanager.out","size":135},{"name":"taskmanager.err","size":573},{"name":"taskmanager.log","size":66777}]} */
//        String taskmanagerId = "container_e07_1647426438639_0022_01_000002";
//        path = String.format("/taskmanagers/%s/logs", taskmanagerId);
//
//        path = String.format("/taskmanagers/%s/log", taskmanagerId);
//
//        path = String.format("/taskmanagers/%s/stdout", taskmanagerId);
//
//        /* 具体日志文件获取 */
//        path = String.format("/taskmanagers/%s/logs/taskmanager.err", taskmanagerId);

//        String result = HttpRequest.get(String.format("%s%s", trackingUrl, path)).body();
//        System.out.println(result);

        InputStream inputStream = HttpRequest.get(String.format("%s%s", trackingUrl, path)).stream();
        try(InputStreamReader inputStreamReader=new InputStreamReader(inputStream)) {
            try(BufferedReader reader=new BufferedReader(inputStreamReader)) {

                StringBuilder patternBuilder = new StringBuilder();
                // 时间正则匹配
                patternBuilder.append("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\\d|3[0-1])\\s{1}([0-1]\\d|2[0-3]):([0-5]\\d):([0-5]\\d),\\d{3}");
                patternBuilder.append("\\s{1}");//空格
                // 日志级别正则匹配
                patternBuilder.append("(OFF|FATAL|ERROR|WARN|INFO|DEBUG|TRACE|ALL)\\s{1,2}");
                // patternBuilder.append("\\s{1}");
                // patternBuilder.append("[\\w.]+\\s+");
                // patternBuilder.append("\\s{1}");
                Pattern pattern = Pattern.compile(patternBuilder.toString());
//                Matcher matcher = pattern.matcher(result);
//                while (matcher.find()) {
//            int count=matcher.groupCount();
//            StringBuilder sb=new StringBuilder();
//            for (int i=0;i<=count;i++){
//                sb.append(matcher.group(i)).append("@@");
//            }
//            System.out.println(sb.toString());
//                    System.out.println(matcher.group() + "@@" + matcher.group(6));
//                }

                String line;
                while ((line=reader.readLine())!=null){
                    Matcher matcher=pattern.matcher(line);
                    if(matcher.find()){
                        if("INFO".equals(matcher.group(6))) {
                            System.out.println(line);
                        }
                    }else{
                        System.out.println(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
