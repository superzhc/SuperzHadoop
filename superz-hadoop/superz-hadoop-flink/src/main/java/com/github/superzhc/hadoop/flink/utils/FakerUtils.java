package com.github.superzhc.hadoop.flink.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author superz
 * @create 2021/10/26 15:51
 */
public class FakerUtils {
    public static final String FAKER_DATETIME_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(FAKER_DATETIME_FORMAT, new Locale("us"));

    /**
     * 转换成毫秒时间戳
     *
     * @param value
     * @return
     */
    public static Long toTimestamp(String value) {
        // 直接使用 Instant 存在时区的问题
        //return Instant.from(formatter.parse(value)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return LocalDateTime.parse(value, formatter).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime toLocalDateTime(String value) {
        return LocalDateTime.parse(value, formatter).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date toDate(String value) {
        /* SimpleDateFormat 是线程不安全的，不放到静态变量中 */
        try {
            return new SimpleDateFormat(FAKER_DATETIME_FORMAT, new Locale("us")).parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static BigInteger toBigInteger(String value) {
        return new BigInteger(value);
    }

    public static Float toFloat(String value) {
        return Float.parseFloat(value);
    }

    public static Double toDouble(String value) {
        return Double.parseDouble(value);
    }

    public static BigDecimal toBigDecimal(String value) {
        return new BigDecimal(value);
    }
}
