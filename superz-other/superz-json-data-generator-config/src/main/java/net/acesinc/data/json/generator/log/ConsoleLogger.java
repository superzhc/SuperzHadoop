package net.acesinc.data.json.generator.log;

import java.util.Map;

/**
 * 输出到控制台，用于测试使用
 * @author superz
 * @create 2021/10/21 16:13
 */
public class ConsoleLogger implements EventLogger {
    @Override
    public void logEvent(String event, Map<String, Object> producerConfig) {
        System.out.println(event);
    }

    @Override
    public void shutdown() {

    }
}
