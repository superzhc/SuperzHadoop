package com.github.superzhc.tablesaw.functions;

import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.StringColumn;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author superz
 * @create 2022/5/31 15:32
 **/
public class LocalDateFunctions {
    public static LongColumn convert2Long(DateColumn dates) {
        LongColumn longs = LongColumn.create(dates.name() + "[Long]", dates.size());
        for (int i = 0, size = dates.size(); i < size; i++) {
            LocalDate date = dates.get(i);
            long timestamp = date.atStartOfDay(ZoneOffset.ofHours(+8)).toInstant().toEpochMilli();
            longs.set(i, timestamp);
        }
        return longs;
    }

    public static StringColumn convert2String(DateColumn dates) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return convert2String(dates, formatter);
    }

    public static StringColumn convert2String(DateColumn dates, DateTimeFormatter formatter) {
        StringColumn strings = StringColumn.create(dates.name() + "[String]", dates.size());
        for (int i = 0, size = dates.size(); i < size; i++) {
            LocalDate date = dates.get(i);
            strings.set(i, date.format(formatter));
        }
        return strings;
    }
}
