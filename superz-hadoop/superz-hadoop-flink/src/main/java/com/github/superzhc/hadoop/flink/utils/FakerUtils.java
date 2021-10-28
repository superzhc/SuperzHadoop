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

    public static class Expression{
        private static final String EXPRESSION_TEMPLATE = "#{%s %s}";

        public static String name() {
            return expression("Name.name");
        }

        public static String age(int min, int max) {
            return expression("number.number_between", min, max);
        }

        public static String options(Object one, Object... params) {
            String template = "(%s){1}";
            StringBuilder optionSb = new StringBuilder();
            optionSb.append(one);
            if (null != params) {
                for (Object param : params) {
                    optionSb.append("|");
                    optionSb.append(param);
                }
            }
            return regexify(String.format(template, optionSb.toString()));
        }

        public static String regexify(String pattern) {
            return expression("regexify", pattern);
        }

        public static String expression(String method, Object... params) {
            StringBuilder sb = new StringBuilder();
            if (null != params && params.length > 0) {
                for (int i = 0, len = params.length; i < len; i++) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append("'").append(params[i]).append("'");
                }
            }
            return String.format(EXPRESSION_TEMPLATE, method, sb.toString());
        }
    }
}
