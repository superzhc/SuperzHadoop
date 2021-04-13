package com.github.superzhc.flink.manage.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

/**
 * 已经引入hutool-core工具，推荐使用RuntimeUtil
 *
 * @author superz
 * @create 2021/4/10 14:17
 */
@Deprecated
@Slf4j
public class ProcessUtil {

    public static int execCommnad(String command) {
        StringTokenizer st = new StringTokenizer(command);
        List<String> cmdarray = new ArrayList<>(st.countTokens());
        while (st.hasMoreTokens()) {
            cmdarray.add(st.nextToken());
        }
        return exec(cmdarray);
    }

    public static int exec(String... command) {
        List<String> lst = new ArrayList<>(command.length);
        for (String arg : command) {
            lst.add(arg);
        }
        return exec(lst);
    }

    public static int exec(List<String> command) {
        return exec(command, new Consumer<InputStream>() {
            @Override
            public void accept(InputStream inputStream) {
                // 获取当前操作系统的编码
                String encoding = System.getProperty("sun.jnu.encoding");
                log.info("当前系统编码：{}", encoding);
                try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream, encoding))) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = output.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                    log.info(result.toString());
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * Java调用进程，并处理进程输出的流
     *
     * @param command  命令语句
     * @param consumer 消费进程输出流的对象
     * @return
     */
    public static int exec(List<String> command, Consumer<InputStream> consumer) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        // 将错误流重定向到输出流中，两个流进行合并
        processBuilder.redirectErrorStream(true);

        Process process = null;
        try {
            process = processBuilder.start();

            if (null != consumer) {
                consumer.accept(process.getInputStream());
            }

            // 执行结果
            int exitCode = process.waitFor();
            return exitCode;
        } catch (Exception e) {
            return -1;
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
    }
}
