package com.github.superzhc.hadoop.kafka.demo.task;

import com.github.superzhc.common.utils.MapUtils;
import com.github.superzhc.data.news.Jin10;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author superz
 * @create 2023/3/18 17:32
 **/
public class TimerDemo {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<Map<String, Object>> data = Jin10.news();
                System.out.println(MapUtils.print(data));
            }
        }, 1000, 1000 * 30);
    }
}
