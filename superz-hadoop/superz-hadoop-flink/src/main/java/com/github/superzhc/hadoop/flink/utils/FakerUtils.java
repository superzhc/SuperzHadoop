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
import java.util.Random;

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

    @Deprecated
    public static Double toDouble(String value) {
        return Double.parseDouble(value);
    }

    public static BigDecimal toBigDecimal(String value) {
        return new BigDecimal(value);
    }

    public static class Expression {
        private static final String EXPRESSION_TEMPLATE = "#{%s %s}";

        public static final String NAME=expression("Name.name");
        public static final String ID_CARD=regexify("(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)");
        public static final String QQ = regexify("[1-9][0-9]{4,10}");
        public static final String IP = regexify("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)");

        /**
         * 新增车的正则表达式
         */
        public static class Car {
            private static final String ORDINARY_LICENSE_PLATE_PATTERN = "[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}";
            private static final String NEW_ENERGY_LICENSE_PLATE_PATTERN = "[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF][A-HJ-NP-Z0-9][0-9]{4}))";
            private static final String LICENSE_PLATE_PATTERN = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|(DF[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})";
            private static final String[] LICENSE_PLATE_COLOR_PATTERN = {"蓝", "黄", "白", "绿", "黑"};
            /* 车架 VIN 正则表达式 */
            private static final String VIN_PATTERN = "[A-HJ-NPR-Z\\d]{8}[X\\d][A-HJ-NPR-Z\\d]{3}\\d{5}";

            /**
             * 车牌号（包含新能源车牌号）
             */
            public static final String LICESE_PLATE = regexify(LICENSE_PLATE_PATTERN);

            /**
             * 普通车牌号
             */
            public static final String ORDINARY_LICENSE_PLATE = regexify(ORDINARY_LICENSE_PLATE_PATTERN);

            /**
             * 新能源车牌号
             */
            public static final String NEW_ENERGY_LICENSE_PLATE = regexify(NEW_ENERGY_LICENSE_PLATE_PATTERN);

            /**
             * 车辆的 vin
             * <p>
             * 规则：
             * 1. 由大写字母和数字组成，长度17位；
             * 2. 字母不会出现O、Q、I三个字母；
             * 3. 第9位只能是【0-9】的数字和字母X;
             * 4. 第13-17位只能是数字；
             *
             * @return
             */
            public static final String VIN = regexify(VIN_PATTERN);
        }

        public static String name() {
            return expression("Name.name");
        }

        public static String age(int min, int max) {
            return numberBetween(min, max);
        }

        public static String numberBetween(int min, int max) {
            return expression("number.number_between", min, max);
        }

        public static String randomDouble(int maxNumberOfDecimals, int min, int max) {
            return expression("number.random_double", maxNumberOfDecimals, min, max);
        }

        /**
         * 2021年11月2日 superz add
         * <p>
         * 随机生成当前时间的过去 @param:second 秒的时间
         * <p>
         * 例子：
         * <p>
         * 当前时间 2021-11-2 16:38:15，设置的参数 second 为 5 秒，那么生成的时间将会在 [2021-11-2 16:38:10,2021-11-2 16:38:15) 之间
         *
         * @param second
         * @return
         */
        public static String pastDate(int second) {
            return expression("date.past", second, 0, "SECONDS");
        }

        /**
         * 随机生成当前时间之后 @param:second 秒的时间
         *
         * @param second
         * @return
         */
        public static String futureDate(int second) {
            return expression("date.future", second, 0, "SECONDS");
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

        public static String bothify(String pattern) {
            return expression("bothify", pattern);
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
