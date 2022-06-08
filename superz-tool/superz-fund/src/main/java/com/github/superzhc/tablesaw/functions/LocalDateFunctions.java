package com.github.superzhc.tablesaw.functions;

import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.StringColumn;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author superz
 * @create 2022/5/31 15:32
 **/
public class LocalDateFunctions {
    public static LongColumn date2Long(DateColumn dates) {
        LongColumn longs = LongColumn.create(dates.name() + "[Long]", dates.size());
        for (int i = 0, size = dates.size(); i < size; i++) {
            LocalDate date = dates.get(i);
            long timestamp = date.atStartOfDay(ZoneId.systemDefault()/*ZoneOffset.ofHours(+8)*/).toInstant().toEpochMilli();
            longs.set(i, timestamp);
        }
        return longs;
    }

    public static DateColumn long2Date(LongColumn timestamps) {
        DateColumn dates = DateColumn.create(timestamps.name() + "[Date]", timestamps.size());
        for (int i = 0, size = dates.size(); i < size; i++) {
            long timestamp = timestamps.get(i);
            LocalDate date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()).toLocalDate();
            dates.set(i, date);
        }
        return dates;
    }

    public static StringColumn date2String(DateColumn dates) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date2String(dates, formatter);
    }

    public static StringColumn date2String(DateColumn dates, DateTimeFormatter formatter) {
        StringColumn strings = StringColumn.create(dates.name() + "[String]", dates.size());
        for (int i = 0, size = dates.size(); i < size; i++) {
            LocalDate date = dates.get(i);
            strings.set(i, date.format(formatter));
        }
        return strings;
    }
}
