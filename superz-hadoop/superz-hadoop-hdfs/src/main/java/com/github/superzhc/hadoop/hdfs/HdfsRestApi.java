package com.github.superzhc.hadoop.hdfs;

import org.apache.hadoop.fs.permission.FsAction;

import java.util.HashMap;
import java.util.Map;

/**
 * WebHDFS 和 HttpFS 的接口相互兼容
 * <p>
 * 参考：https://hadoop.apache.org/docs/r2.4.1/hadoop-project-dist/hadoop-hdfs/WebHDFS.html
 *
 * @author superz
 * @create 2022/3/15 15:49
 **/
public class HdfsRestApi {
    private final static String REST_API_TEMPLATOR_URL = "http://%s:%d/webhdfs/v1/%s?%s";

    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";

    private String host;
    private int port;

    public HdfsRestApi(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void mkdirs(String path) {
        execute(PUT, path, "MKDIRS", null);
    }

    public void mkdirsWithPermission(String path) {
        Map<String, String> params = new HashMap<>();
        params.put("permission", /*"755"*/String.format("%d%d%d", FsAction.ALL.ordinal(), FsAction.READ_EXECUTE.ordinal(), FsAction.READ_EXECUTE.ordinal()));
        execute(PUT, path, "MKDIRS", params);
    }

    public void rename(String oldPath, String newPath) {
        Map<String, String> params = new HashMap<>();
        params.put("destination", newPath);
        execute(PUT, oldPath, "RENAME", params);
    }

    public void delete(String path) {
        Map<String, String> params = new HashMap<>();
        params.put("recursive", "true");// 是否递归
        execute(DELETE, path, "DELETE", params);
    }

    private void execute(String method, String path, String operator, Map<String, String> params) {
        StringBuilder sb = new StringBuilder("");

        sb.append("op").append("=").append(operator);

        if (null != params) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                sb.append("&").append(param.getKey()).append("=").append(param.getValue());
            }
        }

        String url = String.format(REST_API_TEMPLATOR_URL, host, port, path, sb.toString());
    }
}
