package com.github.superzhc.xxljob.handler.util;

import com.xxl.job.core.context.XxlJobHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * @author superz
 * @create 2021/7/27 19:15
 */
public class CommonConsumer implements Consumer<InputStream> {
    @Override
    public void accept(InputStream inputStream) {
        // 获取当前操作系统的编码
        String encoding = System.getProperty("sun.jnu.encoding");
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream, encoding))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = output.readLine()) != null) {
                result.append(line);
            }
            XxlJobHelper.log(result.toString());
        } catch (Exception e) {
            XxlJobHelper.log(e);
        }
    }
}
