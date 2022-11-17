package com.github.superzhc.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author superz
 * @create 2022/11/11 14:23
 **/
public class LocalDateTimeUtils {
    public static LocalDateTime convert4date(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        return localDateTime;
    }

    public static Date convert2datetime(LocalDateTime localDateTime) {
        Date date = Date.from(localDateTime.atZone(ZoneOffset.ofHours(8)).toInstant());
        return date;
    }

    public static Date convert2date(LocalDate localDate) {
        Date date = Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
        return date;
    }

    public static LocalDateTime convert4timestamp(long timestamp) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        return localDateTime;
    }

    public static long convert2timestamp(LocalDate localDate) {
        long timestamp = localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
        return timestamp;
    }

    public static long convert2timestamp(LocalDateTime localDateTime) {
        long timestamp = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        return timestamp;
    }

    public static String format(LocalDateTime localDateTime){
        return format(localDateTime,"yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static String format(LocalDateTime localDateTime,String pattern){
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDate localDate){
        return format(localDate,"yyyy-MM-dd");
    }

    public static String format(LocalDate localDate,String pattern){
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }
}
