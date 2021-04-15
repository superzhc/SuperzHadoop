package com.github.superzhc.flink.manage.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Consumer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author superz
 * @create 2021/4/10 14:17
 */
@Slf4j
public class ProcessUtil {

    /**
     * Java调用进程，并处理进程输出的流
     *
     * @param command  命令语句
     * @param consumer 消费进程输出流的对象
     * @return
     */
    public static int exec(String command, Consumer<InputStream> consumer) {
        log.debug("[{}] 执行命令：{}", DateUtil.formatDateTime(new Date()));
        Process process = null;
        try {
            process = RuntimeUtil.exec(command);

            if (null != consumer) {
                consumer.accept(process.getInputStream());
            }

            // 执行结果
            int exitCode = process.waitFor();
            log.debug("[{}] 执行命令：{}的结果[{}]", DateUtil.formatDateTime(new Date()), command, exitCode);
            return exitCode;
        } catch (Exception e) {
            return -1;
        } finally {
            RuntimeUtil.destroy(process);
        }
    }
}
