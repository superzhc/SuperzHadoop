package com.github.superzhc.hadoop.flink.streaming.time;

/**
 * 时间
 * <p>
 * Flink 支持如下 3 中时间概念：
 * 1. Event Time：事件产生的时间，它通常由事件中的时间戳描述
 * 2. Ingestion Time：事件进入 Flink 的时间
 * 3. Processing Time：事件被处理时当前系统的时间
 * <p>
 * 注意事项：
 * 1. 使用 Event Time 必须指定如何生成 Event Time 水印，这是表示 Event Time 进度的机制
 *
 * @author superz
 * @create 2021/10/9 17:09
 */
public class TimeMain {

}
