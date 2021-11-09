package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author superz
 * @create 2021/11/5 17:00
 */
public class TableWindowMain {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.getConfig().setAutoWatermarkInterval(1000);
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        // tEnv.executeSql(TableTimeMain.processingTimeDDL());
        tEnv.executeSql(TableTimeMain.eventTimeDDL(3));

        String sql;
        // sql = "SELECT sex,TUMBLE_START(process_time,INTERVAL '60' SECONDS),TUMBLE_END(process_time,INTERVAL '60' SECONDS),COUNT(name) FROM faker GROUP BY sex,TUMBLE(process_time,INTERVAL '60' SECONDS)";
        // sql = "SELECT sex,TUMBLE_START(timestamps,INTERVAL '60' SECONDS),TUMBLE_END(timestamps,INTERVAL '60' SECONDS),COUNT(name) FROM faker GROUP BY sex,TUMBLE(timestamps,INTERVAL '60' SECONDS)";
        sql = "SELECT sex,HOP_START(timestamps,INTERVAL '60' SECONDS,INTERVAL '3' MINUTES),HOP_END(timestamps,INTERVAL '60' SECONDS,INTERVAL '3' MINUTES),COUNT(name) FROM faker GROUP BY sex,HOP(timestamps,INTERVAL '60' SECONDS,INTERVAL '3' MINUTES)";
        Table table = tEnv.sqlQuery(sql);
        table.execute().print();
    }

    /**
     * 滚动窗口
     */
    private static void tumbleSql() {
        /**
         * 语法：
         * SELECT
         *     [gk],
         *     [TUMBLE_START(timeCol, size)], -- 窗口开始时间
         *     [TUMBLE_END(timeCol, size)],   -- 窗口结束时间
         *     agg1(col1),
         *     ...
         *     aggn(colN)
         * FROM Tab1
         * GROUP BY [gk], TUMBLE(timeCol, size)
         */
    }

    /**
     * 滑动窗口
     */
    private static void hopSql() {
        /**
         * 语法：
         * SELECT
         *     [gk],
         *     [HOP_START(timeCol, slide, size)] ,  -- 窗口开始时间
         *     [HOP_END(timeCol, slide, size)],     -- 窗口结束时间
         *     agg1(col1),
         *     ...
         *     aggN(colN)
         * FROM Tab1
         * GROUP BY [gk], HOP(timeCol, slide, size) -- slide:窗口滑动的大小；size：窗口的大小
         */
    }
}
