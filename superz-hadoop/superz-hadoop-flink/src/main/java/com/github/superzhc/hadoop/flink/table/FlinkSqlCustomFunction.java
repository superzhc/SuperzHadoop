package com.github.superzhc.hadoop.flink.table;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.table.functions.ScalarFunction;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author superz
 * @create 2021/11/9 15:22
 */
public class FlinkSqlCustomFunction {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        // 注册 udf
        // 该方式被废弃
        // tEnv.registerFunction("timestamp_converter",new UDF.UDFTimestampConverter());
        tEnv.createTemporarySystemFunction("timestamp_converter", new UDF.UDFTimestampConverter());
        tEnv.createTemporarySystemFunction("proportion", new UDAF.UDAFProportion());

        tEnv.executeSql(TableTimeMain.eventTimeDDL(3));

        String sql = "SELECT" +
                " age," +
                " timestamp_converter(TUMBLE_START(timestamps,INTERVAL '60' SECONDS),'YYYY-MM-dd HH:mm:ss','+09:00') as wStart," +
                " timestamp_converter(TUMBLE_END(timestamps,INTERVAL '60' SECONDS),'YYYY-MM-dd HH:mm:ss','+09:00') as wEnd," +
                //" TUMBLE_END(timestamps,INTERVAL '60' SECONDS),"+
                //" COUNT(name)" +
                " proportion(sex)" +
                " FROM faker" +
                " GROUP BY age,TUMBLE(timestamps,INTERVAL '60' SECONDS)";
        Table table = tEnv.sqlQuery(sql);
        table.execute().print();
    }

    /**
     * 自定义标量函数(User Defined Scalar Function)
     * <p>
     * 一行输入一行输出
     */
    public static class UDF {
        public static class UDFTimestampConverter extends ScalarFunction {
            /**
             * 默认转换为北京时间
             *
             * @param timestamp Flink Timestamp 格式时间
             * @param format    目标格式，如"YYYY-MM-dd HH:mm:ss"
             * @return 目标时区的时间
             */
            public String eval(Timestamp timestamp, String format) {
                //LocalDateTime noZoneDateTime = timestamp.toLocalDateTime();
                //ZonedDateTime utcZoneDateTime = ZonedDateTime.of(noZoneDateTime, ZoneId.of("UTC"));
                //ZonedDateTime targetZoneDateTime = utcZoneDateTime.withZoneSameInstant(ZoneId.of("+08:00"));
                //return targetZoneDateTime.format(DateTimeFormatter.ofPattern(format));
                return eval(timestamp, format, "+08:00");
            }

            /**
             * 转换为指定时区时间
             *
             * @param timestamp  Flink Timestamp 格式时间
             * @param format     目标格式，如"YYYY-MM-dd HH:mm:ss"
             * @param zoneOffset 目标时区偏移量
             * @return 目标时区的时间
             */
            public String eval(Timestamp timestamp, String format, String zoneOffset) {
                LocalDateTime noZoneDateTime = timestamp.toLocalDateTime();
                ZonedDateTime utcZoneDateTime = ZonedDateTime.of(noZoneDateTime, ZoneId.of("UTC"));

                ZonedDateTime targetZoneDateTime = utcZoneDateTime.withZoneSameInstant(ZoneId.of(zoneOffset));

                return targetZoneDateTime.format(DateTimeFormatter.ofPattern(format));
            }
        }
    }

    /**
     * 自定义聚合函数
     * <p>
     * 多行输入一行输出
     */
    public static class UDAF {
        public static class UDAFProportion extends AggregateFunction<String, Tuple3<Long, Long, Long>> {
            @Override
            public String getValue(Tuple3<Long, Long, Long> accumulator) {
                if (accumulator.f0 == 0L) {
                    return "无数据";
                }
                return "男性比例：" + ((accumulator.f1).doubleValue() / accumulator.f0) + "，女性比例：" + (accumulator.f2.doubleValue() / accumulator.f0);
            }

            /**
             * 定义如何根据输入更新Accumulator
             *
             * @param accumulator Accumulator
             * @param sex         输入
             */
            public void accumulate(Tuple3<Long, Long, Long> accumulator, String sex) {
                accumulator.f0 += 1;
                if ("男".equals(sex)) {
                    accumulator.f1 += 1;
                } else {
                    accumulator.f2 += 1;
                }
            }

            @Override
            public Tuple3<Long, Long, Long> createAccumulator() {
                return Tuple3.of(
                        0L,// 总数
                        0L,//男
                        0L//女
                );
            }
        }
    }

    /**
     * 自定义表函数
     * <p>
     * 一行输入多行输出或一列输入多列输出
     */
    public static class UDTF {
    }
}
