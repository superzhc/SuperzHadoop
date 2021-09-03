package com.github.superzhc.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 进程工具类
 *
 * @author superz
 * @date 2021年4月8日
 */
public class ProcessUtils {
    private static final Logger log = LoggerFactory.getLogger(ProcessUtils.class);

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
        if (log.isDebugEnabled()) {
            /*log.debug("当前执行的命令为：{}", ListUtils.list2String(command));*/
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        // 将错误流重定向到输出流中，两个流进行合并
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            if (null != consumer) {
                consumer.accept(process.getInputStream());
            }

            // 执行结果
            int exitCode = process.waitFor();
            return exitCode;
        } catch (Exception e) {
            return -1;
        }
    }
}
