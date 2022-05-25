package com.github.superzhc.hadoop.hdfs;

import com.github.superzhc.common.http.HttpRequest;
import org.apache.hadoop.fs.permission.FsAction;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.github.superzhc.common.http.HttpRequest.*;

/**
 * WebHDFS 和 HttpFS 的接口相互兼容
 * <p>
 * 参考：https://hadoop.apache.org/docs/r2.4.1/hadoop-project-dist/hadoop-hdfs/WebHDFS.html
 *
 * @author superz
 * @create 2022/3/15 15:49
 **/
public class HdfsRestApi {
    /* Hadoop3.x 版本的 WebHDFS 默认端口号为 9870 */
    public static final Integer DEFAULT_WEBHDFS_PORT_3_X = 9870;
    private final static String REST_API_TEMPLATOR_URL = "http://%s:%d/webhdfs/v1/%s";

    private String host;
    private int port;
    private String user = null;

    public HdfsRestApi(String host) {
        this(host, DEFAULT_WEBHDFS_PORT_3_X);
    }

    public HdfsRestApi(String host, int port) {
        this(host, port, null);
    }

    public HdfsRestApi(String host, int port, String user) {
        this.host = host;
        this.port = port;
        this.user = user;
    }

    public InputStream open(String path) {
        HttpRequest request = execute(METHOD_GET, path, "OPEN");
        return request.stream();
    }

    public String mkdirs(String path) {
        HttpRequest request = execute(METHOD_PUT, path, "MKDIRS", null);
        String result = request.body();
        return result;
    }

    public String mkdirsWithPermission(String path) {
        Map<String, String> params = new HashMap<>();
        params.put("permission", /*"755"*/String.format("%d%d%d", FsAction.ALL.ordinal(), FsAction.READ_EXECUTE.ordinal(), FsAction.READ_EXECUTE.ordinal()));
        HttpRequest request = execute(METHOD_PUT, path, "MKDIRS", params);
        String result = request.body();
        return result;
    }

    public String rename(String oldPath, String newPath) {
        Map<String, String> params = new HashMap<>();
        params.put("destination", newPath);
        HttpRequest request = execute(METHOD_PUT, oldPath, "RENAME", params);
        return request.body();
    }

    public String delete(String path) {
        Map<String, String> params = new HashMap<>();
        params.put("recursive", "true");// 是否递归
        HttpRequest request = execute(METHOD_DELETE, path, "DELETE", params);
        return request.body();
    }

    public String list(String path) {
        HttpRequest request = execute(METHOD_GET, path, "LISTSTATUS");
        return request.body();
    }

    public String status(String path) {
        HttpRequest request = execute(METHOD_GET, path, "GETFILESTATUS");
        return request.body();
    }

    public String summary(String path) {
        HttpRequest request = execute(METHOD_GET, path, "GETCONTENTSUMMARY");
        return request.body();
    }

    private HttpRequest execute(String method, String path, String operator) {
        return execute(method, path, operator, new HashMap<>());
    }

    private HttpRequest execute(String method, String path, String operator, Map<String, String> params) {
        if (null == params) {
            params = new HashMap<>();
        }
        // 操作
        params.put("op", operator);

        if (null != user && user.trim().length() > 0) {
            params.put("user.name", user);
            params.put("doas", user);
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        String url = String.format(REST_API_TEMPLATOR_URL, host, port, path);
        url = HttpRequest.append(url, params);

        HttpRequest request = new HttpRequest(url, method);
        return request;
    }
}
