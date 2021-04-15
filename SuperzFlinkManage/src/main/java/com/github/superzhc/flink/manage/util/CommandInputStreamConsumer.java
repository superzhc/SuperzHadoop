package com.github.superzhc.flink.manage.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Consumer;

/**
 * 命令的输出流处理
 *
 * @author superz
 * @create 2021/4/15 16:11
 */
@Slf4j
public class CommandInputStreamConsumer implements Consumer<InputStream> {

    private List<String> container;

    public CommandInputStreamConsumer(List<String> container) {
        this.container = container;
    }

    @Override
    public void accept(InputStream stream) {
        try {
            BufferedReader brs = new BufferedReader(new InputStreamReader(stream));
            while (true) {
                String line = brs.readLine();
                if (line == null) {
                    break;
                }
                container.add(line);

                // 打印执行日志
                log.debug(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getContainer() {
        return container;
    }
}
