package com.github.superzhc.hadoop.flink.streaming.demo.userbehavior;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author superz
 * @create 2021/10/12 15:32
 */
public class UserbehaviorSource extends RichSourceFunction<UserBehavior> {
    private Boolean isRunning = true;
    private InputStream streamSource;

    private String path;

    public UserbehaviorSource(String path) {
        this.path = path;
    }

    @Override
    public void run(SourceContext<UserBehavior> ctx) throws Exception {
        streamSource = this.getClass().getClassLoader().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(streamSource));
        String line;
        while (isRunning && ((line = reader.readLine()) != null)) {
            String[] arr = line.split(",");
            UserBehavior userBehavior = new UserBehavior(Long.valueOf(arr[0]), Long.valueOf(arr[1]), Integer.valueOf(arr[2]), arr[3], Long.valueOf(arr[4]));
            ctx.collect(userBehavior);
        }
    }

    @Override
    public void cancel() {
        try {
            if (null != streamSource) {
                streamSource.close();
            }
            isRunning = false;
        } catch (Exception e) {
        }
    }
}
