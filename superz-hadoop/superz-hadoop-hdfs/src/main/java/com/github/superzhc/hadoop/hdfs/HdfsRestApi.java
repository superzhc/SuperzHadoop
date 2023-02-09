package com.github.superzhc.hadoop.hdfs;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import org.apache.hadoop.fs.permission.FsAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.github.superzhc.common.http.HttpRequest.*;

/**
 * WebHDFS 和 HttpFS 的接口相互兼容
 * <p>
 * 参考：https://hadoop.apache.org/docs/r3.2.2/hadoop-project-dist/hadoop-hdfs/WebHDFS.html
 *
 * @author superz
 * @create 2022/3/15 15:49
 **/
public class HdfsRestApi {
    private static final Logger LOG = LoggerFactory.getLogger(HdfsRestApi.class);

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

    public String upload(String path, File file) {
        path = String.format("%s/%s", path, file.getName());
        LOG.info("上传文件：{}开始...", path);

        /**
         * 上传文件分两步：
         * 第一步运行之后，在命令行会返回 namenode 的信息。然后我们根据返回的信息，重定向并针对适当的 datanode 执行 WebHDFS API
         * 第二步根据上一步返回的Location，上传文件
         */
        Map<String, Object> params = new HashMap<>();
        params.put("overwrite", true);
        params.put("noredirect", true);
        HttpRequest request = execute(METHOD_PUT, path, "CREATE", params);
        String location = JsonUtils.string(request.body(), "Location");
        LOG.info("HDFS数据节点上传地址：{}", location);

        HttpRequest request2 = HttpRequest.put(location).send(file);
        String str = request2.body();
        LOG.info("上传结束：{}", str);
        return str;
    }

    public InputStream open(String path) {
        HttpRequest request = execute(METHOD_GET, path, "OPEN");
        return request.stream();
    }

    public String content(String path) {
        return content(path, "UTF-8");
    }

    public String content(String path, String charset) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", 0);
        params.put("length", null);
        params.put("buffersize", null);
        HttpRequest request = execute(METHOD_GET, path, "OPEN", params);
        return request.body(charset);
    }

    public String mkdirs(String path) {
        HttpRequest request = execute(METHOD_PUT, path, "MKDIRS", null);
        String result = request.body();
        return result;
    }

    public String mkdirsWithPermission(String path) {
        Map<String, Object> params = new HashMap<>();
        params.put("permission", /*"755"*/String.format("%d%d%d", FsAction.ALL.ordinal(), FsAction.READ_EXECUTE.ordinal(), FsAction.READ_EXECUTE.ordinal()));
        HttpRequest request = execute(METHOD_PUT, path, "MKDIRS", params);
        String result = request.body();
        return result;
    }

    public String rename(String oldPath, String newPath) {
        Map<String, Object> params = new HashMap<>();
        params.put("destination", newPath);
        HttpRequest request = execute(METHOD_PUT, oldPath, "RENAME", params);
        return request.body();
    }

    public String delete(String path) {
        LOG.info("删除文件开始...");
        Map<String, Object> params = new HashMap<>();
        params.put("recursive", "true");// 是否递归
        HttpRequest request = execute(METHOD_DELETE, path, "DELETE", params);
        String str = request.body();
        LOG.info("删除文件结束：{}", str);
        return str;
    }

    public String list() {
        return list("/");
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

    public String getHomeDirectory() {
        HttpRequest request = execute(METHOD_GET, "", "GETHOMEDIRECTORY");
        return request.body();
    }

    /**
     * 设置文件、目录权限
     *
     * @param path
     * @param permission 示例：755
     * @return
     */
    public String setPermission(String path, String permission) {
        Map<String, Object> params = new HashMap<>();
        params.put("permission", permission);

        HttpRequest request = execute(METHOD_PUT, path, "SETPERMISSION", params);
        return request.body();
    }

    private HttpRequest execute(String method, String path, String operator) {
        return execute(method, path, operator, new HashMap<>());
    }

    private HttpRequest execute(String method, String path, String operator, Map<String, Object> params) {
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
