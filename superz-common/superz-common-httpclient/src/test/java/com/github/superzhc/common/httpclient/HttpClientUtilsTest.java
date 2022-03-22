package com.github.superzhc.common.httpclient;

/**
 * @author superz
 * @create 2022/3/21 15:35
 **/
public class HttpClientUtilsTest {
    public static void main(String[] args) {
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
        System.out.println(HttpClientUtils.doGet(url));
    }
}
