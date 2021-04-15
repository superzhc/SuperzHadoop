package com.github.superzhc.flink.manage.util;

import ch.ethz.ssh2.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2021/4/14 16:10
 */
@Slf4j
public class SSH2Util {
    /**
     * 打开连接
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public static Connection openConnection(String host, Integer port, String username, String password) throws Exception {
        log.debug("与服务器[{}:{}]建立SSH连接", host, port);
        Connection conn = new Connection(host, port);
        conn.connect(null, 60 * 60 * 1000, 30 * 1000);
        boolean isAuthenticate = conn.authenticateWithPassword(username, password);
        if (!isAuthenticate) {
            throw new IOException(StrUtil.format("服务器[{}:{}]用户名或密码不正确", host, port));
        }
        log.debug("与服务器[{}:{}]SSH连接成功", host, port);
        return conn;
    }

    /**
     * 执行命令
     *
     * @param conn
     * @param command
     * @return
     * @throws Exception
     */
    public static int exec(Connection conn, String command, Consumer<InputStream> responseConsumer) throws Exception {
        log.debug("[{}] 执行命令：{}", DateUtil.formatDateTime(new Date()), command);
        Session session = null;
        try {
            session = conn.openSession();
            session.execCommand(command);

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            CountDownLatch latch = new CountDownLatch(2);
            try {
                final InputStream stdout = new StreamGobbler(session.getStdout());
                final InputStream stderr = new StreamGobbler(session.getStderr());
                executorService.execute(new StreamThread(latch, stdout, responseConsumer));
                executorService.execute(new StreamThread(latch, stderr, responseConsumer));
            } catch (Exception e) {

            } finally {
                latch.await();
                executorService.shutdownNow();
            }

            // 等待指令执行完退出
            session.waitForCondition(ChannelCondition.EXIT_STATUS, 60 * 60 * 1000);
            //取得指令执行结束后的状态
            int exitCode = session.getExitStatus();
            log.debug("[{}] 执行命令：{}的结果[{}]", DateUtil.formatDateTime(new Date()), command, exitCode);
            return exitCode;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    /**
     * 获取当前目录下所有的文件及目录名称，不可递归
     *
     * @param conn
     * @param directory
     * @return
     * @throws Exception
     */
    public static List<String> ls(Connection conn, String directory) throws Exception {
        SFTPv3Client client = new SFTPv3Client(conn);
        List<SFTPv3DirectoryEntry> ls = client.ls(directory);
        List<String> currentDirs = ls.stream().map(d -> d.filename).collect(Collectors.toList());
        return currentDirs;
    }

    /**
     * 创建目录，父目录不存在也会一层一层的创建下去
     *
     * @param conn
     * @param directory
     * @throws Exception
     */
    public static void mkdirs(Connection conn, String directory) throws Exception {
        // 标准化路径
        String normDir = FileUtil.normalize(directory);
        String[] dirs = normDir.split("/");

        SFTPv3Client client = new SFTPv3Client(conn);
        String sDir;
        int index = 0;
        if (StrUtil.isBlank(dirs[0])) {
            sDir = "/";
            index = 1;
        } else if ("~".equals(dirs[0]) || "\\.".equals(dirs[0])) {
            sDir = dirs[0];
            index = 1;
        } else {
            sDir = "\\.";
        }
        boolean judge = true;
        dir_flag:
        for (int i = index, len = dirs.length; i < len; i++) {
            if (judge) {
                List<SFTPv3DirectoryEntry> files = client.ls(sDir);
                for (SFTPv3DirectoryEntry file : files) {
                    // 注：文件名和目录名相同，这块不判定，但是在创建会报错，直接让其报错
                    if (file.filename.equals(dirs[i]) && file.attributes.isDirectory()) {
                        sDir += "/" + dirs[i];
                        continue dir_flag;
                    }
                }

                //目录不存在，则后续的目录地址都不需要再进行判断了
                judge = false;
            }

            // 创建目录
            sDir += "/" + dirs[i];
            client.mkdir(sDir, Integer.valueOf("775", 8));
            log.debug("目录[{}]创建成功", sDir);
        }
    }

    /**
     * 创建目录，注：只能创建单层，即当前目录下，父目录必须存在
     */
    public static void mkdir(Connection conn, String directory) throws Exception {
        SFTPv3Client client = new SFTPv3Client(conn);
        // 设置权限，0775代表rwxrwxr-x，具体看Linux权限设置
        client.mkdir(directory, Integer.valueOf("0775", 8));
        client.close();
    }

    /**
     * 删除文件
     *
     * @param conn
     * @param path
     * @throws Exception
     */
    public static void rm(Connection conn, String path) throws Exception {
        SFTPv3Client client = new SFTPv3Client(conn);
        rm(client, path);
        client.close();
    }

    private static void rm(SFTPv3Client client, String path) throws Exception {
        client.rm(path);
        log.debug("文件[{}]删除成功", path);
        Console.log("文件[{}]删除成功", path);
    }

    /**
     * 删除目录，注：会将目录下的所有东西都给删掉，安全使用
     *
     * @param conn
     * @param directory
     * @throws Exception
     */
    public static void rmdir(Connection conn, String directory) throws Exception {
        SFTPv3Client client = new SFTPv3Client(conn);
        rmdir(client, directory);
        client.close();
    }

    private static void rmdir(SFTPv3Client client, String directory) throws Exception {
        // 标准化目录
        directory = FileUtil.normalize(directory);
        List<SFTPv3DirectoryEntry> files = client.ls(directory);

        for (SFTPv3DirectoryEntry file : files) {
            if (".".equals(file.filename) || "..".equals(file.filename)) {
                continue;
            }

            String path = directory + file.filename;
            if (file.attributes.isDirectory()) {
                rmdir(client, path + "/");
            } else {
                rm(client, path);
            }
        }

        // 删掉空目录
        client.rmdir(directory);
        log.debug("目录[{}]删除成功", directory);
        Console.log("目录[{}]删除成功", directory);
    }

    /**
     * 下载文件
     *
     * @param conn
     * @param path 文件的完整路径，注意只能是文件，不能是目录
     * @return
     */
    public static InputStream download(Connection conn, String path) throws Exception {
        SCPClient client = conn.createSCPClient();
        SCPInputStream in = client.get(path);
        return in;
    }

    /**
     * 上传文件
     *
     * @param conn
     * @param path 上传到服务器上的地址
     * @param in
     * @throws Exception
     */
    public static void upload(Connection conn, String path, InputStream in) throws Exception {
        int last = FileUtil.lastIndexOfSeparator(path);
        String fileName = path.substring(last + 1);
        String filePath = path.substring(0, last);
        //判断父目录是否存在，如果不存在，直接创建目录
        mkdirs(conn, filePath);

        SCPClient client = conn.createSCPClient();
        byte[] content = IoUtil.readBytes(in);
        SCPOutputStream out = client.put(fileName, content.length, filePath, "0644");
        IoUtil.write(out, true, content);
        log.debug("文件[{}]上传成功", fileName);
    }

    public static class StreamThread implements Runnable {
        private CountDownLatch latch;
        private InputStream stream;
        private Consumer<InputStream> consumer;

        public StreamThread(CountDownLatch latch, InputStream stream, Consumer<InputStream> consumer) {
            this.latch = latch;
            this.stream = stream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            if (null != consumer) {
                consumer.accept(stream);
            }
            latch.countDown();
        }
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = openConnection("namenode", 22, "flink", "cloud_control@zyzw!@34");
            // 下载文件
            // SCPInputStream in=client.get("/usr/local/flink/examples/streaming/Twitter.jar");
            // FileUtil.writeFromStream(in,"C:\\Users\\superz\\Desktop\\Flink-Examples\\Twitter.jar");

            // 上传文件
//            String path = "/usr/local/flink/examples/superz/data/WordCount-SUPERZ.jar";
//            InputStream in = FileUtil.getInputStream("C:\\Users\\superz\\Desktop\\Flink-Examples\\WordCount.jar");
//            upload(conn, path, in);
//            in.close();

//            rm(conn, path);
            //rmdir(conn, "/usr/local/flink/examples/data/one/");

            //Console.log(exec(conn,"/usr/local/flink/bin/flink run -h"));

            //Console.log(ls(conn,"c:/xx"));
            //Console.log("/xxx/bbb/dd".split("/")[0]);
            //Console.log("xxx/bbb/dd".split("/")[0]);
//            mkdirs(conn, "/usr/local/flink/superz/test");

//            rmdir(conn, "/usr/local/flink/examples/superz/");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != conn) {
                conn.close();
            }
        }
    }
}
