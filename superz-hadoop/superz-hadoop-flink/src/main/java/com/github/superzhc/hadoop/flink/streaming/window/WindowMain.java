package com.github.superzhc.hadoop.flink.streaming.window;

/**
 * 窗口
 * <p>
 * Flink 支持两种划分窗口的方式：
 * 1. 根据时间划分：time-window
 * 2. 根据数量划分：count-window
 * <p>
 * Flink 支持的窗口有两个属性（size 和 interval）
 * 1. 如果 `size=interval`,那么就会形成 tumbling-window(无重叠数据)
 * 2. 如果 `size>interval`,那么就会形成 sliding-window(有重叠数据)
 * 3. 如果 `size<interval`,那么这种窗口将会丢失数据。比如每5秒钟，统计过去3秒的通过路口汽车的数据，将会漏掉2秒钟的数据。
 * <p>
 * 通过上面的组合，可以获取如下四种基本窗口：
 * 1. time-tumbling-window 无重叠数据的时间窗口，设置方式举例：timeWindow(Time.seconds(5))
 * 2. time-sliding-window 有重叠数据的时间窗口，设置方式举例：timeWindow(Time.seconds(5), Time.seconds(3))
 * 3. count-tumbling-window无重叠数据的数量窗口，设置方式举例：countWindow(5)
 * 4. count-sliding-window 有重叠数据的数量窗口，设置方式举例：countWindow(5,3)
 *
 * @author superz
 * @create 2021/10/9 16:43
 */
public class WindowMain {
}
