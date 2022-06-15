package com.github.superzhc.common.visualization.xchart;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/6/1 9:53
 **/
public class XChartFunctions {
    private static final DateTimeFormatter CUSTOM_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final Function<Double, String> timestamp2date = timestamp -> Instant.ofEpochMilli(timestamp.longValue()).atZone(ZoneOffset.ofHours(+8)).toLocalDate().format(CUSTOM_DATE_FORMATTER);
}
